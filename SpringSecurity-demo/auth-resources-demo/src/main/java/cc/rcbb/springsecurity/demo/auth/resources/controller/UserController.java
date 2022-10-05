package cc.rcbb.springsecurity.demo.auth.resources.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/4
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @PreAuthorize("hasAuthority('user_add')")
    @PostMapping
    public String save() {
        return "save() success";
    }

    @PreAuthorize("hasAuthority('user_remove')")
    @DeleteMapping
    public String remove() {
        return "remove() success";
    }

    @PreAuthorize("hasAuthority('user_update')")
    @PutMapping
    public String update() {
        return "update() success";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public String list() {
        return "list() success";
    }

}
