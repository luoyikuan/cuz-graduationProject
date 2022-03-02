package cn.lyk.smartled.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.lyk.smartled.entity.Mcu;
import cn.lyk.smartled.entity.Msg;
import cn.lyk.smartled.service.api.McuApiService;

@RestController
@RequestMapping("/api/mcu")
public class McuApiController {
	@Autowired
	private McuApiService mcuApiService;

	/**
	 * 单片机注册的API
	 * 
	 * @param mcu
	 * @return
	 */
	@PostMapping("/register")
	public Object register(@RequestBody Mcu mcu) {
		return mcuApiService.register(mcu) ? Msg.ok() : Msg.error();
	}

	/**
	 * 设备上报数据API
	 * 上报的格式：
	 * {
	 * 		"mac":"xxx",
	 * 		"token":"xxx",
	 * 		"value":"xxx"
	 * }
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@PostMapping("/data")
	public Object reportData(@RequestBody Map<String, String> map, HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		String mac = map.get("mac");
		String token = map.get("token");
		String value = map.get("value");

		return mcuApiService.reportData(mac, token, ip, value) ? Msg.ok() : Msg.error();
	}

}
