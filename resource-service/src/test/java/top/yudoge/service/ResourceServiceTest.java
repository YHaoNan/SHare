package top.yudoge.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.yudoge.constants.ResourceState;
import top.yudoge.converter.ResourceEntryToStringConverter;
import top.yudoge.pojos.Directory;
import top.yudoge.pojos.File;
import top.yudoge.pojos.Resource;
import top.yudoge.pojos.UserSnap;
import top.yudoge.service.ResourceService;

import java.util.Arrays;
import java.util.Date;

@SpringBootTest
public class ResourceServiceTest {
    @Autowired
    private ResourceService service;

    @Test
    void testSave() {
        Resource resource = new Resource();
        resource.setUrl("https://pan.baidu.com/s/11Cl9l3XMDTIhdTni9xDRlw");
        resource.setCode("ex57");
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
        resource.setDirectory(new Directory(
                "Analog Obession", "487MB", Arrays.asList(
                        new File("Analog Obession.7z", "487MB"),
                        new Directory("更多资源", "12KB", Arrays.asList(
                                new File("更多链接.html", "12KB"),
                                new File("解压密码.txt", "24B")
                        )),
                        new File("README.md", "51KB")
                )
        ));
        service.publish(1l, resource);
    }
}
