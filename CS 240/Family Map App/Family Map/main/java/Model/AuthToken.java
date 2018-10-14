package Model;

import java.util.Objects;

public class AuthToken {
    private String authToken;
    private String username;


    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUserId(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken1 = (AuthToken) o;
        return Objects.equals(authToken, authToken1.authToken) &&
                Objects.equals(username, authToken1.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(authToken, username);
    }
}
