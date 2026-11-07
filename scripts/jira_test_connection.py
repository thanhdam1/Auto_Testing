import os
import sys
import base64
import urllib.request
import json

try:
    sys.stdout.reconfigure(encoding='utf-8')
except:
    pass

def load_env(env_path='.env'):
    env_vars = {}
    if os.path.exists(env_path):
        with open(env_path, 'r', encoding='utf-8') as f:
            for line in f:
                line = line.strip()
                if line and not line.startswith('#') and '=' in line:
                    k, v = line.split('=', 1)
                    env_vars[k.strip()] = v.strip()
    return env_vars

def test_jira_connection():
    env = load_env()
    base_url = env.get('JIRA_BASE_URL', '').rstrip('/')
    email = env.get('JIRA_EMAIL', '')
    token = env.get('JIRA_API_TOKEN', '')

    if not base_url or 'your-domain' in base_url:
        print("[ERROR] JIRA_BASE_URL chua duoc cau hinh hop le.")
        return False
    if not email or 'your-email' in email:
        print("[ERROR] JIRA_EMAIL chua duoc cau hinh hop le.")
        return False
    if not token:
        print("[ERROR] JIRA_API_TOKEN chua duoc cung cap.")
        return False

    auth_str = f"{email}:{token}"
    auth_b64 = base64.b64encode(auth_str.encode('utf-8')).decode('utf-8')

    headers = {
        "Authorization": f"Basic {auth_b64}",
        "Accept": "application/json",
        "Content-Type": "application/json"
    }

    test_url = f"{base_url}/rest/api/3/myself"
    print(f"Connecting to: {test_url} ...")

    req = urllib.request.Request(test_url, headers=headers)
    try:
        with urllib.request.urlopen(req, timeout=15) as resp:
            if resp.status == 200:
                data = json.loads(resp.read().decode('utf-8'))
                display_name = data.get('displayName', 'Unknown')
                email_resp = data.get('emailAddress', email)
                account_id = data.get('accountId', '')
                print(f"[SUCCESS] Ket noi Jira thanh cong!")
                print(f" - User: {display_name}")
                print(f" - Email: {email_resp}")
                print(f" - Account ID: {account_id}")

                # Query accessible projects
                proj_url = f"{base_url}/rest/api/3/project"
                proj_req = urllib.request.Request(proj_url, headers=headers)
                try:
                    with urllib.request.urlopen(proj_req, timeout=15) as p_resp:
                        if p_resp.status == 200:
                            projects = json.loads(p_resp.read().decode('utf-8'))
                            print(f"\n[INFO] Tim thay {len(projects)} du an tren Jira:")
                            for p in projects:
                                print(f"   * [{p.get('key')}] {p.get('name')} (ID: {p.get('id')})")
                except Exception as pe:
                    print(f"Khong the liet ke projects: {pe}")
                return True
    except urllib.error.HTTPError as e:
        print(f"[FAILED] HTTP Error {e.code}: {e.reason}")
        try:
            err_body = e.read().decode('utf-8')
            print(f"Detail: {err_body}")
        except:
            pass
        return False
    except Exception as e:
        print(f"[FAILED] Error: {e}")
        return False

if __name__ == '__main__':
    test_jira_connection()
