package cc.rcbb.mapstruct.demo;

import cc.rcbb.mapstruct.demo.convert.UserConvert;
import cc.rcbb.mapstruct.demo.dto.UserDTO;
import cc.rcbb.mapstruct.demo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
class MapstructDemoApplicationTests {

    @Test
    void toDTO() {
        User user = new User()
                .setId(1L)
                .setName("test")
                .setBirthday(LocalDate.now())
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(1660703967658L);

        UserDTO dto = UserConvert.INSTANCE.convert(user);
        // UserDTO(id=1-id-encode, name=test, birthday=2022-08-17, createTime=2022-08-17 10:40:37, updateTime=1660703967)
        System.out.println(dto);
    }

    @Test
    void toEntity() {
        UserDTO dto = new UserDTO()
                .setId("1-id-encode")
                .setName("test")
                .setBirthday("2022-08-17")
                .setCreateTime("2022-08-17 10:40:37")
                .setUpdateTime(1660703967L);
        // User(id=1, name=test, birthday=2022-08-17, createTime=2022-08-17T10:40:37, updateTime=1660703967000)
        User user = UserConvert.INSTANCE.convert(dto);
        System.out.println(user);
    }

}
