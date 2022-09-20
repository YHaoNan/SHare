from pymongo import MongoClient
import bson
import pymongo

MONGOSTR = "mongodb://root:root@localhost:27017/?authenticationDatabase=admin"

client = None
database = None

def connectToMongo():
    global client, database
    client = MongoClient('localhost:27017', username="root", password="root")

    database = client.share_resource


def modifyDataStore(resource_id, directory):
    print("[+] [Mongo] Update " + str(resource_id) + " => " + str(directory))
    res = database.resource.update_one({'_id': bson.ObjectId(resource_id)}, {'$set': {'directory': directory}})
    if res.modified_count == 0:
        print('[-] [Mongo] update error, update 0 item')
    else: print("[+] [Mongo] OK")