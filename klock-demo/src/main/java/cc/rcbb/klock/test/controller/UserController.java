package cc.rcbb.klock.test.controller;

import cc.rcbb.klock.test.entity.User;
import cc.rcbb.klock.test.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * TestController
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/6/10
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> list(@RequestParam String userName) {
        log.info("[<{}> 进UserController.list()方法]", userName);
        return userService.list(userName);
    }


    @GetMapping("/{userId}")
    public User info(@PathVariable Integer userId) {
        log.info("[<{}> 进UserController.info()方法]", userId);
        return userService.info(userId);
    }
}
