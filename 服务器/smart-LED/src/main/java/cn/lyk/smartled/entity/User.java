package cn.lyk.smartled.entity;

import java.io.Serializable;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class User implements Serializable {
	private static final long serialVersionUID = -3717380903254769377L;
	@TableId(type = IdType.AUTO)
	private Long id;
	private String loginId;
	private String loginPwd;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public User(Long id, String loginId, String loginPwd) {
		super();
		this.id = id;
		this.loginId = loginId;
		this.loginPwd = loginPwd;
	}

	public User() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, loginId, loginPwd);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id) && Objects.equals(loginId, other.loginId)
				&& Objects.equals(loginPwd, other.loginPwd);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", loginId=" + loginId + ", loginPwd=" + loginPwd + "]";
	}

}
