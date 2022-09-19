package top.yudoge.pojos;

import com.sun.istack.internal.NotNull;
import lombok.Data;

import java.util.Date;

/**
 * 代表用户搜索传入的数据
 *
 * keyword不能为空，其它全部可以为空，为空则代表不限制
 */
@Data
public class ResourceSearch {
    public static class NetdiskType {
        public static final Integer BAIDUPAN = 1;
        public static final Integer ALIPAN = 2;
    }

    // 搜索关键词
    @NotNull
    private String keyword;

    // 发布日期起止时间范围
    private Date fromDay;
    private Date toDay;

    // 资源大小下界和上界
    private Long sizeLowerBound;
    private Long sizeUpperBound;

    // 网盘类型，1=>百度，2=>阿里
    private Integer netdiskType;

    // 查询分页，默认第0页
    private Integer offset = 0;
    // 每页个数，默认10个
    private Integer limit = 10;
    public ResourceSearch() {}
}
