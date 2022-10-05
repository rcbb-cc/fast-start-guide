package cc.rcbb.mapstruct.demo.utils;

import lombok.experimental.UtilityClass;

/**
 * <p>
 * UserUtil
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/8/17
 */
@UtilityClass
public class UserUtil {

    public String idEncode(Long id) {
        return id + "-id-encode";
    }

    public Long idDecode(String id) {
        return 1L;
    }

    public Long second(Long millisecond) {
        return millisecond / 1000;
    }

    public Long millisecond(Long second) {
        return second * 1000;
    }
}
