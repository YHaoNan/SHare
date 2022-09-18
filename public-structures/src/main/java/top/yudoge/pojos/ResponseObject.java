package top.yudoge.pojos;

import lombok.Data;

import java.util.HashMap;

@Data
public class ResponseObject {
    protected ResponseObject(){}
    private Integer code;
    private Object data;
    private Object attachment;
    private boolean successed;

    public void setCode(Integer code) {
        this.code = code;
        this.successed = this.code == 200;
    }

    public static class DataWithMapResponseObject extends ResponseObject {
        public DataWithMapResponseObject(ResponseObject object) {
            setCode(object.code);
            setAttachment(object.attachment);
            setData(new HashMap<>());
        }
    }


    public static class AttachmentWithMapResponseObject extends ResponseObject {
        public AttachmentWithMapResponseObject(ResponseObject object) {
            setCode(object.code);
            setData(object.data);
            setAttachment(new HashMap<>());
        }
    }
}
