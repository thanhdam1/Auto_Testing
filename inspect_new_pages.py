# -*- coding: utf-8 -*-
import re
import sys

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

def extract_elements(filepath):
    print('=== FILE:', filepath, '===')
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    ids = re.findall(r'id=["\']([^"\']+)["\']', content)
    buttons = re.findall(r'<button[^>]*id=["\']([^"\']+)["\'][^>]*>(.*?)</button>', content, re.DOTALL)
    inputs = re.findall(r'<input[^>]*id=["\']([^"\']+)["\']', content)
    selects = re.findall(r'<select[^>]*id=["\']([^"\']+)["\']', content)
    modals = [i for i in ids if 'modal' in i.lower()]
    kpis = [i for i in ids if 'kpi' in i.lower()]
    tabs = [i for i in ids if 'tab' in i.lower()]
    
    print('MODALS:', set(modals))
    print('KPIS:', set(kpis))
    print('TABS:', set(tabs))
    print('SELECTS:', set(selects))
    print('INPUTS:', set(inputs))
    print('KEY BUTTONS:')
    for b_id, b_text in buttons[:25]:
        clean_text = ' '.join(re.sub(r'<[^>]+>', '', b_text).split())
        print(f'  #{b_id} -> {clean_text}')

extract_elements('C:/Users/PC/OneDrive/Desktop/TTTN_Web_Ban_Noi_That/frontend/src/pages/admin/nhap-hang.astro')
print('\n' + '='*50 + '\n')
extract_elements('C:/Users/PC/OneDrive/Desktop/TTTN_Web_Ban_Noi_That/frontend/src/pages/admin/so-quy.astro')
