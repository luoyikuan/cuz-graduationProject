package cn.lyk.smartled.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.lyk.smartled.entity.CmdPacket;
import cn.lyk.smartled.entity.Data;
import cn.lyk.smartled.entity.Mcu;
import cn.lyk.smartled.mapper.DataMapper;
import cn.lyk.smartled.mapper.McuMapper;

@Service
public class McuService {
	@Autowired
	private McuMapper mcuMapper;
	@Autowired
	private DataMapper dataMapper;

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	public void sendCmd(Long userId, Long mcuId, CmdPacket cmdPacket) {
		if (cmdPacket == null || !cmdPacket.verify()) {
			return;
		}
		
		Mcu mcu = mcuMapper.selectById(mcuId);
		if (mcu == null || !userId.equals(mcu.getUserId())) {
			return;
		}

		String destination = "mcu.cmd." + mcu.getMac();
		jmsMessagingTemplate.convertAndSend(destination, cmdPacket.toString());
	}

	/**
	 * 获取用户的全部Mcu
	 * 
	 * @return
	 */
	public List<Mcu> getUserMcuList(Long userId) {
		LambdaQueryWrapper<Mcu> queryWrapper = new LambdaQueryWrapper<Mcu>().eq(Mcu::getUserId, userId);
		List<Mcu> list = mcuMapper.selectList(queryWrapper);
		return list;
	}

	/**
	 * 用户绑定一台设备
	 * 
	 * @param mcu
	 * @param userId
	 * @return
	 */
	public boolean bind(Mcu mcu, Long userId) {
		LambdaQueryWrapper<Mcu> queryWrapper = new LambdaQueryWrapper<Mcu>().eq(Mcu::getMac, mcu.getMac())
				.eq(Mcu::getToken, mcu.getToken());

		if (mcuMapper.selectCount(queryWrapper) == 0) {
			return false;
		}

		LambdaUpdateWrapper<Mcu> updateWrapper = new LambdaUpdateWrapper<Mcu>().eq(Mcu::getMac, mcu.getMac());
		mcu.setUserId(userId);
		return mcuMapper.update(mcu, updateWrapper) != 0;
	}

	/**
	 * 用户移除一台设备
	 * 
	 * @param mcuId
	 * @param userId
	 * @return
	 */
	public boolean remove(Long mcuId, Long userId) {
		Mcu mcu = mcuMapper.selectById(mcuId);
		if (mcu == null)
			return false;

		if (!userId.equals(mcu.getUserId()))
			return false;

		LambdaUpdateWrapper<Mcu> updateWrapper = new LambdaUpdateWrapper<Mcu>().set(Mcu::getUserId, null).eq(Mcu::getId,
				mcuId);
		return mcuMapper.update(null, updateWrapper) > 0;
	}

	public Page<Data> getDataPage(Long userId, Long mcuId, Long p) {
		Page<Data> pd = new Page<Data>().setCurrent(p).setSize(1000);

		Mcu mcu = mcuMapper.selectById(mcuId);
		if (mcu == null)
			return pd;

		if (!userId.equals(mcu.getUserId()))
			return pd;

		LambdaQueryWrapper<Data> queryWrapper = new LambdaQueryWrapper<Data>().eq(Data::getMcuId, mcuId)
				.orderByDesc(Data::getCreateTime);

		Page<Data> page = dataMapper.selectPage(pd, queryWrapper);
		return page;
	}

	public boolean delDataByUserIdAndDataId(Long userId, Long dataId) {
		Data data = dataMapper.selectById(dataId);
		if (data == null) {
			return false;
		}

		Mcu mcu = mcuMapper.selectById(data.getMcuId());
		if (mcu == null) {
			return false;
		}

		if (!userId.equals(mcu.getUserId())) {
			return false;
		}

		return dataMapper.deleteById(data) > 0;
	}

	public boolean delMcuByUserIdAndMcuId(Long userId, Long mcuId) {
		Mcu mcu = mcuMapper.selectById(mcuId);
		if (mcu == null) {
			return false;
		}

		if (!userId.equals(mcu.getUserId())) {
			return false;
		}

		LambdaUpdateWrapper<Mcu> updateWrapper = new LambdaUpdateWrapper<Mcu>().set(Mcu::getUserId, null).eq(Mcu::getId,
				mcuId);
		return mcuMapper.update(null, updateWrapper) > 0;
	}

}
