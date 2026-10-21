# -*- coding: utf-8 -*-
"""
Generate Excel Test Report from TestNG results
Format: Test_Cases_NoiThatBang_<DATE>.xlsx
Sheets: Test Cases | Execution Summary | Traceability
"""

import sys
import os
import re
import xml.etree.ElementTree as ET
from datetime import datetime

# ── Try to import openpyxl ──────────────────────────────────────────────────
try:
    import openpyxl
    from openpyxl.styles import (
        PatternFill, Font, Alignment, Border, Side
    )
    from openpyxl.utils import get_column_letter
except ImportError:
    print("❌ openpyxl not installed. Running: pip install openpyxl ...")
    import subprocess
    subprocess.check_call([sys.executable, "-m", "pip", "install", "openpyxl"])
    import openpyxl
    from openpyxl.styles import PatternFill, Font, Alignment, Border, Side
    from openpyxl.utils import get_column_letter

# ── CONFIG ──────────────────────────────────────────────────────────────────
TESTNG_XML   = r"target/surefire-reports/testng-results.xml"
OUTPUT_DIR   = r"reports"
DATE_STR     = datetime.now().strftime("%Y%m%d")
OUTPUT_FILE  = os.path.join(OUTPUT_DIR, f"Test_Cases_NoiThatBang_{DATE_STR}.xlsx")
PROJECT_NAME = "Website_NoiThat (NoiThatBang - Project 4)"
SUITE_NAME   = "testng-noithatbang.xml / testng-admin.xml"
ENVIRONMENT  = "http://localhost:5173/ (Admin) | http://localhost:3000/ (Storefront)"
TECH         = "Selenium UI / TestNG"

# ── MODULE MAP (class short name → module label) ─────────────────────────────
MODULE_MAP = {
    "BangAdminAuthTest":           "Admin Authentication",
    "BangAdminDashboardTest":      "Admin Dashboard",
    "BangAdminCategoryTest":       "Admin Categories",
    "BangAdminBrandTest":          "Admin Brands",
    "BangAdminProductTest":        "Admin Products",
    "BangAdminProductCRUDTest":    "Admin Products CRUD",
    "BangAdminOrderTest":          "Admin Orders",
    "BangAdminCustomerTest":       "Admin Customers",
    "BangAdminCouponTest":         "Admin Coupons",
    "BangAdminBannerTest":         "Admin Banners",
    "BangAdminReviewTest":         "Admin Reviews",
    "BangAdminContactTest":        "Admin Contacts",
    "BangAdminSettingTest":        "Admin Settings",
    "BangStorefrontAuthDataTest":  "Storefront Authentication",
    "BangStorefrontCatalogSearchTest": "Storefront Catalog & Search",
    "BangStorefrontTest":          "Storefront General",
    "BangStorefrontTestData":      "Storefront Test Data",
    "BangCheckoutTest":            "Storefront Checkout",
    "BangCheckoutDataTest":        "Storefront Checkout Data",
    "BangContactDataTest":         "Storefront Contact",
    "BangCustomerOrdersTest":      "Storefront Customer Orders",
    "BangApiTest":                 "API Tests",
}

# ── RISK MAP ────────────────────────────────────────────────────────────────
RISK_MAP = {
    "Admin Authentication":        "Critical",
    "Admin Dashboard":             "High",
    "Admin Categories":            "High",
    "Admin Brands":                "High",
    "Admin Products":              "High",
    "Admin Products CRUD":         "High",
    "Admin Orders":                "High",
    "Admin Customers":             "Medium",
    "Admin Coupons":               "Medium",
    "Admin Banners":               "Medium",
    "Admin Reviews":               "Medium",
    "Admin Contacts":              "Medium",
    "Admin Settings":              "Medium",
    "Storefront Authentication":   "Critical",
    "Storefront Catalog & Search": "High",
    "Storefront General":          "High",
    "Storefront Test Data":        "Medium",
    "Storefront Checkout":         "Critical",
    "Storefront Checkout Data":    "Critical",
    "Storefront Contact":          "Low",
    "Storefront Customer Orders":  "High",
    "API Tests":                   "High",
}

# ── STYLE HELPERS ────────────────────────────────────────────────────────────
def make_fill(hex_color):
    return PatternFill("solid", fgColor=hex_color)

def make_border():
    thin = Side(style="thin", color="CCCCCC")
    return Border(left=thin, right=thin, top=thin, bottom=thin)

