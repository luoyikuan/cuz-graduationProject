package cn.lyk.smartled.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

public class Data implements Serializable {
	private static final long serialVersionUID = 7835474996002953774L;
	@TableId(type = IdType.AUTO)
	private Long id;
	private String value;
	private String ip;
	private Timestamp createTime;
	private Long mcuId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Long getMcuId() {
		return mcuId;
	}

	public void setMcuId(Long mcuId) {
		this.mcuId = mcuId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createTime, id, ip, mcuId, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Data other = (Data) obj;
		return Objects.equals(createTime, other.createTime) && Objects.equals(id, other.id)
				&& Objects.equals(ip, other.ip) && Objects.equals(mcuId, other.mcuId)
				&& Objects.equals(value, other.value);
	}

	@Override
	public String toString() {
		return "Data [id=" + id + ", value=" + value + ", ip=" + ip + ", createTime=" + createTime + ", mcuId=" + mcuId
				+ "]";
	}

	public Data(Long id, String value, String ip, Timestamp createTime, Long mcuId) {
		super();
		this.id = id;
		this.value = value;
		this.ip = ip;
		this.createTime = createTime;
		this.mcuId = mcuId;
	}

	public Data() {
		super();
	}

}
