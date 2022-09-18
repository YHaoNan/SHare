package top.yudoge.converter;

import org.springframework.core.convert.converter.Converter;
import top.yudoge.pojos.Directory;
import top.yudoge.pojos.File;
import top.yudoge.pojos.ResourceEntry;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToResourceEntryConverter implements Converter<String, ResourceEntry> {
    private static final Pattern MATCH_PAT = Pattern.compile("^(\\^+) (\\\\D|\\\\F) (.*?) \\[(.*?)\\]$");

    class Res {
        int level;
        ResourceEntry entry;
        Res(int level, ResourceEntry entry) {
            this.level = level;
            this.entry = entry;
        }
    }

    @Override
    public ResourceEntry convert(String s) {
        ResourceEntry root = null;
        LineNumberReader reader = new LineNumberReader(new StringReader(s));
        Stack<Res> dirs = new Stack<>();

        int lastLevel = 0;
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                Matcher matcher = MATCH_PAT.matcher(line);
                // 忽略不匹配的行
                if (!matcher.matches()) continue;

                // 从行中提取必要数据
                int thisLevel = matcher.group(1).length();
                boolean isDirectory = matcher.group(2).equals("\\D");
                String name = matcher.group(3);
                String size = matcher.group(4);

                // 创建当前Entry
                ResourceEntry entry = isDirectory ? new Directory(name, size) : new File(name, size);

                // 如果当前文件层级小于等于上一个，那么它将被归到它的父文件夹中
                if (thisLevel <= lastLevel) {
                    while (dirs.peek().level >= thisLevel) {
                        dirs.pop();
                    }
                    dirs.peek().entry.addChild(entry);
                } else {
                    // 如果当前文件夹层级大于上一个，根据定义，有两种情况
                    //  1. 上一个层级为0，当前文件是第一个文件，把它设置成root
                    //  2. 当前文件是上一个的子文件，并且上一个是一个文件夹
                    if (lastLevel == 0)
                        root = entry;
                    else
                        dirs.peek().entry.addChild(entry);
                }

                if (isDirectory) {
                    dirs.push(new Res(thisLevel, entry));
                }
                lastLevel = thisLevel;
            }
        } catch (IOException e) { /* This will never reached! */}
        return root;
    }
}
