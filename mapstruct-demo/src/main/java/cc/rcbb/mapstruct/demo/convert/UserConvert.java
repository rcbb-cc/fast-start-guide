package cc.rcbb.mapstruct.demo.convert;

import cc.rcbb.mapstruct.demo.dto.UserDTO;
import cc.rcbb.mapstruct.demo.entity.User;
import cc.rcbb.mapstruct.demo.utils.UserUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * <p>
 * UserConvert
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/8/17
 */
@Mapper(imports = UserUtil.class)
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    @Mappings({
            @Mapping(target = "id", expression = "java(UserUtil.idEncode(user.getId()))"),
            @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "updateTime", expression = "java(UserUtil.second(user.getUpdateTime()))"),
    })
    UserDTO convert(User user);

    @Mappings({
            @Mapping(target = "id", expression = "java(UserUtil.idDecode(dto.getId()))"),
            @Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd"),
            @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss"),
            @Mapping(target = "updateTime", expression = "java(UserUtil.millisecond(dto.getUpdateTime()))"),
    })
    User convert(UserDTO dto);


}
