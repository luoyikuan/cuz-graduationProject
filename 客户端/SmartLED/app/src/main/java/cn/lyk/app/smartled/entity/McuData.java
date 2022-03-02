package cn.lyk.app.smartled.entity;

public class McuData {
    private String light;
    private Boolean auto;
    private Boolean state;
    private Integer color;

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
