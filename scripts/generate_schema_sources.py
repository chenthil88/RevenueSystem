#!/usr/bin/env python3
"""Generate Java schema layer from ProjectDetails/db_script.sql."""
from __future__ import annotations

import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SQL_PATH = ROOT.parent / "ProjectDetails" / "db_script.sql"
JAVA = ROOT / "src/main/java/com/revrec/engine"


def preprocess(sql: str) -> str:
    sql = sql.replace("DECIMAL(65, 27),,", "DECIMAL(65, 27),")
    sql = sql.replace("NUMERIC(18, 2),,", "NUMERIC(18, 2),")
    sql = re.sub(
        r"itemNumber\s+VARCHAR\(255\)\)\s*\n",
        "itemNumber VARCHAR(255),\n",
        sql,
        flags=re.IGNORECASE,
    )
    sql = re.sub(r",\s*\n\s*\)\s*;", "\n);", sql)
    return sql


def to_java_identifier(sql_name: str) -> str:
    s = sql_name.strip("`")
    return (s[0].lower() + s[1:]) if s else s


def to_java_class_name(table_sql: str) -> str:
    t = table_sql.strip("`")
    return "".join(p[:1].upper() + p[1:] for p in re.split(r"[^a-zA-Z0-9]+", t) if p)


# Row packages under com.revrec.engine.domain — must match manual layout in src/.
METADATA_JAVA_CLASSES = frozenset(
    {
        "Calendar",
        "Currency",
        "CurrentOpenPeriod",
        "PerformanceObligationTemplate",
        "PerformanceObligationRule",
        "PerformanceObligationRuleFilter",
        "ArrangementGroupingTemplate",
        "ArrangementGroupingHierarchy",
        "ArrangementGroupingFilter",
        "StandaloneSellPriceHierarchy",
        "StandaloneSellPriceTemplate",
        "StandaloneSellPriceBatchHeader",
        "StandaloneSellPriceBatchDetails",
        "JournalAccountsSetup",
        "ContractModificationHeader",
        "ContractModificationDetails",
    }
)

# Nested under service/JournalEntries/ — matches src layout for journal entry tables.
ENGINE_JOURNAL_ENTRIES_JAVA_BASES = frozenset({"AllocationJournalEntries", "RevenueJournalEntries"})

# Nested under service/ArrangementOrder/ — arrangement order–related row types.
ENGINE_ARRANGEMENT_ORDER_JAVA_BASES = frozenset(
    {
        "ArrangementOrderDetails",
        "ArrangementAllocationDetails",
        "ArrangementOrderAccountDetails",
        "ArrangementOrderAttributes",
    }
)


def schema_layer(table_sql: str) -> str:
    base = to_java_class_name(table_sql)
    return "metadataservice" if base in METADATA_JAVA_CLASSES else "service"


def schema_record_package(table_sql: str) -> str:
    """e.g. ...metadataservice.Calendar, ...service.JournalEntries.*, ...service.ArrangementOrder.*."""
    base = to_java_class_name(table_sql)
    layer = schema_layer(table_sql)
    if layer == "service" and base in ENGINE_JOURNAL_ENTRIES_JAVA_BASES:
        return f"com.revrec.engine.domain.service.JournalEntries.{base}"
    if layer == "service" and base in ENGINE_ARRANGEMENT_ORDER_JAVA_BASES:
        return f"com.revrec.engine.domain.service.ArrangementOrder.{base}"
    return f"com.revrec.engine.domain.{layer}.{base}"


def schema_record_source_dir(table_sql: str) -> Path:
    """Directory for table row types (e.g. .../service/JournalEntries/... or .../service/ArrangementOrder/...)."""
    base = to_java_class_name(table_sql)
    layer = schema_layer(table_sql)
    root = JAVA / "domain" / layer
    if layer == "service" and base in ENGINE_JOURNAL_ENTRIES_JAVA_BASES:
        root = root / "JournalEntries"
    elif layer == "service" and base in ENGINE_ARRANGEMENT_ORDER_JAVA_BASES:
        root = root / "ArrangementOrder"
    return root / base


def schema_service_package(table_sql: str) -> str:
    """TiDB/Redis persistence services under common/... (parallel to migrate_jdbc_to_services.py)."""
    dom = schema_record_package(table_sql)
    suffix = dom.removeprefix("com.revrec.engine.domain.")
    if suffix.startswith("metadataservice."):
        return "com.revrec.engine.common." + suffix
    if suffix.startswith("service.JournalEntries."):
        return (
            "com.revrec.engine.common.service.JournalEntries."
            + suffix.removeprefix("service.JournalEntries.")
        )
    if suffix.startswith("service."):
        return "com.revrec.engine.common.service." + suffix.removeprefix("service.")
    return "com.revrec.engine.common.service." + suffix


