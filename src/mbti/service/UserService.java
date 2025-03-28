package mbti.service;

import mbti.model.User;

public class UserService {
    public void showAllUser() {

    }

    public User createUser(String username) {
        return new User(username);
    }
}
