import sys
import os
import xml.etree.ElementTree as ET

def verify_results(file_path):
    print("=" * 60)
    print("       SUREFIRE RESULTS VERIFIER (CI QUALITY GATE)")
    print("=" * 60)

    if not os.path.exists(file_path):
        print(f"[ERROR] Result file not found at: {file_path}")
        print("Failure: Test execution did not produce surefire results (build/setup crashed).")
        sys.exit(1)

    try:
        tree = ET.parse(file_path)
        root = tree.getroot()
    except Exception as e:
        print(f"[ERROR] Failed to parse XML file: {e}")
        sys.exit(1)

    # In TestNG, root is <testng-results>
    # Attributes: total, passed, failed, skipped
    total_str = root.attrib.get('total')
    passed_str = root.attrib.get('passed')
    failed_str = root.attrib.get('failed')
    skipped_str = root.attrib.get('skipped')

    if total_str is None:
        # Fallback: Count <test-method> tags
        methods = root.findall('.//test-method')
        test_methods = [m for m in methods if m.attrib.get('is-config') != 'true']
        total = len(test_methods)
        passed = len([m for m in test_methods if m.attrib.get('status') == 'PASS'])
        failed = len([m for m in test_methods if m.attrib.get('status') == 'FAIL'])
        skipped = len([m for m in test_methods if m.attrib.get('status') == 'SKIP'])
    else:
        total = int(total_str)
        passed = int(passed_str or 0)
        failed = int(failed_str or 0)
        skipped = int(skipped_str or 0)

    print(f"File Path    : {file_path}")
    print(f"Total Tests  : {total}")
    print(f"Passed       : {passed}")
    print(f"Failed       : {failed}")
    print(f"Skipped      : {skipped}")
    print("-" * 60)

    # Rule 1: No tests executed
    if total == 0:
        print("[GATE FAILED] CRITICAL: 0 tests were executed!")
        print("Reason: No tests matched suite/filters or test discovery failed.")
        sys.exit(1)

    # Rule 2: 100% tests were skipped (False positive / Environment Guard skip)
    if total > 0 and skipped == total:
        print("[GATE FAILED] CRITICAL: 100% of tests were SKIPPED!")
        print(f"Reason: All {total} test cases were skipped (likely target environment unavailable or setup guard failed).")
        sys.exit(1)

    # Rule 3: Any test failed
    if failed > 0:
        print(f"[GATE FAILED] CRITICAL: {failed} test case(s) FAILED!")
        # Print names of failed tests
        failed_methods = root.findall('.//test-method[@status="FAIL"]')
        if failed_methods:
            print("\nList of failed test methods:")
            for idx, m in enumerate(failed_methods, 1):
                class_name = m.attrib.get('name', 'UnknownMethod')
                print(f"  {idx}. {class_name}")
        sys.exit(1)

    # If passed
    print(f"[GATE PASSED] All {passed} test(s) completed successfully without failures or 100% skip.")
    print("=" * 60)
    sys.exit(0)

if __name__ == '__main__':
    target_file = sys.argv[1] if len(sys.argv) > 1 else 'target/surefire-reports/testng-results.xml'
    verify_results(target_file)