def schema_service_source_dir(table_sql: str) -> Path:
    pkg = schema_service_package(table_sql)
    parts = pkg.removeprefix("com.revrec.engine.").split(".")
    return JAVA.joinpath(*parts)


def strip_pk(type_blob: str) -> str:
    return re.sub(r"(?i)\s+primary\s+key\s*$", "", type_blob).strip()


def sql_type_to_java(type_blob: str) -> str:
    t = strip_pk(type_blob).lower()
    t = re.sub(r"\s+", " ", t)
    if t.startswith("bigint"):
        return "Long"
    if t.startswith("boolean"):
        return "Boolean"
    if t.startswith("datetime"):
        return "java.time.LocalDateTime"
    if t.startswith("date"):
        return "java.time.LocalDate"
    if "decimal" in t or "numeric" in t:
        return "java.math.BigDecimal"
    if t.startswith("varchar") or t.startswith("char"):
        return "String"
    return "String"


def build_fields(body: str) -> list[tuple[str, str, str, bool]]:
    """(sql_name, java_name, java_type, is_pk)."""
    rows: list[tuple[str, str, str, bool]] = []
    seen_java: set[str] = set()
    for raw in body.splitlines():
        line = raw.strip()
        if not line or line.startswith("--"):
            continue
        if line in (")", ");") or re.match(r"^\)\s*;\s*$", line):
            continue
        line = line.rstrip(",")
        if not line:
            continue
        if ");" in line:
            line = line[: line.index(");")].strip().rstrip(",")
        if not line:
            continue
        is_pk = bool(re.search(r"(?i)\bprimary\s+key\b", line))
        line = re.sub(r"(?i)\s+primary\s+key\s*$", "", line).strip()
        m = re.match(r"^(\w+)\s+(.+)$", line)
        if not m:
            continue
        sql_name, rest = m.group(1), m.group(2)
        jt = sql_type_to_java(rest)
        jname = to_java_identifier(sql_name)
        base, n = jname, 2
        while jname in seen_java:
            jname = f"{base}_{n}"
            n += 1
        seen_java.add(jname)
        rows.append((sql_name, jname, jt, is_pk))
    return rows


def find_pk_field(fields: list[tuple[str, str, str, bool]]) -> tuple[str, str, str]:
    for sql_name, jname, jt, pk in fields:
        if pk:
            return sql_name, jname, jt
    f0 = fields[0]
    return f0[0], f0[1], f0[2]


def is_boolean_is_accessor(jname: str, jt: str) -> bool:
    return jt == "Boolean" and bool(re.match(r"^is[A-Z]", jname))


def bean_getter_name(jname: str, jt: str) -> str:
    if is_boolean_is_accessor(jname, jt):
        return jname
    return "get" + jname[0].upper() + jname[1:]


def extract_tables(sql: str) -> list[tuple[str, str]]:
    lines = sql.splitlines()
    out: list[tuple[str, str]] = []
    cre = re.compile(r"(?i)^\s*create\s+table\s+(\S+)\s*\(\s*$")
    i = 0
    while i < len(lines):
        m = cre.match(lines[i])
        if not m:
            i += 1
            continue
        tname = m.group(1).strip()
        i += 1
        body_lines: list[str] = []
        while i < len(lines):
            raw = lines[i]
            st = raw.strip()
            if re.match(r"^\)\s*;\s*$", st):
                i += 1
                break
            if ");" in raw:
                before = raw[: raw.index(");")].rstrip()
                if before.strip():
                    body_lines.append(before)
                i += 1
                break
            body_lines.append(raw)
            i += 1
        out.append((tname, "\n".join(body_lines)))
    return out


def emit_row_interface(table: str, fields: list[tuple[str, str, str, bool]]) -> str:
    base = to_java_class_name(table)
    used = sorted({jt for _, _, jt, _ in fields if "." in jt})
    imp = "".join(f"import {u};\n" for u in used)
    if imp:
        imp += "\n"
    sigs = []
    for _, jn, jt, _ in fields:
        short = jt.split(".")[-1]
        if is_boolean_is_accessor(jn, jt):
            sigs.append(f"    {short} {jn}();")
        else:
            sigs.append(f"    {short} {bean_getter_name(jn, jt)}();")
    methods = "\n".join(sigs)
    pkg = schema_record_package(table)
    return (
        f"package {pkg};\n\n"
        f"{imp}"
        "/**\n"
        f" * Row contract for {{@link {base}Record}} (JavaBean-style accessors; TiDB table `{table}`).\n"
        " */\n"
        f"public interface {base} {{\n\n"
        f"{methods}\n"
        "}\n"
    )


