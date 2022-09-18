package top.yudoge.pojos;

import top.yudoge.constants.ResponseCodes;

import java.util.Map;

public class ResponseObjectBuilder {
    protected ResponseObject ro;
    private ResponseObjectBuilder() {
        ro = new ResponseObject();
    }
    private ResponseObjectBuilder(ResponseObject ro) {
        this.ro = ro;
    }

    public DataWithMapResponseObjectBuilder dataWithMap() {
        this.ro = new ResponseObject.DataWithMapResponseObject(ro);
        return new DataWithMapResponseObjectBuilder(ro);
    }

    public class DataWithMapResponseObjectBuilder extends ResponseObjectBuilder {
        protected DataWithMapResponseObjectBuilder(ResponseObject ro) {
            super(ro);
        }

        public DataWithMapResponseObjectBuilder addData(String key, Object value) {
            Map<String, Object> data = (Map<String, Object>)ro.getData();
            data.put(key, value);
            return this;
        }

    }

    public AttachmentWithMapResponseObjectBuilder attachmentWithMap() {
        this.ro = new ResponseObject.AttachmentWithMapResponseObject(ro);
        return new AttachmentWithMapResponseObjectBuilder(ro);
    }


    public class AttachmentWithMapResponseObjectBuilder extends ResponseObjectBuilder {
        protected AttachmentWithMapResponseObjectBuilder(ResponseObject ro) {
            super(ro);
        }
        public AttachmentWithMapResponseObjectBuilder addAttachment(String key, Object value) {
            Map<String, Object> attachment = (Map<String, Object>)ro.getAttachment();
            attachment.put(key, value);
            return this;
        }

    }

    public ResponseObject build() {
        return this.ro;
    }

    /***** 下面是一些构建ResponseObjectBuilder的辅助方法 *****/

    public static ResponseObjectBuilder byCondition(boolean condition, Integer faildCode) {
        if (condition) return success();
        else return faild(faildCode);
    }

    public static ResponseObjectBuilder success() {
        return success(null);
    }
    public static ResponseObjectBuilder success(Object data) {
        ResponseObjectBuilder builder = new ResponseObjectBuilder();
        builder.ro.setCode(ResponseCodes.SUCCESSED);
        builder.ro.setData(data);
        return builder;
    }

    public static ResponseObjectBuilder faild(Integer code) {
        return faild(code, null);
    }

    public static ResponseObjectBuilder faild(Integer code, Object data) {
        ResponseObjectBuilder builder = new ResponseObjectBuilder();
        builder.ro.setCode(code);
        builder.ro.setData(data);
        return builder;
    }

}
