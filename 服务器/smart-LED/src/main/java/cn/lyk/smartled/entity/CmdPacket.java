package cn.lyk.smartled.entity;

public class CmdPacket {
	public static final String CMD_AUTO = "auto";
	public static final String CMD_STATE = "state";
	public static final String CMD_COLOR = "color";

	public static final String BOOL_TRUE = "true";
	public static final String BOOL_FALSE = "false";

	private String cmd;
	private String data;
	
	@Override
	public String toString() {
		if (!verify()) {
			return "{}";
		}

		Map<String, Object> map = new HashMap<>();
		map.put("cmd", this.cmd);

		if (CMD_AUTO.equals(cmd) || CMD_STATE.equals(cmd)) {
			map.put("data", data.equals(BOOL_TRUE));
		} else {
			map.put("data", data);
		}

		ObjectMapper objectMapper = new ObjectMapper();
		String str = null;
		try {
			str = objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			str = "{}";
		}

		return str;
	}

	public boolean verify() {
		if (CMD_AUTO.equals(cmd)) {
			return BOOL_TRUE.equals(data) || BOOL_FALSE.equals(data);
		} else if (CMD_STATE.equals(cmd)) {
			return BOOL_TRUE.equals(data) || BOOL_FALSE.equals(data);
		} else if (CMD_COLOR.equals(cmd)) {
			try {
				Integer.parseInt(data);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}

	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
