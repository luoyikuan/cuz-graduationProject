package cn.lyk.app.smartled.entity;

public class User {
    private Long id;
    private String loginId;
    private String loginPwd;

    public User() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (loginId != null ? !loginId.equals(user.loginId) : user.loginId != null) return false;
        return loginPwd != null ? loginPwd.equals(user.loginPwd) : user.loginPwd == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (loginId != null ? loginId.hashCode() : 0);
        result = 31 * result + (loginPwd != null ? loginPwd.hashCode() : 0);
        return result;
    }

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
}