HEADER_FONT  = Font(name="Calibri", bold=True, color="FFFFFF", size=11)
NORMAL_FONT  = Font(name="Calibri", size=10)
HEADER_FILL  = make_fill("2E4057")   # dark navy
PASS_FILL    = make_fill("D9F0E8")   # light green
FAIL_FILL    = make_fill("FCE4EC")   # light red
SKIP_FILL    = make_fill("FFF9C4")   # light yellow
ALT_FILL     = make_fill("F5F7FA")   # alternate row
CENTER_ALIGN = Alignment(horizontal="center", vertical="center", wrap_text=True)
LEFT_ALIGN   = Alignment(horizontal="left",   vertical="center", wrap_text=True)


def apply_header_row(ws, headers, row=1):
    for col_idx, header in enumerate(headers, start=1):
        cell = ws.cell(row=row, column=col_idx, value=header)
        cell.font      = HEADER_FONT
        cell.fill      = HEADER_FILL
        cell.alignment = CENTER_ALIGN
        cell.border    = make_border()


def style_data_cell(cell, status=None, alt_row=False):
    cell.font   = NORMAL_FONT
    cell.border = make_border()
    if status == "PASS":
        cell.fill = PASS_FILL
    elif status in ("FAIL", "FAILURE"):
        cell.fill = FAIL_FILL
    elif status in ("SKIP", "SKIPPED"):
        cell.fill = SKIP_FILL
    elif alt_row:
        cell.fill = ALT_FILL


def set_col_widths(ws, widths):
    for col_idx, width in enumerate(widths, start=1):
        ws.column_dimensions[get_column_letter(col_idx)].width = width


# ── PARSE TESTNG XML ─────────────────────────────────────────────────────────
def parse_testng_results(xml_path):
    if not os.path.exists(xml_path):
        print(f"❌ File not found: {xml_path}")
        sys.exit(1)

    tree = ET.parse(xml_path)
    root = tree.getroot()

    suite_el = root.find("suite")
    suite_name_xml = suite_el.get("name", SUITE_NAME) if suite_el is not None else SUITE_NAME
    started_at     = suite_el.get("started-at", "") if suite_el is not None else ""
    finished_at    = suite_el.get("finished-at", "") if suite_el is not None else ""

    total   = int(root.get("total",   0))
    passed  = int(root.get("passed",  0))
    failed  = int(root.get("failed",  0))
    skipped = int(root.get("skipped", 0))

    records = []
    counter = 1

    for m in root.findall(".//test-method"):
        # skip config methods (@BeforeMethod, @AfterMethod, etc.)
        if m.get("is-config"):
            continue

        name        = m.get("name", "")
        description = m.get("description", name)
        status      = m.get("status", "UNKNOWN")
        started     = m.get("started-at", "")

        # extract class name from signature
        sig = m.get("signature", "")
        cls_match = re.search(r"instance:([^\]@]+)@", sig)
        full_class = cls_match.group(1).strip() if cls_match else ""
        short_class = full_class.split(".")[-1] if full_class else "Unknown"

        module    = MODULE_MAP.get(short_class, short_class)
        risk      = RISK_MAP.get(module, "Medium")

        # failure message
        exc = m.find("exception")
        fail_msg = ""
        if exc is not None:
            msg_el = exc.find("message")
            if msg_el is not None and msg_el.text:
                fail_msg = msg_el.text.strip()

        # actual result cell
        if status == "PASS":
            actual = "PASS - Expected result observed."
        elif status in ("FAIL", "FAILURE"):
            actual = f"FAIL - {fail_msg[:200]}" if fail_msg else "FAIL - Assertion or application defect."
        else:
            actual = f"SKIP - Not executed."

        # priority
        priority = "P1" if risk in ("Critical", "High") else "P2"

        # format started_at
        exec_time = ""
        if started:
            try:
                dt = datetime.strptime(started[:19], "%Y-%m-%dT%H:%M:%S")
                exec_time = dt.strftime("%Y-%m-%d %H:%M:%S")
            except Exception:
                exec_time = started[:19]

        tc_id = f"TC-WNT-{counter:03d}"

        records.append({
            "tc_id":       tc_id,
            "module":      module,
            "risk":        risk,
            "description": description,
            "pre_cond":    "Website_NoiThat Admin (port 5173) and/or Storefront (port 3000) must be running.",
            "steps":       (
                f"1. Navigate to the relevant page/module.\n"
                f"2. Execute: {description}\n"
                f"3. Verify the resulting UI, URL, data, or validation state."
            ),
            "expected":    "Expected UI, URL, data, or validation result is displayed without error.",
            "actual":      actual,
            "status":      status,
            "priority":    priority,
            "test_data":   "Website_NoiThat deterministic fixture data",
            "automatable": "Yes",
            "auto_type":   TECH,
            "tags":        f"{module.lower().replace(' ', '-')},{status.lower()}",
            "auto_class":  short_class,
            "auto_method": name,
            "exec_at":     exec_time,
            "source":      "target/surefire-reports/testng-results.xml",
        })
        counter += 1

    return records, {
        "suite_name":  suite_name_xml,
        "started_at":  started_at,
        "finished_at": finished_at,
        "total":       total,
        "passed":      passed,
        "failed":      failed,
        "skipped":     skipped,
    }


