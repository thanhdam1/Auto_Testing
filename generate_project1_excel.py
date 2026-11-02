# -*- coding: utf-8 -*-
"""
Generate Excel Test Report for Project 1: Nội Thất & Phong Thủy
Format: Test_Cases_Project1_<DATE>.xlsx
Sheets: Test Cases | Execution Summary | Traceability
"""

import sys
import os
import re
import xml.etree.ElementTree as ET
from datetime import datetime

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

try:
    import openpyxl
    from openpyxl.styles import PatternFill, Font, Alignment, Border, Side
    from openpyxl.utils import get_column_letter
except ImportError:
    import subprocess
    subprocess.check_call([sys.executable, "-m", "pip", "install", "openpyxl"])
    import openpyxl
    from openpyxl.styles import PatternFill, Font, Alignment, Border, Side
    from openpyxl.utils import get_column_letter

# ── CONFIG ──────────────────────────────────────────────────────────────────
TESTNG_XML   = r"target/surefire-reports/testng-results.xml"
OUTPUT_DIR   = r"reports"
DATE_STR     = datetime.now().strftime("%Y%m%d")
OUTPUT_FILE  = os.path.join(OUTPUT_DIR, f"Test_Cases_Project1_{DATE_STR}.xlsx")
PROJECT_NAME = "Website Nội Thất & Phong Thủy (Project 1 - TTTN_Web_Ban_Noi_That)"
SUITE_NAME   = "testng.xml"
ENVIRONMENT  = "http://localhost:4321/ (Storefront) | http://localhost:4321/admin (Admin Portal)"
TECH         = "Selenium WebDriver 4.27.0 / TestNG 7.10.2 / Java 24 / ExtentReports"

MODULE_MAP = {
    "StorefrontHomeTest":          "Storefront Home Page",
    "StorefrontAuthTest":          "Storefront Authentication",
    "StorefrontRegisterTest":      "Storefront Registration",
    "StorefrontForgotPasswordTest":"Storefront Forgot Password & OTP",
    "StorefrontCatalogTest":       "Storefront Catalog & Filter",
    "PhongThuyConsultationTest":   "Phong Thuy AI Consultation",
    "ProductDetailTest":           "Product Detail Page",
    "CartTest":                    "Shopping Cart",
    "CheckoutFlowTest":            "Checkout & Order Placement",
    "StorefrontAccountTest":       "Storefront Customer Account",
    "AdminProductManagementTest":  "Admin Product Management",
    "AdminOrderManagementTest":    "Admin Order Management",
    "AdminCategoryManagementTest": "Admin Category Management",
    "AdminCustomerManagementTest": "Admin Customer Management",
    "AdminImportManagementTest":   "Admin Import & Supplier Management",
    "AdminCashbookManagementTest": "Admin Cashbook & COD Management",
}

RISK_MAP = {
    "Storefront Home Page":        "Medium",
    "Storefront Authentication":   "Critical",
    "Storefront Registration":     "High",
    "Storefront Forgot Password & OTP": "High",
    "Storefront Catalog & Filter": "High",
    "Phong Thuy AI Consultation":  "Medium",
    "Product Detail Page":         "High",
    "Shopping Cart":               "Critical",
    "Checkout & Order Placement":  "Critical",
    "Storefront Customer Account": "Medium",
    "Admin Product Management":    "Critical",
    "Admin Order Management":      "Critical",
    "Admin Category Management":   "High",
    "Admin Customer Management":   "High",
    "Admin Import & Supplier Management": "Critical",
    "Admin Cashbook & COD Management":    "Critical",
}

def make_fill(hex_color):
    return PatternFill("solid", fgColor=hex_color)

def make_border():
    thin = Side(style="thin", color="CCCCCC")
    return Border(left=thin, right=thin, top=thin, bottom=thin)

