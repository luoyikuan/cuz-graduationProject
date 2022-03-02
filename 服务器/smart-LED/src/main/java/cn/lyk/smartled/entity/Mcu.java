package cn.lyk.smartled.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class Mcu implements Serializable {
	private static final long serialVersionUID = -1956517346564982311L;
	@TableId(type = IdType.AUTO)
	private Long id;
	private String mac;
	private String token;
	private Timestamp createTime;
	private Timestamp lastTime;
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastTime() {
		return lastTime;
	}

	public void setLastTime(Timestamp lastTime) {
		this.lastTime = lastTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createTime, id, lastTime, mac, token, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mcu other = (Mcu) obj;
		return Objects.equals(createTime, other.createTime) && Objects.equals(id, other.id)
				&& Objects.equals(lastTime, other.lastTime) && Objects.equals(mac, other.mac)
				&& Objects.equals(token, other.token) && Objects.equals(userId, other.userId);
	}

	@Override
	public String toString() {
		return "Mcu [id=" + id + ", mac=" + mac + ", token=" + token + ", createTime=" + createTime + ", lastTime="
				+ lastTime + ", userId=" + userId + "]";
	}

	public Mcu(Long id, String mac, String token, Timestamp createTime, Timestamp lastTime, Long userId) {
		super();
		this.id = id;
		this.mac = mac;
		this.token = token;
		this.createTime = createTime;
		this.lastTime = lastTime;
		this.userId = userId;
	}

	public Mcu() {
		super();
	}

}
