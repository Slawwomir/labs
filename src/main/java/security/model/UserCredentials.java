package security.model;

import lombok.Data;
import security.service.CryptUtils;

import javax.security.enterprise.credential.Password;

@Data
public class UserCredentials {
    private String username;
    private Password password;

    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public String getPasswordHash() {
        return CryptUtils.sha256(new String(password.getValue()));
    }
}
