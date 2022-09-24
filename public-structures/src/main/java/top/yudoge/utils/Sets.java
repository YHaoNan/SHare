package top.yudoge.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Sets {
    public static <T> Set<T> of(T...ts) {
        Set<T> set = new TreeSet<>();
        for (T t : ts) set.add(t);
        return set;
    }
}
