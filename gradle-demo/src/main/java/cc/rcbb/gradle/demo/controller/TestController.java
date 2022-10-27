package cc.rcbb.gradle.demo.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TestController
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/10/26
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping
    public String test() {
        log.info("[测试 ip<{}>]", NetUtil.getLocalhost());
        return DateUtil.now();
    }

}
