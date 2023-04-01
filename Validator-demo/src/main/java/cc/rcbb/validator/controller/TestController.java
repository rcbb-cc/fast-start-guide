package cc.rcbb.validator.controller;

import cc.rcbb.validator.config.AddGroup;
import cc.rcbb.validator.config.sequence.ValidateSequence;
import cc.rcbb.validator.dto.TestDTO1;
import cc.rcbb.validator.dto.TestDTO3;
import cc.rcbb.validator.dto.TestDTO4;
import cc.rcbb.validator.dto.TestDTO5;
import cc.rcbb.validator.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>TestController</p>
 *
 * @author rcbb.cc
 * @version 1.0.0
 * @date 2020/11/18
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

    /**
     * 1.基础使用，开启校验功能 @Valid
     * @param dto 提交的内容
     * @return
     * 参数未按要求填写，没有配置统一异常处理，会被 DefaultHandlerExceptionResolver 捕捉到，postman 请求的结果为 400。
     */
    @PostMapping("/test1")
    public R test1(@Valid @RequestBody TestDTO1 dto) {
        log.info("[TestDTO1 <{}>]", dto);
        return R.ok();
    }

    /**
     * 2.基础使用，使用自定义message，给校验的 bean 后紧跟一个 BindingResult，就可以获取到校验的结果
     * @param dto 提交的内容
     * @param result 接收错误
     * @return
     * 参数未按要求填写，会将所有错误返回
     * {
     *     "code": 1,
     *     "msg": null,
     *     "data": [
     *         {
     *             "codes": [
     *                 "NotNull.testDTO1.status",
     *                 "NotNull.status",
     *                 "NotNull.java.lang.Integer",
     *                 "NotNull"
     *             ],
     *             "arguments": [
     *                 {
     *                     "codes": [
     *                         "testDTO1.status",
     *                         "status"
     *                     ],
     *                     "arguments": null,
     *                     "defaultMessage": "status",
     *                     "code": "status"
     *                 }
     *             ],
     *             "defaultMessage": "不能为null",
     *             "objectName": "testDTO1",
     *             "field": "status",
     *             "rejectedValue": null,
     *             "bindingFailure": false,
     *             "code": "NotNull"
     *         }
     *     ]
     * }
     */
    @PostMapping("/test2")
    public R test2(@Valid @RequestBody TestDTO1 dto, BindingResult result) {
        log.info("[TestDTO1 <{}>]", dto);
        log.error("[BindingResult <{}>]", result);
        if (result != null && result.getErrorCount() > 0) {
            return R.failed(result.getAllErrors());
        }
        return R.ok();
    }


    /**
     * 3.分组校验
     *
     * 使用一个实体接收提交内容时，新增和更新操作的参数校验标准不同时如何解决？
     *      例：新增操作不需要传入 id，更新操作必须传入 id。
     *
     */
    @PostMapping("/test3")
    public R test3(@Validated(AddGroup.class) @RequestBody TestDTO3 dto) {
        log.info("[TestDTO3 <{}>]", dto);
        return R.ok();
    }

    /**
     * form 表单提交方式
     * @param dto
     * @return
     */
    @PostMapping("/test4")
    public R test4(@Validated(AddGroup.class) TestDTO3 dto) {
        log.info("[TestDTO3 <{}>]", dto);
        return R.ok();
    }

    /**
     * 测试校验顺序
     * @param dto
     * @return
     */
    @PostMapping("/test5")
    public R test5(@Validated(ValidateSequence.class) @RequestBody TestDTO4 dto) {
        log.info("[TestDTO4 <{}>]", dto);
        return R.ok();
    }

    /**
     *  测试自定义校验注解
     * @param dto
     * @return
     */
    @PostMapping("/test6")
    public R test5(@Valid @RequestBody TestDTO5 dto) {
        log.info("[TestDTO5 <{}>]", dto);
        return R.ok();
    }
}