def emit_record_delegate_methods(fields: list[tuple[str, str, str, bool]]) -> str:
    lines: list[str] = []
    for _, jn, jt, _ in fields:
        if is_boolean_is_accessor(jn, jt):
            continue
        short = jt.split(".")[-1]
        g = bean_getter_name(jn, jt)
        lines.append("    @Override")
        lines.append(f"    public {short} {g}() {{")
        lines.append(f"        return {jn};")
        lines.append("    }")
        lines.append("")
    return "\n".join(lines).rstrip() + ("\n" if lines else "")


def emit_record(table: str, fields: list[tuple[str, str, str, bool]]) -> str:
    base = to_java_class_name(table)
    cls = base + "Record"
    parts = [f"        {jt.split('.')[-1]} {jn}" for _, jn, jt, _ in fields]
    delegates = emit_record_delegate_methods(fields)
    tail = f'    public static final String TABLE_NAME = "{table}";\n'
    if delegates.strip():
        tail += "\n" + delegates
    pkg = schema_record_package(table)
    return (
        f"package {pkg};\n\n"
        "import java.io.Serializable;\n"
        "import java.math.BigDecimal;\n"
        "import java.time.LocalDate;\n"
        "import java.time.LocalDateTime;\n\n"
        "/**\n"
        f" * Row mapped from TiDB table `{table}`.\n"
        " */\n"
        f"public record {cls}(\n"
        + ",\n".join(parts)
        + f"\n) implements {base}, Serializable {{\n"
        + tail
        + "}\n"
    )


def emit_mapper(table: str, fields: list[tuple[str, str, str, bool]]) -> str:
    base = to_java_class_name(table)
    cls = base + "Record"
    mapper = base + "RecordMapper"
    pkg = schema_record_package(table)
    lines = [
        f"package {pkg};",
        "",
        "import java.sql.ResultSet;",
        "import java.sql.SQLException;",
        "import org.springframework.jdbc.core.RowMapper;",
        "import org.springframework.lang.NonNull;",
        "import org.springframework.stereotype.Component;",
        "",
        "/**",
        f" * Maps JDBC columns to {{@link {cls}}} by column label (order-independent).",
        " */",
        "@Component",
        f"public final class {mapper} implements RowMapper<{cls}> {{",
        "",
        "    @Override",
        f"    public @NonNull {cls} mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {{",
    ]
    for sql_name, jn, jt, _ in fields:
        lit = sql_name.replace("\\", "\\\\").replace('"', '\\"')
        if jt == "Long":
            lines.append(f'        var {jn} = rs.getObject("{lit}", Long.class);')
        elif jt == "Boolean":
            lines.append(f'        var {jn} = rs.getObject("{lit}", Boolean.class);')
        elif jt == "java.time.LocalDate":
            lines.append(f'        var {jn} = rs.getObject("{lit}", java.time.LocalDate.class);')
        elif jt == "java.time.LocalDateTime":
            lines.append(f'        var {jn} = rs.getObject("{lit}", java.time.LocalDateTime.class);')
        elif jt == "java.math.BigDecimal":
            lines.append(f'        var {jn} = rs.getBigDecimal("{lit}");')
        else:
            lines.append(f'        var {jn} = rs.getString("{lit}");')
    lines.append(f"        return new {cls}(")
    lines.append(",\n".join(f"                {jn}" for _, jn, _, _ in fields))
    lines.append("        );")
    lines.append("    }")
    lines.append("}")
    lines.append("")
    return "\n".join(lines)


def sql_select_list(fields: list[tuple[str, str, str, bool]]) -> str:
    return ", ".join(f"`{sn}`" for sn, _, _, _ in fields)


