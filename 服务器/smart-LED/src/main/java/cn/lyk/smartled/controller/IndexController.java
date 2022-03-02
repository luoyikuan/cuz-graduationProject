package cn.lyk.smartled.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {

	/**
	 * 首页
	 * 
	 * @return
	 */
	@GetMapping({ "/", "/index", "/index.html" })
	public ModelAndView index() {
		return new ModelAndView("index");
	}
}