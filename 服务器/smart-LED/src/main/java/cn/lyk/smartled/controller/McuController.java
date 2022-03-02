package cn.lyk.smartled.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.lyk.smartled.entity.Mcu;
import cn.lyk.smartled.entity.Msg;
import cn.lyk.smartled.entity.User;
import cn.lyk.smartled.service.McuService;

@RestController
@RequestMapping("/mcu")
public class McuController {
	private static final String PATH = "mcu/";
	@Autowired
	private McuService mcuService;

	/**
	 * 一个用户的所有单片机
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping("/list")
	public ModelAndView list(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView(PATH + "list");
		User user = (User) session.getAttribute("user");
		List<Mcu> mcuList = mcuService.getUserMcuList(user.getId());
		modelAndView.addObject("mcuList", mcuList);
		return modelAndView;
	}

	/**
	 * 用户绑定设备
	 * 
	 * @param mcu
	 * @param session
	 * @return
	 */
	@PostMapping("/add")
	public Object add(Mcu mcu, HttpSession session) {
		User user = (User) session.getAttribute("user");
		return mcuService.bind(mcu, user.getId()) ? Msg.ok() : Msg.error();
	}

	/**
	 * 解绑用户的一台设备
	 * 
	 * @return
	 */
	@PostMapping("/remove")
	public Object remove(Long id, HttpSession session) {
		User user = (User) session.getAttribute("user");
		return mcuService.remove(id, user.getId()) ? Msg.ok() : Msg.error();
	}

}
