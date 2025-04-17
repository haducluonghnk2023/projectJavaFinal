package ra.edu.business.model.admin;

import java.time.LocalDateTime;

public class Admin {
    private int id;
    private String username;
    private String password;
    private LocalDateTime lastLogin;

    public Admin() {

    }

    public Admin(int id, String username, String password, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastLogin = lastLogin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
