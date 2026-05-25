#!/usr/bin/env python3
"""One-off: Jdbc*RecordRepository -> *Service under common/...; remove *RecordRepository interfaces."""
from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ENGINE = ROOT / "src/main/java/com/revrec/engine"
SCHEMA = ENGINE / "repository/schema"


def domain_row_package(java: str) -> str:
    for pat in (
        r"import (com\.revrec\.engine\.domain\.[\w.]+)\.\w+Record;",
        r"import (com\.revrec\.engine\.domain\.[\w.]+)\.\w+RecordMapper;",
    ):
        m = re.search(pat, java)
        if m:
            return m.group(1)
    raise ValueError("Could not find domain row import")


def to_service_package(domain_pkg: str) -> str:
    suffix = domain_pkg.removeprefix("com.revrec.engine.domain.")
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


def out_directory(service_pkg: str) -> Path:
    parts = service_pkg.removeprefix("com.revrec.engine.").split(".")
    return ENGINE.joinpath(*parts)


def transform(java: str, jdbc_class: str, stem: str, service_class: str, service_pkg: str) -> str:
    java = java.replace("package com.revrec.engine.repository.schema;", f"package {service_pkg};")
    java = re.sub(
        rf"public class {re.escape(jdbc_class)} implements {re.escape(stem)}RecordRepository",
        f"public class {service_class}",
        java,
    )
    java = java.replace(f"public {jdbc_class}(", f"public {service_class}(")
    java = java.replace("@Repository", "@Service")
    java = java.replace(
        "import org.springframework.stereotype.Repository;",
        "import org.springframework.stereotype.Service;",
    )
    java = re.sub(
        r"/\*\*[\s\S]*?TiDB-backed[\s\S]*?\*/\s*@Service",
        "/**\n * TiDB-backed persistence with optional Redis materialization.\n */\n@Service",
        java,
        count=1,
    )
    java = re.sub(r"^\s*@Override\s*\n", "", java, flags=re.MULTILINE)
    return java


def main() -> None:
    for path in sorted(SCHEMA.glob("Jdbc*RecordRepository.java")):
        raw = path.read_text(encoding="utf-8")
        jdbc_class = path.stem
        m = re.match(r"Jdbc(.+)RecordRepository$", jdbc_class)
        if not m:
            raise SystemExit(f"Unexpected filename: {path.name}")
        stem = m.group(1)
        service_class = stem + "Service"
        domain_pkg = domain_row_package(raw)
        service_pkg = to_service_package(domain_pkg)
        out_dir = out_directory(service_pkg)
        out_dir.mkdir(parents=True, exist_ok=True)
        out_file = out_dir / f"{service_class}.java"
        new_java = transform(raw, jdbc_class, stem, service_class, service_pkg)
        out_file.write_text(new_java, encoding="utf-8")
        path.unlink()

        iface = SCHEMA / f"{stem}RecordRepository.java"
        if iface.is_file():
            iface.unlink()

    for orphan in SCHEMA.glob("*RecordRepository.java"):
        if orphan.name != "SchemaRecordRepository.java":
            orphan.unlink()
    schema_iface = SCHEMA / "SchemaRecordRepository.java"
    if schema_iface.is_file():
        schema_iface.unlink()

    # remove empty repository/schema if possible
    try:
        next(SCHEMA.iterdir())
    except StopIteration:
        SCHEMA.rmdir()


if __name__ == "__main__":
    main()
