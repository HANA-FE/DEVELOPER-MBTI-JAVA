package mbti.util;

import mbti.model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 데이터를 파일로 저장하고 불러오는 유틸리티 클래스
 */
public class UserUtil {
    // JSON 파일 경로
    private static final Path JSON_FILE_PATH = Paths.get("resources", "exportData", "users.json");

    // 사용자 데이터 캐시
    private List<User> userCache = null;

    /**
     * JSON 파일에서 사용자 데이터를 로드
     *
     * @return 로드된 사용자 리스트
     */
    private List<User> loadUsersFromJson() {
        List<User> userList = new ArrayList<>();

        if (!Files.exists(JSON_FILE_PATH)) {
            System.out.println("사용자 데이터 파일이 없습니다. 새로운 리스트를 초기화합니다.");
            return userList;
        }

        try (FileReader reader = new FileReader(JSON_FILE_PATH.toFile())) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray userArray = (JSONArray) obj;

            for (Object o : userArray) {
                JSONObject userObject = (JSONObject) o;
                String name = (String) userObject.get("userName");
                User user = new User(name);
                userList.add(user);
            }

            System.out.println("JSON 파일에서 " + userList.size() + "명의 사용자 데이터를 로드했습니다.");
        } catch (IOException e) {
            System.err.println("사용자 데이터 파일 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("사용자 데이터 파싱 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * 사용자 리스트를 반환합니다.
     * 첫 호출 시 데이터를 로드하고 캐싱하여 이후 호출에서는 캐시된 데이터를 반환합니다.
     *
     * @return 사용자 리스트
     * @throws RuntimeException 데이터 로딩 실패 시 발생
     */
    public List<User> getUsers() {
        // 캐시된 데이터가 없으면 로드
        if (userCache == null) {
            userCache = loadUsersFromJson();
        }
        return userCache;
    }

    /**
     * 모든 사용자 정보를 JSON 파일로 저장하고 캐시를 업데이트합니다.
     *
     * @param userList 저장할 사용자 리스트
     */
    public void saveUsersToJson(List<User> userList) {
        if (userList == null || userList.isEmpty()) {
            System.out.println("저장할 사용자 데이터가 없습니다.");
            return;
        }

        JSONArray userArray = new JSONArray();

        for (User user : userList) {
            JSONObject userObject = new JSONObject();
            userObject.put("userName", user.getName());
            userArray.add(userObject);
        }

        try {
            // 디렉토리가 없으면 생성
            Files.createDirectories(JSON_FILE_PATH.getParent());

            // JSON 파일 생성 및 데이터 쓰기
            try (FileWriter fileWriter = new FileWriter(JSON_FILE_PATH.toFile())) {
                fileWriter.write(userArray.toJSONString());
                fileWriter.flush();
            }

            // 캐시 업데이트
            userCache = new ArrayList<>(userList);

            System.out.println("사용자 데이터가 " + JSON_FILE_PATH + " 파일에 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("사용자 데이터 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 캐시를 무효화하여 다음 호출 시 데이터를 다시 로드하도록 합니다.
     */
    public void invalidateCache() {
        userCache = null;
    }
}