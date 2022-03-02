package cn.lyk.smartled.controller.api.app;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.lyk.smartled.entity.Msg;
import cn.lyk.smartled.entity.User;
import cn.lyk.smartled.service.UserService;

@RestController
@RequestMapping("/api/app/user")
public class UserAppApiController {
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public Object login(@RequestBody User user, HttpSession session) {
		User dbUser = userService.login(user);
		if (dbUser != null && dbUser.getId() != null) {
			session.setAttribute("app_user", dbUser);
			return Msg.ok();
		} else {
			return Msg.error();
		}
	}

	@PostMapping("/register")
	public Object register(@RequestBody User user, HttpSession session) {
		if (userService.register(user) > 0) {
			session.setAttribute("app_user", user);
			return Msg.ok();
		} else {
			return Msg.error();
		}
	}

	@DeleteMapping("/logout")
	public Object register(HttpSession session) {
		session.removeAttribute("app_user");
		return Msg.ok();
	}

	@PutMapping("/changePwd")
	public Object changePwd(String oldPwd, String newPwd, HttpSession session) {
		User user = (User) session.getAttribute("app_user");
		if (userService.changePwd(user.getId(), oldPwd, newPwd)) {
			user.setLoginPwd(newPwd);
			return Msg.ok();
		} else {
			return Msg.error();
		}
	}

}
