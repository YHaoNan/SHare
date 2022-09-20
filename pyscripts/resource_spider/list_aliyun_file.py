"""
https://www.aliyundrive.com/s/YgfYRy98DFg
f87d
"""
import re
import requests
import json
from resource_entry import *

PATTERN = r"https://www.aliyundrive.com/s/(\S+)/?"

resource_url = "https://www.aliyundrive.com/s/YgfYRy98DFg"
extract_code = "f87d"



def get_share_token(share_id, extract_code):
    result = requests.post("https://api.aliyundrive.com/v2/share_link/get_share_token", json={
        'share_id': share_id,
        'share_pwd': extract_code
    })

    if result.status_code != 200:
        raise 'get share token faild, resposne code is not 200'

    jo = json.loads(result.text)
    if 'share_token' not in jo:
        raise 'get share token faild, there is no field named `share_token` in result object'

    return jo['share_token']

def listIter(share_id, share_token, parent, parent_file_id = "root"):
    resp = requests.post('https://api.aliyundrive.com/adrive/v3/file/list', json=
        {
            "share_id": share_id,
            "parent_file_id": parent_file_id,
            "limit":100,
            "image_thumbnail_process":"image/resize,w_160/format,jpeg",
            "image_url_process":"image/resize,w_1920/format,jpeg",
            "video_thumbnail_process":"video/snapshot,t_1000,f_jpg,ar_auto,w_300",
            "order_by":"name",
            "order_direction":"DESC"
        }, headers= {
            'x-share-token': share_token
        }
    )
    # 如果请求失败，静默放弃
    if resp.status_code != 200:
        return

    jo = json.loads(resp.text)
    for item in jo['items']:
        entry = None
        if item['type'] == 'file':
            entry = File(item['name'], item['size'])
        elif item['type'] == 'folder':
            entry = Directory(item['name'])
            listIter(share_id, share_token, entry, item['file_id'])
        else:
            # 不知道是啥，放弃
            continue
        parent.addChild(entry)



def support(resource_url):
    match_result = re.match(PATTERN, resource_url)
    return match_result

def list(resource_url, extract_code):
    match_result = re.match(PATTERN, resource_url)
    if not match_result:
        raise 'url not match!'

    share_id = match_result.group(1)
    share_token = get_share_token(share_id, extract_code)

    wrapped_entry = Directory('SHARE')
    listIter(share_id, share_token, wrapped_entry)
    return wrapped_entry

