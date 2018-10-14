package Request;

/**
 * request of person
 * has getters and setters
 */
public class PersonRequest {
    private String personId;
    private String authToken;

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
