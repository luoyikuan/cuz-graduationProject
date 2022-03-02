package cn.lyk.smartled.service.api;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import cn.lyk.smartled.entity.Data;
import cn.lyk.smartled.entity.Mcu;
import cn.lyk.smartled.mapper.DataMapper;
import cn.lyk.smartled.mapper.McuMapper;

@Service
public class McuApiService {
	@Autowired
	private McuMapper mcuMapper;
	@Autowired
	private DataMapper dataMapper;
	
	

	/**
	 * 注册设备
	 * 
	 * @param mcu
	 * @return
	 */
	public boolean register(Mcu mcu) {
		LambdaQueryWrapper<Mcu> queryWrapper = new LambdaQueryWrapper<Mcu>().eq(Mcu::getMac, mcu.getMac());
		Mcu dbMcu = mcuMapper.selectOne(queryWrapper);
		if (dbMcu == null) {
			// 增加设备
			mcu.setId(null);
			mcu.setUserId(null);
			mcu.setCreateTime(null);
			mcu.setLastTime(null);
			return mcuMapper.insert(mcu) == 1;
		} else {// 设备已经注册过了,更新最后在线时间
			LambdaUpdateWrapper<Mcu> updateWrapper = new LambdaUpdateWrapper<Mcu>()
					.eq(Mcu::getMac, mcu.getMac())
					.eq(Mcu::getToken, mcu.getToken());
			
			mcu.setId(null);
			mcu.setUserId(null);
			mcu.setMac(null);
			mcu.setToken(null);
			mcu.setCreateTime(null);
			mcu.setLastTime(new Timestamp(System.currentTimeMillis()));
			
			return mcuMapper.update(mcu, updateWrapper) == 1;
		}
	}

	/**
	 * 记录设备上报的数据 并 更新设备最后在线时间
	 * 
	 * @param mac
	 * @param token
	 * @param ip
	 * @param value
	 * @return
	 */
	@Transactional
	public boolean reportData(String mac, String token, String ip, String value) {
		if (mac == null || mac.isBlank() || token == null || token.isBlank() || value == null || value.isBlank()) {
			return false;
		}
		LambdaQueryWrapper<Mcu> queryWrapper = new LambdaQueryWrapper<Mcu>().eq(Mcu::getMac, mac).eq(Mcu::getToken,
				token);
		Mcu dbMcu = mcuMapper.selectOne(queryWrapper);
		if (dbMcu == null) {
			return false;
		}
		
		Data data = new Data();
		data.setIp(ip);
		data.setMcuId(dbMcu.getId());
		data.setValue(value);
		
		dbMcu.setUserId(null);
		dbMcu.setMac(null);
		dbMcu.setToken(null);
		dbMcu.setCreateTime(null);
		dbMcu.setLastTime(new Timestamp(System.currentTimeMillis()));
		
		return mcuMapper.updateById(dbMcu) > 0 && dataMapper.insert(data) > 0;
	}
}
