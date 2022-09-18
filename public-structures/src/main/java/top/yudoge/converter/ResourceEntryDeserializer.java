package top.yudoge.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import top.yudoge.pojos.Directory;
import top.yudoge.pojos.File;
import top.yudoge.pojos.ResourceEntry;

import java.io.IOException;

public class ResourceEntryDeserializer extends JsonDeserializer {

    private ResourceEntry explore(TreeNode entryNode) {
        JsonNode node = (JsonNode) entryNode;
        boolean isDirectory = node.get("directory").asBoolean();
        ResourceEntry entry;
        if (isDirectory) {
            entry = new Directory();
            JsonNode childrenNodes = node.get("children");
            for (int i=0;i<childrenNodes.size();i++)
                entry.addChild(explore(childrenNodes.get(i)));
        } else {
            entry = new File();
        }
        entry.setName(node.get("name").asText());
        entry.setSize(node.get("size").asText());
        return entry;
    }
    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return explore(jsonParser.getCodec().readTree(jsonParser));
    }
}