def emit_schema_table_service(table: str, fields: list[tuple[str, str, str, bool]]) -> str:
    """Spring @Service for TiDB + optional Redis cache (no separate repository interface)."""
    base = to_java_class_name(table)
    cls = base + "Record"
    svc = base + "Service"
    mapper = base + "RecordMapper"
    pk_sql, pk_java, _pk_jt = find_pk_field(fields)
    sel = sql_select_list(fields)
    rec_pkg = schema_record_package(table)
    svc_pkg = schema_service_package(table)
    put_key = f"row.{pk_java}()"
    return (
        f"package {svc_pkg};\n\n"
        f"import {rec_pkg}.{cls};\n"
        f"import com.revrec.engine.integration.nosql.NoSqlRecordServer;\n"
        f"import {rec_pkg}.{mapper};\n"
        "import java.util.List;\n"
        "import java.util.Map;\n"
        "import java.util.Optional;\n"
        "import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;\n"
        "import org.springframework.stereotype.Service;\n\n"
        "/**\n"
        f" * TiDB-backed persistence with optional Redis materialization.\n"
        " */\n"
        "@Service\n"
        f"public class {svc} {{\n\n"
        "    private final NamedParameterJdbcTemplate jdbc;\n"
        f"    private final {mapper} rowMapper;\n"
        "    private final NoSqlRecordServer noSqlRecordServer;\n\n"
        f"    public {svc}(\n"
        "            NamedParameterJdbcTemplate jdbc,\n"
        f"            {mapper} rowMapper,\n"
        "            NoSqlRecordServer noSqlRecordServer) {\n"
        "        this.jdbc = jdbc;\n"
        "        this.rowMapper = rowMapper;\n"
        "        this.noSqlRecordServer = noSqlRecordServer;\n"
        "    }\n\n"
        f"    private static final String SELECT =\n"
        f'            "SELECT {sel} FROM `{table}`";\n\n'
        f"    public Optional<{cls}> findById(Long {pk_java}) {{\n"
        f'        var list = jdbc.query(SELECT + " WHERE `{pk_sql}` = :id", Map.of("id", {pk_java}), rowMapper);\n'
        "        return list.stream().findFirst();\n"
        "    }\n\n"
        f"    public Optional<{cls}> findByIdCached(Long {pk_java}) {{\n"
        "        return noSqlRecordServer\n"
        f"                .get({cls}.TABLE_NAME, String.valueOf({pk_java}), {cls}.class)\n"
        f"                .or(() -> findById({pk_java}).map(row -> {{\n"
        "                    noSqlRecordServer.put(\n"
        f"                            {cls}.TABLE_NAME, String.valueOf({put_key}), row);\n"
        "                    return row;\n"
        "                }));\n"
        "    }\n\n"
        f"    public List<{cls}> findAll(int limit, int offset) {{\n"
        '        return jdbc.query(SELECT + " LIMIT :limit OFFSET :offset",\n'
        "                Map.of(\"limit\", limit, \"offset\", offset), rowMapper);\n"
        "    }\n"
        "}\n"
    )


def emit_schema_tables(tables: list[str]) -> str:
    body = "\n".join(f'    public static final String {re.sub(r"[^a-zA-Z0-9]+", "_", t).upper().strip("_")} = "{t}";' for t in tables)
    return (
        "package com.revrec.engine.domain;\n\n"
        "/**\n"
        " * Literal table identifiers matching {@code db_script.sql}.\n"
        " */\n"
        "public final class SchemaTables {\n\n"
        "    private SchemaTables() {}\n\n"
        + body
        + "\n}\n"
    )


def main() -> int:
    sql = preprocess(SQL_PATH.read_text(encoding="utf-8"))
    tables = extract_tables(sql)
    if not tables:
        print("No tables found", file=sys.stderr)
        return 1

    (JAVA / "domain").mkdir(parents=True, exist_ok=True)
    (JAVA / "domain/metadataservice").mkdir(parents=True, exist_ok=True)
    (JAVA / "domain/service").mkdir(parents=True, exist_ok=True)
    (JAVA / "domain/service/JournalEntries").mkdir(parents=True, exist_ok=True)
    (JAVA / "domain/service/ArrangementOrder").mkdir(parents=True, exist_ok=True)
    (JAVA / "integration/nosql").mkdir(parents=True, exist_ok=True)

    tnames: list[str] = []
    for tname, body in tables:
        fields = build_fields(body)
        if not fields:
            print(f"Skip empty table body: {tname}", file=sys.stderr)
            continue
        tnames.append(tname)
        base = to_java_class_name(tname)
        record_dir = schema_record_source_dir(tname)
        record_dir.mkdir(parents=True, exist_ok=True)
        (record_dir / f"{base}.java").write_text(
            emit_row_interface(tname, fields), encoding="utf-8"
        )
        (record_dir / f"{base}Record.java").write_text(
            emit_record(tname, fields), encoding="utf-8"
        )
        (record_dir / f"{base}RecordMapper.java").write_text(
            emit_mapper(tname, fields), encoding="utf-8"
        )
        svc_dir = schema_service_source_dir(tname)
        svc_dir.mkdir(parents=True, exist_ok=True)
        (svc_dir / f"{base}Service.java").write_text(
            emit_schema_table_service(tname, fields), encoding="utf-8"
        )

    (JAVA / "domain/SchemaTables.java").write_text(emit_schema_tables(tnames), encoding="utf-8")
    print(f"Generated {len(tnames)} tables under domain + common/service")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
