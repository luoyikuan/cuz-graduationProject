package cn.lyk.smartled.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.lyk.smartled.entity.Data;
import cn.lyk.smartled.entity.Msg;
import cn.lyk.smartled.entity.User;
import cn.lyk.smartled.service.DataService;

@RestController
@RequestMapping("/data")
public class DataController {
	private static final String PATH = "data/";
	@Autowired
	private DataService dataService;

	/**
	 * 分页显示mcu的数据
	 * 
	 * @param id
	 * @param p
	 * @param session
	 * @return
	 */
	@GetMapping("/list/{id}-{p}")
	public ModelAndView list(@PathVariable Long id, @PathVariable Long p, HttpSession session) {
		ModelAndView mv = new ModelAndView(PATH + "list");
		User user = (User) session.getAttribute("user");
		Page<Data> dataPage = dataService.getDataPageByUserIdAndMcuId(user.getId(), id, p);
		mv.addObject("dataPage", dataPage);
		mv.addObject("mcuId", id);
		return mv;
	}

	/**
	 * 删除一条数据
	 * 
	 * @param id      数据ID
	 * @param session
	 * @return
	 */
	@PostMapping("/del")
	public Object del(long id, HttpSession session) {
		User user = (User) session.getAttribute("user");
		return dataService.delByUserIdAndDataId(user.getId(), id) ? Msg.ok() : Msg.error();
	}
}
