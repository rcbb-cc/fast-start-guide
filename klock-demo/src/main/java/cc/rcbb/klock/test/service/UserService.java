package cc.rcbb.klock.test.service;

import cc.rcbb.klock.test.entity.User;

import java.util.List;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/6/10
 */
public interface UserService {

    List<User> list(String userName);

    User info(Integer userId);

}
