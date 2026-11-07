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

class JiraClient:
    def __init__(self, env_path='.env'):
        self.env = load_env(env_path)
        self.base_url = self.env.get('JIRA_BASE_URL', '').rstrip('/')
        self.email = self.env.get('JIRA_EMAIL', '')
        self.token = self.env.get('JIRA_API_TOKEN', '')
        
        auth_str = f"{self.email}:{self.token}"
        self.auth_b64 = base64.b64encode(auth_str.encode('utf-8')).decode('utf-8')
        self.headers = {
            "Authorization": f"Basic {self.auth_b64}",
            "Accept": "application/json",
            "Content-Type": "application/json"
        }

    def get_myself(self):
        url = f"{self.base_url}/rest/api/3/myself"
        req = urllib.request.Request(url, headers=self.headers)
        with urllib.request.urlopen(req, timeout=15) as resp:
            return json.loads(resp.read().decode('utf-8'))

    def get_projects(self):
        url = f"{self.base_url}/rest/api/3/project"
        req = urllib.request.Request(url, headers=self.headers)
        with urllib.request.urlopen(req, timeout=15) as resp:
            return json.loads(resp.read().decode('utf-8'))

    def get_project_details(self, project_key):
        url = f"{self.base_url}/rest/api/3/project/{project_key}"
        req = urllib.request.Request(url, headers=self.headers)
        with urllib.request.urlopen(req, timeout=15) as resp:
            return json.loads(resp.read().decode('utf-8'))

    def create_issue(self, project_key, summary, description_text, issue_type_name="Bug", priority_name="Medium"):
        url = f"{self.base_url}/rest/api/3/issue"
        
        # Format description as Atlassian Document Format (ADF)
        adf_description = {
            "version": 1,
            "type": "doc",
            "content": [
                {
                    "type": "paragraph",
                    "content": [
                        {
                            "type": "text",
                            "text": description_text
                        }
                    ]
                }
            ]
        }

        payload = {
            "fields": {
                "project": {
                    "key": project_key
                },
                "summary": summary,
                "description": adf_description,
                "issuetype": {
                    "name": issue_type_name
                }
            }
        }

        data_bytes = json.dumps(payload).encode('utf-8')
        req = urllib.request.Request(url, data=data_bytes, headers=self.headers, method='POST')
        
        with urllib.request.urlopen(req, timeout=15) as resp:
            return json.loads(resp.read().decode('utf-8'))

    def get_issue(self, issue_key):
        url = f"{self.base_url}/rest/api/3/issue/{issue_key}"
        req = urllib.request.Request(url, headers=self.headers)
        with urllib.request.urlopen(req, timeout=15) as resp:
            return json.loads(resp.read().decode('utf-8'))

if __name__ == '__main__':
    client = JiraClient()
    user = client.get_myself()
    print("Logged in as:", user.get('displayName'))
    projects = client.get_projects()
    for p in projects:
        pkey = p.get('key')
        print(f"\nProject: [{pkey}] {p.get('name')}")
        try:
            details = client.get_project_details(pkey)
            print("Issue Types:")
            for it in details.get('issueTypes', []):
                print(f"  - {it.get('name')} (ID: {it.get('id')})")
        except Exception as e:
            print("  Could not fetch issue types:", e)
