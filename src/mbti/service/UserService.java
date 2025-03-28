package mbti.service;

import mbti.model.User;
import mbti.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    // 사용자 목록을 저장할 리스트
    private List<User> userList = new ArrayList<>();
    private final UserUtil userUtil;

    /**
     * 생성자 - 서비스 초기화 시 파일에서 사용자 데이터 로드
     */
    public UserService(UserUtil userUtil) {
        this.userUtil = userUtil;
        loadUsersFromJson();
    }

    /**
     * JSON 파일에서 사용자 데이터를 로드하여 userList 초기화
     */
    public void loadUsersFromJson() {
        userList = userUtil.getUsers();
    }

    /**
     * 등록된 모든 사용자 목록 출력
     */
    public void showAllUser() {
        if (userList.isEmpty()) {
            System.out.println("아직 등록된 사용자가 없습니다.");
            return;
        }

        System.out.println("===== 등록된 사용자 목록 =====");
        for (int i = 0; i < userList.size(); i++) {
            System.out.println((i + 1) + ". " + userList.get(i).getName());
        }
        System.out.println("==========================");
    }

    /**
     * 사용자를 생성하고 리스트에 추가
     */
    public User createUser(String username) {
        User user = new User(username);
        userList.add(user); // 생성된 사용자를 리스트에 추가
        System.out.println(username + " 사용자가 등록되었습니다.");
        // 사용자가 추가될 때마다 파일 저장
        saveUsersToJson();
        return user;
    }

    /**
     * 모든 사용자 정보를 JSON 파일로 저장
     */
    public void saveUsersToJson() {
        userUtil.saveUsersToJson(userList);
    }

}
