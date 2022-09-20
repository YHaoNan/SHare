class ResourceEntry(dict):
    def __init__(self, name, size, directory, _class):
        self['name'] = name
        self['size'] = size
        self['directory'] = directory
        self['_class'] = _class

class File(ResourceEntry):
    def __init__(self, name, size):
        ResourceEntry.__init__(self, name, size, False, 'top.yudoge.pojos.File')

class Directory(ResourceEntry):
    def __init__(self, name):
        ResourceEntry.__init__(self, name, 0, True, 'top.yudoge.pojos.Directory')
        self['children'] = []

    def addChild(self, entry):
        self['children'].append(entry)
        self['size'] += entry['size']