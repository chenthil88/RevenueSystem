#!/usr/bin/env python3
"""Rename arrangement -> RevenueContract in RevRecProject/src/main/java/com."""
from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1] / "src/main/java/com"

# Longest-first (same rules as db_script.sql + Java packages)
REPLACEMENTS = [
    ("arrangementbatchcollection", "revenuecontractbatchcollection"),
    ("ArrangementContractModificationDetails", "RevenueContractModificationDetails"),
    ("ArrangementGroupingTemplateId", "RevenueContractGroupingTemplateId"),
    ("ARRANGEMENTGROUPINGTEMPLATE", "REVENUECONTRACTGROUPINGTEMPLATE"),
    ("ARRANGEMENTGROUPINGHIERARCHY", "REVENUECONTRACTGROUPINGHIERARCHY"),
    ("ARRANGEMENTGROUPINGFILTER", "REVENUECONTRACTGROUPINGFILTER"),
    ("ARRANGEMENTBATCHHEADER", "REVENUECONTRACTBATCHHEADER"),
    ("ARRANGEMENTBATCHDETAILS", "REVENUECONTRACTBATCHDETAILS"),
    ("ARRANGEMENTHEAD", "REVENUECONTRACTHEADER"),
    ("ARRANGEMENTPOBDETAILS", "REVENUECONTRACTPOBDETAILS"),
    ("ARRANGEMENTORDERDETAILS", "REVENUECONTRACTORDERDETAILS"),
    ("ARRANGEMENTALLOCATIONDETAILS", "REVENUECONTRACTALLOCATIONDETAILS"),
    ("ARRANGEMENTORDERACCOUNTDETAILS", "REVENUECONTRACTORDERACCOUNTDETAILS"),
    ("ARRANGEMENTORDERATTRIBUTES", "REVENUECONTRACTORDERATTRIBUTES"),
    ("ARRANGEMENTBILLINGDETAILS", "REVENUECONTRACTBILLINGDETAILS"),
    ("ARRANGEMENTBILLINGACCOUNTDETAILS", "REVENUECONTRACTBILLINGACCOUNTDETAILS"),
    ("ArrangementBillingAccountDetails", "RevenueContractBillingAccountDetails"),
    ("ArrangementGroupingTemplate", "RevenueContractGroupingTemplate"),
    ("ArrangementGroupingHierarchy", "RevenueContractGroupingHierarchy"),
    ("ArrangementGroupingFilter", "RevenueContractGroupingFilter"),
    ("ArrangementAllocationDetails", "RevenueContractAllocationDetails"),
    ("ArrangementBillingDetails", "RevenueContractBillingDetails"),
    ("ArrangementBatchDetails", "RevenueContractBatchDetails"),
    ("ArrangementBatchHeader", "RevenueContractBatchHeader"),
    ("ArrangementPobDetails", "RevenueContractPobDetails"),
    ("ArrangementHead", "RevenueContractHeader"),
    ("arrangementOrderDetailsId", "revenueContractOrderDetailsId"),
    ("arrangementBillingAccountDetails", "revenueContractBillingAccountDetails"),
    ("arrangementOrderAccountDetails", "revenueContractOrderAccountDetails"),
    ("arrangementOrderAttributes", "revenueContractOrderAttributes"),
    ("arrangementAllocationDetails", "revenueContractAllocationDetails"),
    ("arrangementBillingDetails", "revenueContractBillingDetails"),
    ("arrangementBatchHeader", "revenueContractBatchHeader"),
    ("arrangementBatchDetails", "revenueContractBatchDetails"),
    ("arrangementRecordProcessed", "revenueContractRecordProcessed"),
    ("arrangementOrderDetails", "revenueContractOrderDetails"),
    ("ArrangementGroupValue", "RevenueContractGroupValue"),
    ("ArrangementProcess", "RevenueContractProcess"),
    ("ArrangementGrouping", "RevenueContractGrouping"),
    ("arrangementPobDetails", "revenueContractPobDetails"),
    ("isArrangementPosted", "isRevenueContractPosted"),
    ("arrangementHead", "revenueContractHeader"),
    ("ArrangementOrder", "RevenueContractOrder"),
    ("ArrangementId", "RevenueContractId"),
    ("arrangementId", "revenueContractId"),
    ("arrangmentLineId", "revenueContractLineId"),
]


def apply_replacements(text: str) -> str:
    for old, new in REPLACEMENTS:
        text = text.replace(old, new)
    return text


def rename_path_component(name: str) -> str | None:
    new = apply_replacements(name)
    return new if new != name else None


def main() -> None:
    # 1) Update file contents
    for path in sorted(ROOT.rglob("*")):
        if path.is_file() and path.suffix in {".java", ".kt", ".xml", ".properties", ".md"}:
            original = path.read_text(encoding="utf-8")
            updated = apply_replacements(original)
            if updated != original:
                path.write_text(updated, encoding="utf-8")
                print(f"updated: {path.relative_to(ROOT)}")

    # 2) Rename files (deepest paths first)
    files = sorted(ROOT.rglob("*"), key=lambda p: len(p.parts), reverse=True)
    for path in files:
        if not path.is_file():
            continue
        new_name = rename_path_component(path.name)
        if new_name:
            path.rename(path.with_name(new_name))
            print(f"file: {path.name} -> {new_name}")

    # 3) Rename directories (deepest first)
    dirs = sorted([p for p in ROOT.rglob("*") if p.is_dir()], key=lambda p: len(p.parts), reverse=True)
    for path in dirs:
        new_name = rename_path_component(path.name)
        if new_name:
            path.rename(path.with_name(new_name))
            print(f"dir: {path.name} -> {new_name}")

    # 4) Verify
    remaining = []
    for path in ROOT.rglob("*"):
        if path.is_file() and path.suffix == ".java":
            text = path.read_text(encoding="utf-8")
            if re.search(r"arrangement|arrangment", text, re.I):
                remaining.append(str(path.relative_to(ROOT)))
    if remaining:
        print(f"\nWARNING: {len(remaining)} files still contain arrangement:")
        for r in remaining[:20]:
            print(f"  {r}")
    else:
        print("\nOK: no arrangement references in .java under com/")


if __name__ == "__main__":
    main()
