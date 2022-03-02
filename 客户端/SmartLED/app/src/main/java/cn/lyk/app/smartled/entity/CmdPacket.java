package cn.lyk.app.smartled.entity;

public class CmdPacket {
    public static final String CMD_AUTO = "auto";
    public static final String CMD_STATE = "state";
    public static final String CMD_COLOR = "color";

    public static final String BOOL_TRUE = "true";
    public static final String BOOL_FALSE = "false";

    private String cmd;
    private String data;

    public static CmdPacket auto(boolean auto) {
        CmdPacket cmd = new CmdPacket();
        cmd.setCmd(CMD_AUTO);
        cmd.setData(auto ? BOOL_TRUE : BOOL_FALSE);
        return cmd;
    }

    public static CmdPacket state(boolean state) {
        CmdPacket cmd = new CmdPacket();
        cmd.setCmd(CMD_STATE);
        cmd.setData(state ? BOOL_TRUE : BOOL_FALSE);
        return cmd;
    }

    public static CmdPacket color(int color) {
        CmdPacket cmd = new CmdPacket();
        cmd.setCmd(CMD_COLOR);
        cmd.setData(Integer.toString(color));
        return cmd;
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
