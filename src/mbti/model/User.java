package mbti.model;

public class User {
    String userName;

    public User(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public String getName() {
        return userName;
    }
}
