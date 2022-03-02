package cn.lyk.smartled.entity;

import java.util.HashMap;
import java.util.Map;

public class Msg {
	private int code;
	private String msg;
	private Map<String, Object> data = new HashMap<String, Object>();

	public static Msg msg(int code, String msg) {
		Msg m = new Msg();
		m.code = code;
		m.msg = msg;
		return m;
	}

	public static Msg ok() {
		Msg m = new Msg();
		m.code = 100;
		m.msg = "ok";
		return m;
	}

	public static Msg error() {
		Msg m = new Msg();
		m.code = -1;
		m.msg = "error";
		return m;
	}

	public Msg add(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
