package cn.lyk.smartled.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import cn.lyk.smartled.entity.User;
import cn.lyk.smartled.mapper.UserMapper;

@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return
	 */
	public int register(User user) {
		if (user == null || user.getLoginPwd() == null || user.getLoginId() == null) {
			return -1;
		}
		user.setLoginId(user.getLoginId().trim());
		user.setLoginPwd(user.getLoginPwd().trim());

		if (user.getLoginId().isBlank() || user.getLoginPwd().isBlank()) {
			return -2;
		}

		if (userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getLoginId, user.getLoginId())) > 0) {
			return -3;
		}

		return userMapper.insert(user);
	}

	/**
	 * 用户登录
	 * 
	 * @return
	 */
	public User login(User user) {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>().eq(User::getLoginId, user.getLoginId())
				.eq(User::getLoginPwd, user.getLoginPwd());
		return userMapper.selectOne(queryWrapper);
	}

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 * @return
	 */
	public boolean changePwd(Long userId, String oldPwd, String newPwd) {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>().eq(User::getId, userId)
				.eq(User::getLoginPwd, oldPwd);
		User user = userMapper.selectOne(queryWrapper);
		if (user == null) {
			return false;
		}

		LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<User>().eq(User::getId, userId)
				.set(User::getLoginPwd, newPwd);
		return userMapper.update(null, updateWrapper) > 0;
	}

}
