import requests

LEVEL_DISCRIPTOR = "^"
DIRECTORY_DISCRIPTOR = "\\D"
FILE_DISCRIPTOR = "\\F"

def resource_entry_to_string(entry, W, level):
    W['dir'] += LEVEL_DISCRIPTOR * level
    W['dir'] += " "
    if entry['directory']:
        W['dir'] += DIRECTORY_DISCRIPTOR
    else: W['dir'] += FILE_DISCRIPTOR
    W['dir'] += " "
    W['dir'] += entry['name']
    W['dir'] += " "
    W['dir'] += "["
    W['dir'] += str(entry['size'])
    W['dir'] += "]\n"

    if entry['directory']:
        for item in entry['children']:
            resource_entry_to_string(item, W, level + 1)


def sendElasticsearchUpdateRequest(resource_id, directory):
    W = {'dir': ""}
    resource_entry_to_string(directory, W, 1)
    print("[+] [ES] Update " + str(resource_id) + " => " + W['dir'])

    json = {
        "doc": {
            "directory": W['dir']
        }
    }

    resp = requests.post("http://localhost:9201/resource/_update/" + resource_id, json = json)

    if resp.status_code == 200:
        print('[+] [ES] OK')
    else:
        print('[-] [ES] Faild, status_code is => ' + str(resp.status_code))
        print(resp.text)
