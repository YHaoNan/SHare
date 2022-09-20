from list_aliyun_file import list, support
import rabbitpy
from mongo import modifyDataStore, connectToMongo
from elasticsearch import sendElasticsearchUpdateRequest

connectToMongo()

with rabbitpy.Connection() as conn:
    with conn.channel() as chan:
        for msg in rabbitpy.consume(queue_name="resource.check.queue", no_ack=True, prefetch=100):
            resource = msg.json()
            if support(resource['url']):
                try:
                    result = list(resource['url'], resource['code'])
                    modifyDataStore(resource['id'], result)
                    sendElasticsearchUpdateRequest(resource['id'], result)
                except Exception as e:
                    print('exception raised when check resource ' + e)
            else:
                print('Unsupport url ' + resource['url'])