# -*- coding: utf-8 -*-
import openpyxl
import sys
from collections import Counter

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

f1 = 'reports/Test_Cases_NoiThatBang_20261015.xlsx'
f2 = 'reports/Test_Cases_NoiThat2_20261023.xlsx'

wb1 = openpyxl.load_workbook(f1)
wb2 = openpyxl.load_workbook(f2)

ws1 = wb1['Test Cases']
ws2 = wb2['Test Cases']

headers1 = [cell.value for cell in ws1[1]]
headers2 = [cell.value for cell in ws2[1]]

def get_records(ws, headers):
    records = []
    for row in ws.iter_rows(min_row=2, values_only=True):
        if not any(row):
            continue
        rec = dict(zip(headers, row))
        records.append(rec)
    return records

rec1 = get_records(ws1, headers1)
rec2 = get_records(ws2, headers2)

print(f"=== SO SANH SO LUONG TEST CASES ===")
print(f"Tuần trước (15/10/2026): {len(rec1)} test cases")
print(f"Hôm nay    (23/10/2026): {len(rec2)} test cases")
print(f"Chênh lệch: {len(rec2) - len(rec1):+d} test cases\n")

# Status comparison
c1 = Counter(r.get('Status') for r in rec1)
c2 = Counter(r.get('Status') for r in rec2)

print("=== SO SANH TRANG THAI EXECUTION ===")
print(f"15/10/2026: PASS={c1.get('PASS', 0)}, FAIL={c1.get('FAIL', 0)}, SKIP={c1.get('SKIP', 0)}")
print(f"23/10/2026: PASS={c2.get('PASS', 0)}, FAIL={c2.get('FAIL', 0)}, SKIP={c2.get('SKIP', 0)}")

# Module breakdown
mod1 = Counter(r.get('Module') for r in rec1)
mod2 = Counter(r.get('Module') for r in rec2)

all_modules = sorted(set(list(mod1.keys()) + list(mod2.keys())))
print("\n=== SO SANH THEO MODULE ===")
print(f"{'Module':<45} | {'15/10':<6} | {'23/10':<6} | {'Diff':<6}")
print("-" * 70)
for m in all_modules:
    cnt1 = mod1.get(m, 0)
    cnt2 = mod2.get(m, 0)
    diff = cnt2 - cnt1
    diff_str = f"{diff:+d}" if diff != 0 else "="
    print(f"{str(m):<45} | {cnt1:<6} | {cnt2:<6} | {diff_str:<6}")

# Traceability comparison
ws_tr1 = wb1['Traceability']
ws_tr2 = wb2['Traceability']

print("\n=== TRACEABILITY 15/10 ===")
for row in ws_tr1.iter_rows(min_row=2, values_only=True):
    if any(row):
        print(f"  {row[0]}: Total={row[2]}, Pass={row[3]}, Fail={row[4]}, Skip={row[5]}, Note={row[6]}")

print("\n=== TRACEABILITY 23/10 ===")
for row in ws_tr2.iter_rows(min_row=2, values_only=True):
    if any(row):
        print(f"  {row[0]}: Total={row[2]}, Pass={row[3]}, Fail={row[4]}, Skip={row[5]}, Note={row[6]}")

# Compare specific test methods / titles
titles1 = set((r.get('Automation Class'), r.get('Automation Method')) for r in rec1)
titles2 = set((r.get('Automation Class'), r.get('Automation Method')) for r in rec2)

new_in_2 = titles2 - titles1
removed_in_2 = titles1 - titles2

print(f"\n=== CAC TEST CASES MOI THU HUONG TRONG TUAN NAY (+{len(new_in_2)}) ===")
for cls, mtd in sorted(new_in_2):
    print(f"  + [{cls}] {mtd}")

print(f"\n=== CAC TEST CASES GIAO LUU / THAY THE (-{len(removed_in_2)}) ===")
for cls, mtd in sorted(removed_in_2):
    print(f"  - [{cls}] {mtd}")
