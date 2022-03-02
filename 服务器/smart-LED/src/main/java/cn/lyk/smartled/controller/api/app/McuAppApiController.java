package cn.lyk.smartled.controller.api.app;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.lyk.smartled.entity.CmdPacket;
import cn.lyk.smartled.entity.Data;
import cn.lyk.smartled.entity.Mcu;
import cn.lyk.smartled.entity.Msg;
import cn.lyk.smartled.entity.User;
import cn.lyk.smartled.service.McuService;

@RestController
@RequestMapping("/api/app/mcu")
public class McuAppApiController {
	@Autowired
	private McuService mcuService;
	
	@DeleteMapping("/del/{mcuId}")
	public Object delMcu(@PathVariable Long mcuId, HttpSession session) {
		User user = (User) session.getAttribute("app_user");
		return mcuService.delMcuByUserIdAndMcuId(user.getId(), mcuId) ? Msg.ok() : Msg.error();
	}

	@GetMapping("/list")
	public List<Mcu> list(HttpSession session) {
		User user = (User) session.getAttribute("app_user");
		return mcuService.getUserMcuList(user.getId());
	}

	@PostMapping("/add")
	public Object add(@RequestBody Mcu mcu, HttpSession session) {
		User user = (User) session.getAttribute("app_user");
		return mcuService.bind(mcu, user.getId()) ? Msg.ok() : Msg.error();
	}

	@PostMapping("/cmd/{mcuId}")
	public Object cmd(@PathVariable Long mcuId, @RequestBody CmdPacket cmdPacket, HttpSession session) {
		User user = (User) session.getAttribute("app_user");
		mcuService.sendCmd(user.getId(), mcuId, cmdPacket);
		return Msg.ok();
	}

	@GetMapping("/data/{mcuId}/{p}")
	public List<Data> data(@PathVariable Long mcuId, @PathVariable Long p, HttpSession session) {
		User user = (User) session.getAttribute("app_user");
		return mcuService.getDataPage(user.getId(), mcuId, p).getRecords();
	}

	@DeleteMapping("/data/{dataId}")
	public Object delData(@PathVariable Long dataId, HttpSession session) {
		User user = (User) session.getAttribute("app_user");
		return mcuService.delDataByUserIdAndDataId(user.getId(), dataId) ? Msg.ok() : Msg.error();
	}
	

}
