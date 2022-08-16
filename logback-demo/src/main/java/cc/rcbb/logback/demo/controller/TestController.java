package cc.rcbb.logback.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * TestController
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/8/16
 */
@Slf4j
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test(@RequestParam("level") String level, @RequestParam("content") String content) {
        if ("debug".equals(level)) {
            log.debug("[debug日志 内容<{}>]", content);
        }
        if ("info".equals(level)) {
            log.info("[info日志 内容<{}>]", content);
        }
        if ("warn".equals(level)) {
            log.warn("[warn日志 内容<{}>]", content);
        }
        if ("error".equals(level)) {
            try {
                int i = 1 / Integer.valueOf(content);
                log.error("[error日志 内容<{}>]", content);
            } catch (Exception e) {
                log.error("[error日志 内容<{}> message<{}>]", content, e.getMessage(), e);
            }
        }
        return "SUCCESS";
    }
}
