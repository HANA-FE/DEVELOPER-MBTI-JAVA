package mbti.util;

import mbti.model.Result;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MBTI 유형별 결과 데이터와 관련된 유틸리티 메서드를 제공하는 클래스
 * JSON 파일에서 MBTI 유형별 결과 데이터를 로드하고 정보를 제공합니다.
 */
public class MbtiResultUtil {
    // MBTI 템플릿 데이터를 저장할 캐시
    private HashMap<String, Result> mbtiResultCache = null;

    // 날짜 형식
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // JSON 파일 경로
    private static final Path MBTI_RESULT_DATA_PATH = Paths.get("resources", "importData", "result.json");
    
    // 마지막으로 파일을 읽은 시간
    private FileTime lastModifiedTime = null;
    
    /**
     * 파일의 마지막 수정 시간을 업데이트합니다.
     */
    private void updateLastModifiedTime() {
        try {
            if (Files.exists(MBTI_RESULT_DATA_PATH)) {
                lastModifiedTime = Files.getLastModifiedTime(MBTI_RESULT_DATA_PATH);
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
            if (Files.exists(MBTI_RESULT_DATA_PATH)) {
                FileTime currentModifiedTime = Files.getLastModifiedTime(MBTI_RESULT_DATA_PATH);
                // lastModifiedTime이 현재 수정 시간과 다르면 파일이 변경된 것
                return !currentModifiedTime.equals(lastModifiedTime);
            }
        } catch (IOException e) {
            System.err.println("파일 수정 시간 확인 중 오류 발생: " + e.getMessage());
        }
        return false;
    }

    /**
     * result.json 파일에서 MBTI 템플릿 데이터를 로드하여 HashMap으로 반환합니다.
     * - key: MBTI 유형 (예: "ISFP")
     * - value: Map<String, Object>으로 name, hashTag, content 정보 포함
     *
     * @return MBTI 유형을 키로 하는 HashMap
     * @throws IOException    JSON 파일 읽기 실패 시 발생
     * @throws ParseException JSON 파싱 실패 시 발생
     */
    private HashMap<String, Result> loadMbtiResults() throws IOException, ParseException {
        // 결과를 저장할 HashMap 생성
        HashMap<String, Result> mbtiResultMap = new HashMap<>();
        
        // 파일 수정 시간 업데이트
        updateLastModifiedTime();

        // JSON 파싱 - try-with-resources 적용
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(MBTI_RESULT_DATA_PATH.toFile())) {
            JSONArray resultsArray = (JSONArray) parser.parse(reader);

            // 각 MBTI 결과를 HashMap에 저장
            for (Object obj : resultsArray) {
                JSONObject resultObj = (JSONObject) obj;

                // MBTI 유형 (키값)
                String mbti = (String) resultObj.get("mbti");

                Result result = new Result(
                        mbti,
                        (String) resultObj.get("name"),
                        convertJsonArrayToList((JSONArray) resultObj.get("hashTag")),
                        convertJsonArrayToList((JSONArray) resultObj.get("content"))
                );
                mbtiResultMap.put(mbti, result);
            }
        }
        
        System.out.println("JSON 파일에서 " + mbtiResultMap.size() + "개의 MBTI 결과 템플릿을 로드했습니다.");
        return mbtiResultMap;
    }

    /**
     * MBTI 템플릿 데이터를 HashMap 형태로 반환합니다.
     * 첫 호출 시 데이터를 로드하고 캐싱하여 이후 호출에서는 캐시된 데이터를 반환합니다.
     * 파일이 외부에서 변경된 경우 새로운 데이터를 로드합니다.
     *
     * @return MBTI 유형을 키로 하고 세부 정보를 값으로 하는 HashMap
     * @throws RuntimeException 데이터 로딩 실패 시 발생
     */
    public HashMap<String, Result> getMbtiResultMap() {
        // 캐시가 없거나 파일이 변경된 경우 다시 로드
        if (mbtiResultCache == null || isFileModified()) {
            try {
                System.out.println("MBTI 결과 템플릿 데이터를 새로 로드합니다.");
                mbtiResultCache = loadMbtiResults();
            } catch (IOException | ParseException e) {
                throw new RuntimeException("MBTI 템플릿 데이터 로딩 실패: " + e.getMessage(), e);
            }
        }
        return mbtiResultCache;
    }

    // JSON 배열을 List<String>로 변환하는 메서드
    private List<String> convertJsonArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        if (jsonArray != null) {
            for (Object item : jsonArray) {
                list.add((String) item);
            }
        }
        return list;
    }

    /**
     * 특정 MBTI 유형의 결과 템플릿을 반환합니다.
     * 
     * @param mbtiType MBTI 유형 (예: "INFP")
     * @return 해당 MBTI 유형의 결과 템플릿 객체, 없으면 null
     */
    public Result getMbtiResult(String mbtiType) {
        if (mbtiType == null || mbtiType.isEmpty()) {
            return null;
        }
        return getMbtiResultMap().get(mbtiType.toUpperCase());
    }
    
    /**
     * 캐시를 무효화하여 다음 호출 시 데이터를 다시 로드하도록 합니다.
     */
    public void invalidateCache() {
        mbtiResultCache = null;
        lastModifiedTime = null;
    }
}