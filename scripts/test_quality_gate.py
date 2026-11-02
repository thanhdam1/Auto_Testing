import subprocess
import os
import tempfile

def run_gate(xml_content=None, file_path=None):
    if file_path is None:
        with tempfile.NamedTemporaryFile('w', delete=False, suffix='.xml', encoding='utf-8') as f:
            f.write(xml_content)
            temp_path = f.name
    else:
        temp_path = file_path

    try:
        proc = subprocess.run(
            ['python', 'scripts/verify_surefire_results.py', temp_path],
            capture_output=True,
            text=True
        )
        return proc.returncode, proc.stdout + proc.stderr
    finally:
        if file_path is None and os.path.exists(temp_path):
            os.remove(temp_path)

def test_all():
    print("Testing Quality Gate scenarios...")
    
    # 1. Missing file
    code, out = run_gate(file_path="non_existent_file.xml")
    assert code == 1, f"Expected 1, got {code}"
    assert "Result file not found" in out
    print("PASS: Scenario 1 - Missing file triggers FAIL (exit 1)")

    # 2. Total = 0
    xml_zero = """<?xml version="1.0" encoding="UTF-8"?>
    <testng-results total="0" passed="0" failed="0" skipped="0">
    </testng-results>"""
    code, out = run_gate(xml_content=xml_zero)
    assert code == 1, f"Expected 1, got {code}"
    assert "0 tests were executed" in out
    print("PASS: Scenario 2 - Zero tests executed triggers FAIL (exit 1)")

    # 3. All Skipped (total > 0 and skipped == total)
    xml_all_skip = """<?xml version="1.0" encoding="UTF-8"?>
    <testng-results total="15" passed="0" failed="0" skipped="15">
    </testng-results>"""
    code, out = run_gate(xml_content=xml_all_skip)
    assert code == 1, f"Expected 1, got {code}"
    assert "100% of tests were SKIPPED" in out
    print("PASS: Scenario 3 - All tests skipped triggers FAIL (exit 1)")

    # 4. Failed tests (total = 10, passed = 9, failed = 1)
    xml_fail = """<?xml version="1.0" encoding="UTF-8"?>
    <testng-results total="10" passed="9" failed="1" skipped="0">
      <suite name="Suite">
        <test name="Test">
          <class name="com.ecommerce.tests.LoginTest">
            <test-method status="FAIL" name="testLoginFailure">
            </test-method>
          </class>
        </test>
      </suite>
    </testng-results>"""
    code, out = run_gate(xml_content=xml_fail)
    assert code == 1, f"Expected 1, got {code}"
    assert "1 test case(s) FAILED" in out
    assert "testLoginFailure" in out
    print("PASS: Scenario 4 - Test failure triggers FAIL (exit 1)")

    # 5. Success run (total = 82, passed = 82, failed = 0, skipped = 0)
    xml_success = """<?xml version="1.0" encoding="UTF-8"?>
    <testng-results total="82" passed="82" failed="0" skipped="0">
    </testng-results>"""
    code, out = run_gate(xml_content=xml_success)
    assert code == 0, f"Expected 0, got {code}"
    assert "GATE PASSED" in out
    print("PASS: Scenario 5 - Valid green run PASSES (exit 0)")

    print("\nALL 5 SCENARIO TESTS VERIFIED SUCCESSFULLY!")

if __name__ == '__main__':
    test_all()