# ── SHEET 1: Test Cases ──────────────────────────────────────────────────────
def build_test_cases_sheet(ws, records):
    headers = [
        "TC ID", "Module", "Risk Level", "Test Title",
        "Pre-Condition", "Test Steps", "Expected Result",
        "Actual Result", "Status", "Priority", "Test Data",
        "Automatable", "Auto Type", "Tags",
        "Automation Class", "Automation Method",
        "Executed At", "Source",
    ]
    apply_header_row(ws, headers)
    ws.row_dimensions[1].height = 30

    for row_idx, rec in enumerate(records, start=2):
        alt = (row_idx % 2 == 0)
        status = rec["status"]

        values = [
            rec["tc_id"], rec["module"], rec["risk"],
            rec["description"], rec["pre_cond"], rec["steps"],
            rec["expected"], rec["actual"], status,
            rec["priority"], rec["test_data"], rec["automatable"],
            rec["auto_type"], rec["tags"], rec["auto_class"],
            rec["auto_method"], rec["exec_at"], rec["source"],
        ]

        for col_idx, val in enumerate(values, start=1):
            cell = ws.cell(row=row_idx, column=col_idx, value=val)
            style_data_cell(cell, status=status, alt_row=alt)
            if col_idx in (5, 6, 7, 8):   # long text columns
                cell.alignment = LEFT_ALIGN
            else:
                cell.alignment = CENTER_ALIGN

        ws.row_dimensions[row_idx].height = 45

    # freeze header
    ws.freeze_panes = "A2"

    set_col_widths(ws, [
        12, 22, 12, 42,   # TC ID, Module, Risk, Title
        35, 40, 35, 45,   # Pre, Steps, Expected, Actual
        10, 8, 30,        # Status, Priority, Test Data
        12, 20, 25,       # Automatable, AutoType, Tags
        30, 30,           # Class, Method
        20, 35,           # ExecAt, Source
    ])

    # auto-filter
    ws.auto_filter.ref = ws.dimensions


# ── SHEET 2: Execution Summary ───────────────────────────────────────────────
def build_summary_sheet(ws, records, meta):
    passed  = sum(1 for r in records if r["status"] == "PASS")
    failed  = sum(1 for r in records if r["status"] in ("FAIL", "FAILURE"))
    skipped = sum(1 for r in records if r["status"] in ("SKIP", "SKIPPED"))
    total   = len(records)

    # Header
    ws.merge_cells("A1:C1")
    title_cell = ws["A1"]
    title_cell.value     = "📊 Execution Summary"
    title_cell.font      = Font(name="Calibri", bold=True, size=14, color="FFFFFF")
    title_cell.fill      = make_fill("2E4057")
    title_cell.alignment = CENTER_ALIGN
    ws.row_dimensions[1].height = 35

    rows = [
        ("Project",        PROJECT_NAME,       None),
        ("Environment",    ENVIRONMENT,         None),
        ("Suite",          SUITE_NAME,          None),
        ("Execution Time",
            f"{meta.get('started_at','N/A')} → {meta.get('finished_at','N/A')}",
            None),
        ("Total Tests",    total,               None),
        ("Passed",         passed,              None),
        ("Failed",         failed,              None),
        ("Skipped",        skipped,             None),
        ("Errors",         0,                   None),
        ("Pass Rate",      None,                "=B6/B5"),   # formula
        (None, None, None),
        ("Status", "Meaning", "Count"),
        ("PASS",  "Expected behavior observed",              passed),
        ("FAIL",  "Assertion or application defect",         failed),
        ("SKIP",  "Not executed",                            skipped),
    ]

    STATUS_FILLS = {"PASS": PASS_FILL, "FAIL": FAIL_FILL, "SKIP": SKIP_FILL}
    LABEL_FILL  = make_fill("EDF2F7")

    for row_idx, (label, value, formula) in enumerate(rows, start=2):
        cell_a = ws.cell(row=row_idx, column=1, value=label)
        cell_b = ws.cell(row=row_idx, column=2, value=value)
        cell_c = ws.cell(row=row_idx, column=3)

        if formula:
            cell_b.value = formula
            cell_b.number_format = "0.00%"

        if label in ("Status", "Meaning", "Count"):
            for c in [cell_a, cell_b, cell_c]:
                c.font = Font(name="Calibri", bold=True, size=10)
                c.fill = LABEL_FILL
        elif label in STATUS_FILLS:
            for c in [cell_a, cell_b, cell_c]:
                c.fill = STATUS_FILLS[label]
                c.font = NORMAL_FONT

        if label == "Count":
            cell_c.value = {"PASS": passed, "FAIL": failed, "SKIP": skipped}.get(value, None)

        for c in [cell_a, cell_b, cell_c]:
            c.border    = make_border()
            c.alignment = CENTER_ALIGN if row_idx >= 13 else LEFT_ALIGN

    set_col_widths(ws, [22, 55, 12])


