package mbti.util;

import mbti.model.Result;
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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 사용자의 테스트 결과를 관리하는 유틸리티 클래스
 * JSON 파일에서 사용자 테스트 결과를 로드하고 저장합니다.
 */
public class UserTestResultUtil {

    private static final Path USER_TEST_RESULTS_PATH = Paths.get("resources", "exportData", "userTestResults.json");

    // 날짜 형식
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // 결과 데이터 캐시
    private List<Result> resultCache = null;
    
    // 마지막으로 파일을 읽은 시간
    private FileTime lastModifiedTime = null;

    /**
     * JSON 파일에서 사용자 테스트 결과 데이터를 로드하여 List로 반환합니다.
     *
     * @return 테스트 결과 목록
     */
    private List<Result> loadResultsFromJson() {
        List<Result> resultList = new ArrayList<>();

        if (!Files.exists(USER_TEST_RESULTS_PATH)) {
            System.out.println("결과 데이터 파일이 없습니다. 새 파일을 생성합니다.");
            try {
                // 디렉토리가 없으면 생성
                Files.createDirectories(USER_TEST_RESULTS_PATH.getParent());
                
                // 빈 JSON 배열로 파일 생성
                try (FileWriter fileWriter = new FileWriter(USER_TEST_RESULTS_PATH.toFile(), StandardCharsets.UTF_8)) {
                    fileWriter.write("[]");
                    fileWriter.flush();
                }
                System.out.println("빈 결과 데이터 파일이 생성되었습니다: " + USER_TEST_RESULTS_PATH);
                
                // 파일 생성 후 수정 시간 저장
                updateLastModifiedTime();
            } catch (IOException e) {
                System.err.println("결과 데이터 파일 생성 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
            return resultList;
        }

        try {
            // 현재 파일 수정 시간 저장
            updateLastModifiedTime();
            
            // 파일 내용 확인을 위해 FileReader 사용
            FileReader reader = new FileReader(USER_TEST_RESULTS_PATH.toFile(), StandardCharsets.UTF_8);
            
            // 파일이 비어있는지 확인
            int firstChar = reader.read();
            if (firstChar == -1) {
                System.out.println("결과 데이터 파일이 비어 있습니다. 새로운 리스트를 초기화합니다.");
                reader.close();
                return resultList;
            }
            
            // 파일 포인터를 다시 처음으로 이동 (새 FileReader 생성)
            reader.close();
            reader = new FileReader(USER_TEST_RESULTS_PATH.toFile(), StandardCharsets.UTF_8);
            
            JSONParser parser = new JSONParser();
            
            try {
                Object obj = parser.parse(reader);
                JSONArray resultArray = (JSONArray) obj;
    
                for (Object o : resultArray) {
                    JSONObject resultObject = (JSONObject) o;
                    Result result = new Result();
    
                    // 기본 정보 설정
                    result.setUserName((String) resultObject.get("userName"));
                    result.setMbtiType((String) resultObject.get("mbtiType"));
                    result.setMbtiName((String) resultObject.get("mbtiName"));
    
                    // 완료 상태 정보 가져오기 (파일에 저장된 결과는 완료된 것으로 간주)
                    result.setCompleted(true);

                    // 시간 정보 파싱
                    String startTimeStr = (String) resultObject.get("startTime");
                    String endTimeStr = (String) resultObject.get("endTime");
    
                    if (startTimeStr != null) {
                        try {
                            Date startDate = dateFormat.parse(startTimeStr);
                            result.setStartTime(new Timestamp(startDate.getTime()));
                        } catch (java.text.ParseException e) {
                            System.err.println("시작 시간 파싱 중 오류 발생: " + e.getMessage());
                        }
                    }
    
                    if (endTimeStr != null) {
                        try {
                            Date endDate = dateFormat.parse(endTimeStr);
                            result.setEndTime(new Timestamp(endDate.getTime()));
                        } catch (java.text.ParseException e) {
                            System.err.println("종료 시간 파싱 중 오류 발생: " + e.getMessage());
                        }
                    }
    
                    // 해시태그 파싱
                    JSONArray hashTagArray = (JSONArray) resultObject.get("hashTag");
                    if (hashTagArray != null) {
                        List<String> hashTag = new ArrayList<>();
                        for (Object tag : hashTagArray) {
                            hashTag.add((String) tag);
                        }
                        result.setHashTag(hashTag);
                    }
    
                    // 내용 파싱
                    JSONArray contentArray = (JSONArray) resultObject.get("contents");
                    if (contentArray != null) {
                        List<String> contents = new ArrayList<>();
                        for (Object content : contentArray) {
                            contents.add((String) content);
                        }
                        result.setContent(contents);
                    }
    
                    resultList.add(result);
                }
    
                System.out.println("JSON 파일에서 " + resultList.size() + "개의 결과 데이터를 로드했습니다.");
            } catch (ParseException e) {
                System.err.println("결과 데이터 파싱 중 오류 발생: " + e.getMessage());
                System.out.println("비어 있거나 유효하지 않은 JSON 파일입니다. 새로운 리스트를 초기화합니다.");
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            System.err.println("결과 데이터 파일 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }
    
    /**
     * 파일의 마지막 수정 시간을 업데이트합니다.
     */
    private void updateLastModifiedTime() {
        try {
            if (Files.exists(USER_TEST_RESULTS_PATH)) {
                lastModifiedTime = Files.getLastModifiedTime(USER_TEST_RESULTS_PATH);
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
            if (Files.exists(USER_TEST_RESULTS_PATH)) {
                FileTime currentModifiedTime = Files.getLastModifiedTime(USER_TEST_RESULTS_PATH);
                // lastModifiedTime이 현재 수정 시간과 다르면 파일이 변경된 것
                return !currentModifiedTime.equals(lastModifiedTime);
            }
        } catch (IOException e) {
            System.err.println("파일 수정 시간 확인 중 오류 발생: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * 테스트 결과 리스트를 반환합니다.
     * 첫 호출 시 데이터를 로드하고 캐싱하여 이후 호출에서는 캐시된 데이터를 반환합니다.
     * 파일이 외부에서 변경된 경우 새로운 데이터를 로드합니다.
     *
     * @return 테스트 결과 리스트
     */
    public List<Result> getResults() {
        // 캐시가 없거나 파일이 변경된 경우 다시 로드
        if (resultCache == null || isFileModified()) {
            System.out.println("테스트 결과 데이터를 새로 로드합니다.");
            resultCache = loadResultsFromJson();
        }
        return resultCache;
    }

    /**
     * 테스트 결과 목록을 JSON 파일로 저장합니다.
     * 단, 테스트가 완료된 결과만 저장합니다.
     *
     * @param resultList 저장할 테스트 결과 목록
     * @return 저장 성공 여부
     */
    public boolean saveResultsToJson(List<Result> resultList) {
        if (resultList == null || resultList.isEmpty()) {
            System.out.println("저장할 테스트 결과가 없습니다.");
            return false;
        }

        // 완료된 결과만 필터링
        List<Result> completedResults = new ArrayList<>();
        for (Result result : resultList) {
            if (result.isCompleted()) {
                completedResults.add(result);
            }
        }
        
        if (completedResults.isEmpty()) {
            System.out.println("저장할 완료된 테스트 결과가 없습니다.");
            return false;
        }

        JSONArray resultArray = new JSONArray();

        for (Result result : completedResults) {
            JSONObject resultObject = new JSONObject();
            resultObject.put("userName", result.getUserName());
            resultObject.put("mbtiType", result.getMbtiType());
            resultObject.put("mbtiName", result.getMbtiName());
            // 완료 상태도 저장
            resultObject.put("isCompleted", result.isCompleted());

            // 시작 시간과 종료 시간 포맷팅
            if (result.getStartTime() != null) {
                resultObject.put("startTime", dateFormat.format(result.getStartTime()));
            }
            if (result.getEndTime() != null) {
                resultObject.put("endTime", dateFormat.format(result.getEndTime()));
            }

            // 해시태그 저장
            JSONArray hashTagArray = new JSONArray();
            if (result.getHashTag() != null) {
                for (String tag : result.getHashTag()) {
                    hashTagArray.add(tag);
                }
            }
            resultObject.put("hashTag", hashTagArray);

            // 내용 저장
            JSONArray contentArray = new JSONArray();
            if (result.getContent() != null) {
                for (String content : result.getContent()) {
                    contentArray.add(content);
                }
            }
            resultObject.put("contents", contentArray);

            resultArray.add(resultObject);
        }

        try {
            // 디렉토리가 없으면 생성
            Files.createDirectories(USER_TEST_RESULTS_PATH.getParent());

            // JSON 파일 생성 및 데이터 쓰기
            try (FileWriter fileWriter = new FileWriter(USER_TEST_RESULTS_PATH.toFile(), StandardCharsets.UTF_8)) {
                fileWriter.write(resultArray.toJSONString());
                fileWriter.flush();
            }
            
            // 캐시 업데이트
            resultCache = new ArrayList<>(resultList);
            
            // 파일 수정 시간 업데이트
            updateLastModifiedTime();

            System.out.println("테스트 결과가 " + USER_TEST_RESULTS_PATH + " 파일에 성공적으로 저장되었습니다.");
            return true;
        } catch (IOException e) {
            System.err.println("테스트 결과 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 캐시를 무효화하여 다음 호출 시 데이터를 다시 로드하도록 합니다.
     */
    public void invalidateCache() {
        resultCache = null;
        lastModifiedTime = null;
    }
}