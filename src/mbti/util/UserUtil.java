package mbti.util;

import mbti.model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
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
    
    // 마지막으로 파일을 읽은 시간
    private FileTime lastModifiedTime = null;

    /**
     * JSON 파일에서 사용자 데이터를 로드
     *
     * @return 로드된 사용자 리스트
     */
    private List<User> loadUsersFromJson() {
        List<User> userList = new ArrayList<>();

        if (!Files.exists(JSON_FILE_PATH)) {
            System.out.println("사용자 데이터 파일이 없습니다. 새 파일을 생성합니다.");
            try {
                // 디렉토리가 없으면 생성
                Files.createDirectories(JSON_FILE_PATH.getParent());
                
                // 빈 JSON 배열로 파일 생성
                try (FileWriter fileWriter = new FileWriter(JSON_FILE_PATH.toFile(), StandardCharsets.UTF_8)) {
                    fileWriter.write("[]");
                    fileWriter.flush();
                }
                System.out.println("빈 사용자 데이터 파일이 생성되었습니다: " + JSON_FILE_PATH);
                
                // 파일 생성 후 수정 시간 저장
                updateLastModifiedTime();
            } catch (IOException e) {
                System.err.println("사용자 데이터 파일 생성 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
            return userList;
        }

        try {
            // 현재 파일 수정 시간 저장
            updateLastModifiedTime();
            
            // 파일 내용 확인을 위해 FileReader 사용
            FileReader reader = new FileReader(JSON_FILE_PATH.toFile(), StandardCharsets.UTF_8);
            
            // 파일이 비어있는지 확인
            int firstChar = reader.read();
            if (firstChar == -1) {
                System.out.println("사용자 데이터 파일이 비어 있습니다. 새로운 리스트를 초기화합니다.");
                reader.close();
                return userList;
            }
            
            // 파일 포인터를 다시 처음으로 이동 (새 FileReader 생성)
            reader.close();
            reader = new FileReader(JSON_FILE_PATH.toFile(), StandardCharsets.UTF_8);
            
            JSONParser parser = new JSONParser();
            try {
                Object obj = parser.parse(reader);
                JSONArray userArray = (JSONArray) obj;
    
                for (Object o : userArray) {
                    JSONObject userObject = (JSONObject) o;
                    String name = (String) userObject.get("userName");
                    User user = new User(name);
                    userList.add(user);
                }
    
                System.out.println("JSON 파일에서 " + userList.size() + "명의 사용자 데이터를 로드했습니다.");
            } catch (ParseException e) {
                System.err.println("사용자 데이터 파싱 중 오류 발생: " + e.getMessage());
                System.out.println("비어 있거나 유효하지 않은 JSON 파일입니다. 새로운 리스트를 초기화합니다.");
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("사용자 데이터 파일 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return userList;
    }
    
    /**
     * 파일의 마지막 수정 시간을 업데이트합니다.
     */
    private void updateLastModifiedTime() {
        try {
            if (Files.exists(JSON_FILE_PATH)) {
                lastModifiedTime = Files.getLastModifiedTime(JSON_FILE_PATH);
            }
        } catch (IOException e) {
            System.err.println("파일 수정 시간 확인 중 오류 발생: " + e.getMessage());
        }
    }
    
    /**
     * 파일이 마지막으로 읽은 이후 변경되었는지 확인합니다.
     * 
     * @return 파일이 변경되었으면 true, 그렇지 않으면 false
     */
    private boolean isFileModified() {
        try {
            if (Files.exists(JSON_FILE_PATH)) {
                FileTime currentModifiedTime = Files.getLastModifiedTime(JSON_FILE_PATH);
                // lastModifiedTime이 현재 수정 시간과 다르면 파일이 변경된 것
                return !currentModifiedTime.equals(lastModifiedTime);
            }
        } catch (IOException e) {
            System.err.println("파일 수정 시간 확인 중 오류 발생: " + e.getMessage());
        }
        return false;
    }

    /**
     * 사용자 리스트를 반환합니다.
     * 첫 호출 시 데이터를 로드하고 캐싱하여 이후 호출에서는 캐시된 데이터를 반환합니다.
     * 파일이 외부에서 변경된 경우 새로운 데이터를 로드합니다.
     *
     * @return 사용자 리스트
     */
    public List<User> getUsers() {
        // 캐시가 없거나 파일이 변경된 경우 다시 로드
        if (userCache == null || isFileModified()) {
            System.out.println("사용자 데이터를 새로 로드합니다.");
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
            try (FileWriter fileWriter = new FileWriter(JSON_FILE_PATH.toFile(), StandardCharsets.UTF_8)) {
                fileWriter.write(userArray.toJSONString());
                fileWriter.flush();
            }

            // 캐시 업데이트
            userCache = new ArrayList<>(userList);
            
            // 파일 수정 시간 업데이트
            updateLastModifiedTime();
            
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
        lastModifiedTime = null;
    }
}