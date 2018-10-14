package Request;

/**
 * request of Fill
 *
 */
public class FillRequest {
    private String userName;
    /**
     * default of generations is 4
     */
    private int generations = 4;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
