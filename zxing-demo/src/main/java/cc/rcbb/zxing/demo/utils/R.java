package cc.rcbb.zxing.demo.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * R
 * </p>
 * 响应信息主体
 *
 * @author rcbb.cc
 * @date 2022/5/14
 */
@Accessors(chain = true)
@Data
public class R<T> implements Serializable {

    private static final long serialVersionUID = 9138627749853861107L;

    /**
     * 返回标记（0：成功；）
     */
    private static final int _0 = 0;
    /**
     * 返回标记（1：成功；）
     */
    private static final int _1 = 1;

    /**
     * 返回标记（0：成功；1：失败；）
     */
    private Integer code;

    private String msg;

    private T data;

    public static <T> R<T> ok() {
        return restResult(_0, null, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(_0, null, data);
    }

    public static <T> R<T> failed() {
        return restResult(_1, null, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(_1, msg, null);
    }

    public static <T> R<T> restResult(int code, String msg, T data) {
        return new R<T>().setCode(code).setData(data).setMsg(msg);
    }
}