# ── SHEET 3: Traceability Matrix ─────────────────────────────────────────────
def build_traceability_sheet(ws, records):
    headers = ["Module", "Automation Class", "Test Count", "Passed", "Failed", "Skipped", "Notes"]
    apply_header_row(ws, headers)
    ws.row_dimensions[1].height = 28

    # group by module
    from collections import defaultdict
    groups = defaultdict(lambda: {"class": "N/A", "total": 0, "pass": 0, "fail": 0, "skip": 0})
    for r in records:
        g = groups[r["module"]]
        g["class"] = r["auto_class"]
        g["total"] += 1
        if r["status"] == "PASS":
            g["pass"] += 1
        elif r["status"] in ("FAIL", "FAILURE"):
            g["fail"] += 1
        elif r["status"] in ("SKIP", "SKIPPED"):
            g["skip"] += 1

    for row_idx, (module, g) in enumerate(sorted(groups.items()), start=2):
        alt   = (row_idx % 2 == 0)
        note  = "✅ All passed" if g["fail"] == 0 and g["skip"] == 0 else "⚠️ Review failed cases before release"
        row_fill = PASS_FILL if g["fail"] == 0 else FAIL_FILL

        values = [module, g["class"], g["total"], g["pass"], g["fail"], g["skip"], note]
        for col_idx, val in enumerate(values, start=1):
            cell = ws.cell(row=row_idx, column=col_idx, value=val)
            cell.font      = NORMAL_FONT
            cell.border    = make_border()
            cell.alignment = CENTER_ALIGN if col_idx != 1 else LEFT_ALIGN
            if g["fail"] == 0:
                cell.fill = PASS_FILL if not alt else make_fill("E8F5E9")
            else:
                cell.fill = FAIL_FILL if not alt else make_fill("FFEBEE")

        ws.row_dimensions[row_idx].height = 22

    ws.freeze_panes = "A2"
    set_col_widths(ws, [28, 30, 12, 10, 10, 10, 36])


# ── MAIN ─────────────────────────────────────────────────────────────────────
def main():
    print(f"📂 Parsing: {TESTNG_XML}")
    records, meta = parse_testng_results(TESTNG_XML)
    print(f"✅ Found {len(records)} test methods")

    os.makedirs(OUTPUT_DIR, exist_ok=True)

    wb = openpyxl.Workbook()
    wb.remove(wb.active)   # remove default sheet

    ws_tc  = wb.create_sheet("Test Cases")
    ws_sum = wb.create_sheet("Execution Summary")
    ws_tr  = wb.create_sheet("Traceability")

    ws_tc.sheet_properties.tabColor  = "2E4057"
    ws_sum.sheet_properties.tabColor = "27AE60"
    ws_tr.sheet_properties.tabColor  = "E67E22"

    print("📝 Building Test Cases sheet ...")
    build_test_cases_sheet(ws_tc, records)

    print("📊 Building Execution Summary sheet ...")
    build_summary_sheet(ws_sum, records, meta)

    print("🗺️  Building Traceability sheet ...")
    build_traceability_sheet(ws_tr, records)

    wb.save(OUTPUT_FILE)
    print(f"\n🎉 Excel report saved: {OUTPUT_FILE}")

    # Summary
    passed  = sum(1 for r in records if r["status"] == "PASS")
    failed  = sum(1 for r in records if r["status"] in ("FAIL", "FAILURE"))
    skipped = sum(1 for r in records if r["status"] in ("SKIP", "SKIPPED"))
    print(f"   Total: {len(records)} | ✅ {passed} PASS | ❌ {failed} FAIL | ⏭  {skipped} SKIP")


if __name__ == "__main__":
    main()
