package mbti.util;

import mbti.model.Result;
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

    private static final Path USER_TEST_RESULTS_PATH = Paths.get("resources", "exportData", "testResults.json");

    // 날짜 형식
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * JSON 파일에서 사용자 테스트 결과 데이터를 로드하여 List로 반환합니다.
     *
     * @return 테스트 결과 목록
     */
    public List<Result> loadResultsFromJson() {
        List<Result> resultList = new ArrayList<>();

        if (!Files.exists(USER_TEST_RESULTS_PATH)) {
            System.out.println("결과 데이터 파일이 없습니다. 새로운 리스트를 초기화합니다.");
            return resultList;
        }

        try (FileReader reader = new FileReader(USER_TEST_RESULTS_PATH.toFile())) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(reader);
            JSONArray resultArray = (JSONArray) obj;

            for (Object o : resultArray) {
                JSONObject resultObject = (JSONObject) o;
                Result result = new Result();

                // 기본 정보 설정
                result.setUserName((String) resultObject.get("userName"));
                result.setMbtiType((String) resultObject.get("mbtiType"));
                result.setMbtiName((String) resultObject.get("mbtiName"));

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
        } catch (IOException e) {
            System.err.println("결과 데이터 파일 읽기 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("결과 데이터 파싱 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }

    /**
     * 테스트 결과 목록을 JSON 파일로 저장합니다.
     *
     * @param resultList 저장할 테스트 결과 목록
     * @return 저장 성공 여부
     */
    public boolean saveResultsToJson(List<Result> resultList) {
        if (resultList == null || resultList.isEmpty()) {
            System.out.println("저장할 테스트 결과가 없습니다.");
            return false;
        }

        JSONArray resultArray = new JSONArray();

        for (Result result : resultList) {
            JSONObject resultObject = new JSONObject();
            resultObject.put("userName", result.getUserName());
            resultObject.put("mbtiType", result.getMbtiType());
            resultObject.put("mbtiName", result.getMbtiName());

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
            try (FileWriter fileWriter = new FileWriter(USER_TEST_RESULTS_PATH.toFile())) {
                fileWriter.write(resultArray.toJSONString());
                fileWriter.flush();
            }

            System.out.println("테스트 결과가 " + USER_TEST_RESULTS_PATH + " 파일에 성공적으로 저장되었습니다.");
            return true;
        } catch (IOException e) {
            System.err.println("테스트 결과 저장 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}