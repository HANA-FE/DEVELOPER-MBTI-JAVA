package mbti.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MBTI 유형별 템플릿 데이터와 관련된 유틸리티 메서드를 제공하는 클래스
 * JSON 파일에서 MBTI 유형별 템플릿 데이터를 로드하고 정보를 제공합니다.
 */
public class MbtiResultTemplateUtil {
    // MBTI 템플릿 데이터를 저장할 캐시
    private static HashMap<String, Map<String, Object>> mbtiTemplateCache = null;

    // 날짜 형식
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // JSON 파일 경로
    private static final Path MBTI_TEMPLATE_DATA_PATH = Paths.get("resources", "importData", "result.json");

    /**
     * result.json 파일에서 MBTI 템플릿 데이터를 로드하여 HashMap으로 반환합니다.
     * - key: MBTI 유형 (예: "ISFP")
     * - value: Map<String, Object>으로 name, hashTag, content 정보 포함
     *
     * @return MBTI 유형을 키로 하는 HashMap
     * @throws IOException    JSON 파일 읽기 실패 시 발생
     * @throws ParseException JSON 파싱 실패 시 발생
     */
    private HashMap<String, Map<String, Object>> loadMbtiResults() throws IOException, ParseException {
        // 결과를 저장할 HashMap 생성
        HashMap<String, Map<String, Object>> mbtiResultMap = new HashMap<>();

        // JSON 파싱 - try-with-resources 적용
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(MBTI_TEMPLATE_DATA_PATH.toFile())) {
            JSONArray resultsArray = (JSONArray) parser.parse(reader);

            // 각 MBTI 결과를 HashMap에 저장
            for (Object obj : resultsArray) {
                JSONObject resultObj = (JSONObject) obj;

                // MBTI 유형 (키값)
                String mbti = (String) resultObj.get("mbti");

                // 세부 정보를 담을 Map 생성
                Map<String, Object> resultDetails = new HashMap<>();

                // 이름 정보 추가
                resultDetails.put("name", resultObj.get("name"));

                // 해시태그 정보 추가 (JSONArray를 List<String>으로 변환)
                JSONArray hashTagArray = (JSONArray) resultObj.get("hashTag");
                List<String> hashTag = new ArrayList<>();
                if (hashTagArray != null) {
                    for (Object tag : hashTagArray) {
                        hashTag.add((String) tag);
                    }
                }
                resultDetails.put("hashtag", hashTag);

                // 내용 정보 추가 (JSONArray를 List<String>으로 변환)
                JSONArray contentArray = (JSONArray) resultObj.get("content");
                List<String> contents = new ArrayList<>();
                if (contentArray != null) {
                    for (Object content : contentArray) {
                        contents.add((String) content);
                    }
                }
                resultDetails.put("content", contents);

                // 결과 맵에 추가
                mbtiResultMap.put(mbti, resultDetails);
            }
        }

        return mbtiResultMap;
    }

    /**
     * MBTI 템플릿 데이터를 HashMap 형태로 반환합니다.
     * 첫 호출 시 데이터를 로드하고 캐싱하여 이후 호출에서는 캐시된 데이터를 반환합니다.
     *
     * @return MBTI 유형을 키로 하고 세부 정보를 값으로 하는 HashMap
     * @throws RuntimeException 데이터 로딩 실패 시 발생
     */
    public HashMap<String, Map<String, Object>> getMbtiResults() {
        // 캐시된 데이터가 없으면 로드
        if (mbtiTemplateCache == null) {
            try {
                mbtiTemplateCache = loadMbtiResults();
            } catch (IOException | ParseException e) {
                throw new RuntimeException("MBTI 템플릿 데이터 로딩 실패: " + e.getMessage(), e);
            }
        }
        return mbtiTemplateCache;
    }

    /**
     * 특정 MBTI 유형에 대한 세부 정보를 반환합니다.
     *
     * @param mbtiType MBTI 유형 (예: "ISFP")
     * @return 해당 MBTI 유형의 세부 정보를 담은 Map, 없으면 null 반환
     */
    public Map<String, Object> getMbtiResult(String mbtiType) {
        if (mbtiType == null || mbtiType.isEmpty()) {
            return null;
        }

        // 대문자로 변환하여 검색
        String upperCaseMbti = mbtiType.toUpperCase();
        return getMbtiResults().get(upperCaseMbti);
    }

    /**
     * 특정 MBTI 유형의 해시태그 목록을 반환합니다.
     *
     * @param mbtiType MBTI 유형 (예: "ISFP")
     * @return 해당 MBTI 유형의 해시태그 목록, 없으면 null 반환
     */
    @SuppressWarnings("unchecked")
    public List<String> getHashTag(String mbtiType) {
        Map<String, Object> result = getMbtiResult(mbtiType);
        return result != null ? (List<String>) result.get("hashtag") : null;
    }

    /**
     * 특정 MBTI 유형의 내용 목록을 반환합니다.
     *
     * @param mbtiType MBTI 유형 (예: "ISFP")
     * @return 해당 MBTI 유형의 내용 목록, 없으면 null 반환
     */
    @SuppressWarnings("unchecked")
    public List<String> getContents(String mbtiType) {
        Map<String, Object> result = getMbtiResult(mbtiType);
        return result != null ? (List<String>) result.get("content") : null;
    }

    /**
     * 특정 MBTI와 매칭된 동물 이름을 반환합니다.
     *
     * @param mbtiType MBTI 유형 (예: "ISFP")
     * @return 해당 동물 이름, 없으면 null 반환
     */
    @SuppressWarnings("unchecked")
    public String getMbtiName(String mbtiType) {
        Map<String, Object> result = getMbtiResult(mbtiType);
        return result != null ? (String) result.get("name") : null;
    }
}