package cc.rcbb.validator.util;

import cc.rcbb.validator.constant.CommonConstant;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>R</p>
 * 响应信息主体
 *
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/10/26
 */
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 返回标记（0：成功标记；1：失败标记；）
     */
    @Getter
    @Setter
    private int code;

    /**
     * 返回信息
     */
    @Getter
    @Setter
    private String msg;

    /**
     * 数据
     */
    @Getter
    @Setter
    private T data;

    public static <T> R<T> ok() {
        return restResult(null, CommonConstant.SUCCESS, null);
    }

    public static <T> R<T> ok(T data) {
        return restResult(data, CommonConstant.SUCCESS, null);
    }

    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, CommonConstant.SUCCESS, msg);
    }

    public static <T> R<T> failed() {
        return restResult(null, CommonConstant.FAIL, null);
    }

    public static <T> R<T> failed(String msg) {
        return restResult(null, CommonConstant.FAIL, msg);
    }

    public static <T> R<T> failed(T data) {
        return restResult(data, CommonConstant.FAIL, null);
    }

    public static <T> R<T> failed(T data, String msg) {
        return restResult(data, CommonConstant.FAIL, msg);
    }


    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> result = new R<>();
        result.setCode(code);
        result.setData(data);
        result.setMsg(msg);
        return result;
    }
}