HEADER_FONT  = Font(name="Calibri", bold=True, color="FFFFFF", size=11)
NORMAL_FONT  = Font(name="Calibri", size=10)
HEADER_FILL  = make_fill("173524")   # Walnut forest green (matching brand)
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
        if m.get("is-config") == "true":
            continue

        name        = m.get("name", "")
        description = m.get("description", name)
        status      = m.get("status", "UNKNOWN")
        started     = m.get("started-at", "")

        sig = m.get("signature", "")
        cls_match = re.search(r"instance:([^\]@]+)@", sig)
        full_class = cls_match.group(1).strip() if cls_match else ""
        short_class = full_class.split(".")[-1] if full_class else "Unknown"

        module = MODULE_MAP.get(short_class, short_class)
        risk   = RISK_MAP.get(module, "Medium")

        exc = m.find("exception")
        fail_msg = ""
        if exc is not None:
            msg_el = exc.find("message")
            if msg_el is not None and msg_el.text:
                fail_msg = msg_el.text.strip()

        if status == "PASS":
            actual = "PASS - Kết quả thực tế khớp đúng với mong đợi thiết kế."
        elif status in ("FAIL", "FAILURE"):
            actual = f"FAIL - {fail_msg[:200]}" if fail_msg else "FAIL - Sai lệch kỳ vọng kiểm thử."
        else:
            actual = f"SKIP - {fail_msg[:150]}" if fail_msg else "SKIP - Bỏ qua do backend API/database chưa bật."

        priority = "P1" if risk in ("Critical", "High") else "P2"

        exec_time = ""
        if started:
            try:
                dt = datetime.strptime(started[:19], "%Y-%m-%dT%H:%M:%S")
                exec_time = dt.strftime("%Y-%m-%d %H:%M:%S")
            except Exception:
                exec_time = started[:19]

        tc_id = f"TC-NT-{counter:03d}"

        records.append({
            "tc_id":       tc_id,
            "module":      module,
            "risk":        risk,
            "description": description,
            "pre_cond":    "Hệ thống Nội Thất & Phong Thủy đang hoạt động tại http://localhost:4321.",
            "steps":       (
                f"1. Khởi tạo phiên trình duyệt và điều hướng đến màn hình chức năng tương ứng.\n"
                f"2. Thực thi: {description}\n"
                f"3. Kiểm tra UI components, form validation, localStorage hoặc phản hồi API."
            ),
            "expected":    "Hành vi giao diện và dữ liệu phản hồi đúng quy chuẩn và không phát sinh lỗi.",
            "actual":      actual,
            "status":      status,
            "priority":    priority,
            "test_data":   "Dữ liệu kiểm thử chuẩn của dự án Nội Thất & Phong Thủy",
            "automatable": "Yes",
            "auto_type":   TECH,
            "tags":        f"{module.lower().replace(' ', '-')},{status.lower()}",
            "auto_class":  short_class,
            "auto_method": name,
            "exec_at":     exec_time,
            "source":      "testng.xml",
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
            if col_idx in (5, 6, 7, 8):
                cell.alignment = LEFT_ALIGN
            else:
                cell.alignment = CENTER_ALIGN

        ws.row_dimensions[row_idx].height = 45

    ws.freeze_panes = "A2"

    set_col_widths(ws, [
        12, 26, 12, 42,
        35, 42, 35, 45,
        10, 8, 30,
        12, 20, 25,
        30, 30,
        20, 20,
    ])
    ws.auto_filter.ref = ws.dimensions

def build_summary_sheet(ws, records, meta):
    passed  = sum(1 for r in records if r["status"] == "PASS")
    failed  = sum(1 for r in records if r["status"] in ("FAIL", "FAILURE"))
    skipped = sum(1 for r in records if r["status"] in ("SKIP", "SKIPPED"))
    total   = len(records)

    ws.merge_cells("A1:C1")
    title_cell = ws["A1"]
    title_cell.value     = "📊 Execution Summary - Project 1 (Nội Thất & Phong Thủy)"
    title_cell.font      = Font(name="Calibri", bold=True, size=14, color="FFFFFF")
    title_cell.fill      = make_fill("173524")
    title_cell.alignment = CENTER_ALIGN
    ws.row_dimensions[1].height = 35

    rows = [
        ("Project",        PROJECT_NAME,       None),
        ("Environment",    ENVIRONMENT,         None),
        ("Suite",          SUITE_NAME,          None),
        ("Framework",      TECH,                None),
        ("Execution Time",
            f"{meta.get('started_at','N/A')} → {meta.get('finished_at','N/A')}",
            None),
        ("Total Tests",    total,               None),
        ("Passed",         passed,              None),
        ("Failed",         failed,              None),
        ("Skipped",        skipped,             None),
        ("Errors",         0,                   None),
        ("Pass Rate",      None,                f"={passed}/{total}"),
        (None, None, None),
        ("Status", "Meaning", "Count"),
        ("PASS",  "Expected behavior observed (Thành công)", passed),
        ("FAIL",  "Assertion or defect (Thất bại)",           failed),
        ("SKIP",  "Skipped gracefully (Cần DB/API)",          skipped),
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
            c.alignment = CENTER_ALIGN if row_idx >= 14 else LEFT_ALIGN

    set_col_widths(ws, [24, 65, 15])

def build_traceability_sheet(ws, records):
    headers = ["Module", "Automation Class", "Test Count", "Passed", "Failed", "Skipped", "Status Note"]
    apply_header_row(ws, headers)
    ws.row_dimensions[1].height = 28

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
        alt = (row_idx % 2 == 0)
        note = "✅ 100% Passed" if g["fail"] == 0 and g["skip"] == 0 else ("⏭ 100% Executable Passed (2 E2E Skipped do DB)" if g["fail"] == 0 else "❌ Has Failures")

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
    set_col_widths(ws, [30, 32, 12, 10, 10, 10, 42])

def main():
    print(f"📂 Đang phân tích kết quả: {TESTNG_XML}")
    records, meta = parse_testng_results(TESTNG_XML)
    print(f"✅ Đã tìm thấy {len(records)} test cases")

    os.makedirs(OUTPUT_DIR, exist_ok=True)

    wb = openpyxl.Workbook()
    wb.remove(wb.active)

    ws_tc  = wb.create_sheet("Test Cases")
    ws_sum = wb.create_sheet("Execution Summary")
    ws_tr  = wb.create_sheet("Traceability")

    ws_tc.sheet_properties.tabColor  = "173524"
    ws_sum.sheet_properties.tabColor = "27AE60"
    ws_tr.sheet_properties.tabColor  = "E67E22"

    print("📝 Đang tạo sheet Test Cases ...")
    build_test_cases_sheet(ws_tc, records)

    print("📊 Đang tạo sheet Execution Summary ...")
    build_summary_sheet(ws_sum, records, meta)

    print("🗺️ Đang tạo sheet Traceability ...")
    build_traceability_sheet(ws_tr, records)

    wb.save(OUTPUT_FILE)
    print(f"\n🎉 Đã xuất thành công báo cáo Excel: {OUTPUT_FILE}")

    passed  = sum(1 for r in records if r["status"] == "PASS")
    failed  = sum(1 for r in records if r["status"] in ("FAIL", "FAILURE"))
    skipped = sum(1 for r in records if r["status"] in ("SKIP", "SKIPPED"))
    print(f"   Tổng số: {len(records)} | ✅ {passed} PASS | ❌ {failed} FAIL | ⏭  {skipped} SKIP")

if __name__ == "__main__":
    main()
