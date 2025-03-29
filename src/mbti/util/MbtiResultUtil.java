package mbti.util;

import mbti.model.Result;
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
public class MbtiResultUtil {
    // MBTI 템플릿 데이터를 저장할 캐시
    private static HashMap<String, Result> mbtiTemplateCache = null;

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
    private HashMap<String, Result> loadMbtiResults() throws IOException, ParseException {
        // 결과를 저장할 HashMap 생성
        HashMap<String, Result> mbtiResultMap = new HashMap<>();

        // JSON 파싱 - try-with-resources 적용
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(MBTI_TEMPLATE_DATA_PATH.toFile())) {
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

        return mbtiResultMap;
    }

    /**
     * MBTI 템플릿 데이터를 HashMap 형태로 반환합니다.
     * 첫 호출 시 데이터를 로드하고 캐싱하여 이후 호출에서는 캐시된 데이터를 반환합니다.
     *
     * @return MBTI 유형을 키로 하고 세부 정보를 값으로 하는 HashMap
     * @throws RuntimeException 데이터 로딩 실패 시 발생
     */
    public HashMap<String, Result> getMbtiResultMap() {
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

    public Result getMbtiResult(String mbtiType) {
        if (mbtiType == null || mbtiType.isEmpty()) {
            return null;
        }
        return getMbtiResultMap().get(mbtiType.toUpperCase());
    }


}