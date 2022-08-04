package cc.rcbb.klock.test.service;

import cc.rcbb.klock.test.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * UserServiceImpl
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/6/10
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Klock
    @Override
    public List<User> list(String userName) {
        log.info("[<{}> 进入UserService.list() 成功获取锁]", userName);

        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {}

        List<User> list = new ArrayList<>();
        list.add(new User().setId(1).setName("Jack").setAge(18));
        list.add(new User().setId(2).setName("Tom").setAge(20));
        log.info("[<{}> UserService.list() 执行完毕，释放锁]", userName);
        return list;
    }

    @Klock(keys = {"#userId"})
    @Override
    public User info(Integer userId) {
        log.info("[<{}> 进入UserService.info() 成功获取锁]", userId);

        try {
            Thread.sleep(10 * 1000);
        } catch (Exception e) {}

        log.info("[<{}> UserService.info() 执行完毕，释放锁]", userId);

        if (userId == 1) {
            return new User().setId(1).setName("Jack").setAge(18);
        } else if (userId == 2) {
            return new User().setId(2).setName("Tom").setAge(20);
        }
        return null;
    }
}
