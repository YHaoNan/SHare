package top.yudoge.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yudoge.constants.ResourceState;
import top.yudoge.pojos.Directory;
import top.yudoge.pojos.File;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.UserSnap;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class ResourceRepositoryTest {
    @Autowired
    private ResourceRepository repository;

    @Test
    void testSave() {
        Resource resource = new Resource();
        resource.setUrl("https://pan.baidu.com/s/11Cl9l3XMDTIhdTni9xDRlw");
        resource.setCode("ex57");
        resource.setTitle("Analog Obession");
        resource.setBio("Analog Obsession是由一个个人开发者——Rıdvan Küçük——撑起的数字音频处理插件品牌，曾经是付费插件，现在完全免费。\n" +
                "\n" +
                "Rıdvan Küçük是一个硬件设计师，也是一个音乐家，他提供了数十款品质极高的免费插件，大多是模拟硬件设备的插件，其中也不乏一些创意插件。\n" +
                "\n" +
                "可惜，他并没有提供一个打包下载的渠道，所有人必须逐个下载插件，而且插件所托管的存储平台在国外，对于有些用户来说，下载插件可能是一个问题。\n" +
                "\n" +
                "现在，我把它们全部的插件打包成一个压缩包，你可以一次性下载它们！");
        resource.setPrice(10l);
        resource.setCheckPending(false);
        resource.setLastCheckTime(new Date());
        resource.setSaleCount(7);
        resource.setRefundCount(0);
        UserSnap userSnap = new UserSnap();
        userSnap.setEmail("1355265122@qq.com");
        userSnap.setNick("Yudoge");
        resource.setPublisher(userSnap);
        resource.setState(ResourceState.OK);
        resource.setPublisherId(1l);

        Directory directory = new Directory("Analog Obession", "489MB", Arrays.asList(
                new Directory("Analog Obession", "489MB", Arrays.asList(
                        new File("Analog Obession.7z", "489MB")
                )),
                new File("README.md", "302B"),
                new File(".DB_Store", "10B")
        ));
        resource.setDirectory(directory);
        repository.save(resource);
        JsonMapper mapper = new JsonMapper();
        try {
            System.out.println(mapper.writeValueAsString(resource));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        Optional<Resource> resource = repository.findById("6326c722c3d02c40211985c7");
        Assertions.assertTrue(resource.isPresent());
        System.out.println(resource.get());
    }
}
