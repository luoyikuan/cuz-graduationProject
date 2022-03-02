package cn.lyk.smartled.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.lyk.smartled.entity.Data;
import cn.lyk.smartled.entity.Mcu;
import cn.lyk.smartled.mapper.DataMapper;
import cn.lyk.smartled.mapper.McuMapper;

@Service
public class DataService {
	@Autowired
	private DataMapper dataMapper;
	@Autowired
	private McuMapper mcuMapper;

	public Page<Data> getDataPageByUserIdAndMcuId(Long userId, Long mcuId, Long p) {
		Mcu mcu = mcuMapper.selectById(mcuId);
		if (mcu == null || mcu.getUserId() == null || !mcu.getUserId().equals(userId)) {
			return null;
		}

		LambdaQueryWrapper<Data> queryWrapper = new LambdaQueryWrapper<Data>().eq(Data::getMcuId, mcuId)
				.orderByDesc(Data::getCreateTime);

		Page<Data> page = dataMapper.selectPage(new Page<Data>().setCurrent(p), queryWrapper);
		return page;
	}

	/**
	 * 根据用户ID和数据ID删除数据
	 * 
	 * @param userId
	 * @param dataId
	 * @return
	 */
	public boolean delByUserIdAndDataId(Long userId, Long dataId) {
		Data data = dataMapper.selectById(dataId);
		if (data == null)
			return false;

		Mcu mcu = mcuMapper.selectById(data.getMcuId());
		if (mcu == null)
			return false;

		if (!userId.equals(mcu.getUserId()))
			return false;

		return dataMapper.deleteById(data) > 0;
	}

}
