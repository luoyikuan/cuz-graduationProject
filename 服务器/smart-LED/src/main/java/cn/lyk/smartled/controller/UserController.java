package cn.lyk.smartled.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.lyk.smartled.entity.Msg;
import cn.lyk.smartled.entity.User;
import cn.lyk.smartled.service.UserService;

@RestController
@RequestMapping(path = "/user")
public class UserController {
	@Autowired
	private UserService userService;

	/**
	 * 用户退出
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping("/logout")
	public ModelAndView logout(HttpSession session) {
		session.removeAttribute("user");
		return new ModelAndView("redirect:/");
	}

	/**
	 * 用户注册
	 * 
	 * @return
	 */
	@PostMapping("/register")
	public Object register(User user, HttpSession session) {
		Msg msg = null;

		int result = userService.register(user);
		if (result == 1) {
			msg = Msg.ok();
			session.setAttribute("user", user);
		} else {
			msg = Msg.error();
		}

		return msg;
	}

	/**
	 * 用户登录
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@PostMapping("/login")
	public Object login(User user, HttpSession session) {
		Msg msg = null;
		User dbUser = userService.login(user);
		if (dbUser == null) {
			msg = Msg.error();
		} else {
			msg = Msg.ok();
			session.setAttribute("user", dbUser);
		}
		return msg;
	}

}
