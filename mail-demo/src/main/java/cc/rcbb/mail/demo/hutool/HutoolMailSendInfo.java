package cc.rcbb.mail.demo.hutool;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * MailSendDTO
 * </p>
 *
 * @author rcbb.cc
 * @date 2022/3/3
 */
@Accessors(chain = true)
@Data
public class HutoolMailSendInfo {

	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * SMTP服务器地址
	 */
	private String host;
	/**
	 * 端口
	 */
	private Integer port;
	/**
	 * 显示名称
	 */
	private String personal;
	/**
	 * 邮箱地址
	 */
	private String email;

	/**
	 * logo图标地址
	 */
	private String logoUrl;

	/**
	 * 接收人邮箱（多个值使用 , 隔开）
	 */
	private List<String> toUsers;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;

	/**
	 * 是否需要用户名密码验证
	 */
	private Boolean auth;
	/**
	 * 使用 STARTTLS安全连接，
	 */
	private Boolean starttlsEnable;

}

