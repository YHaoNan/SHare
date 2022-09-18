package top.yudoge.converter;

import org.springframework.core.convert.converter.Converter;
import top.yudoge.pojos.ResourceEntry;

public class ResourceEntryToStringConverter implements Converter<ResourceEntry, String> {
    public static final char LEVEL_DISCRIPTOR = '^';
    public static final String FILE_DISCRIPTOR = "\\F";
    public static final String DIRECTORY_DISCRIPTOR = "\\D";

    /**
     * 转换规则：前置的`^`个数代表层级数，然后是类型描述，\D代表是文件夹，\F代表是文件，最后是文件大小
     *
     * 比如:
     *  ^ \D Analog Obession [75MB]
     *  ^^ \F Analog Obession.7z [75MB]
     *  ^^ \F README.md [1KB]
     *  ^^ \D About
     *  ^^^ \D WHAT
     *  ^^^^ \D THE
     *  ^^^^^ \D FUCK
     *  ^^^ \F 解压密码.txt [51B]
     *  ^^^ \F 更多链接.html [12KB]
     *
     * AO,
     *
     *  需要在ES配置中将\D、\F、#设置为停止词
     *  约束
     *      1. 只有在标有\D的行的下一行才有可能出现层级符号数比上面多1的情况
     *      2. 下面一行的层级符号数只可能比上面一行多1
     *      3. 下面一行的层级符号数可能比上面一行少好多
     *      4. 层级符号数最少为1
     */

    private char[] nTimes(char c, int n) {
        char ret[] = new char[n];
        for (int i=0; i<n; i++) ret[i] = c;
        return ret;
    }

    private void explore(ResourceEntry entry, StringBuffer sb, int level) {

        sb.append(nTimes(LEVEL_DISCRIPTOR, level));
        sb.append(" ");
        sb.append(entry.isDirectory() ? DIRECTORY_DISCRIPTOR : FILE_DISCRIPTOR);
        sb.append(" ");
        sb.append(entry.getName());
        sb.append(" ");
        sb.append("[");
        sb.append(entry.size());
        sb.append("]\n");

        if (!entry.isDirectory()) return;

        for (ResourceEntry sub : entry.getChildren()) {
            explore(sub, sb, level + 1);
        }
    }

    @Override
    public String convert(ResourceEntry resourceEntry) {
        StringBuffer sb = new StringBuffer();
        explore(resourceEntry, sb, 1);
        return sb.toString();
    }

}
