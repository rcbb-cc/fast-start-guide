package cc.rcbb.captcha.demo.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * CaptchaController
 * </p>
 *
 * @author rcbb.cc
 * @date 2021/7/16
 */
@RestController
@RequestMapping("/rcbb/captcha")
public class RcbbCaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/blockPuzzle")
    public ResponseModel blockPuzzle() {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaType("blockPuzzle");
        return captchaService.get(vo);
    }

}
