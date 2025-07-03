package cc.rcbb.test.demo.contoller;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * TestController
 * </p>
 *
 * @author rcbb.cc
 * @date 2025/7/3
 */
@Slf4j
@RestController
@RequestMapping
public class TestController {


    @PostMapping("/rpc/devices/{deviceId}")
    public Dict rpcPostTest(@PathVariable String deviceId, @RequestBody JSONObject jsonObject) {
        log.info("[访问成功 deviceId<{}> jsonObject<{}>]", deviceId, jsonObject);
        return new Dict().set("key", deviceId);
    }

}
