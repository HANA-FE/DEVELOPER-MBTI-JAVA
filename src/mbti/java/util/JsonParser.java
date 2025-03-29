package mbti.java.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mbti.java.model.Answer;
import mbti.java.model.PersonalityType;
import mbti.java.model.Question;
import mbti.java.model.UserResult;

public class JsonParser {

    // 질문 파싱
    public static List<Question> parseQuestions(String jsonContent) {
        List<Question> questions = new ArrayList<>();

        // 각 객체 추출
        List<String> jsonObjects = extractJsonObjects(jsonContent);

        for (String questionJson : jsonObjects) {
            // 타입 추출
            String type = extractStringValue(questionJson, "type");

            // 텍스트 추출
            String text = extractStringValue(questionJson, "text");

            // 답변 목록 추출
            List<Answer> answers = parseAnswers(questionJson);

            // Question 객체 생성 및 추가
            if (!type.isEmpty() && !text.isEmpty()) {
                questions.add(new Question(type, text, answers));
            }
        }

        return questions;
    }

    // 답변 파싱
    private static List<Answer> parseAnswers(String questionJson) {
        List<Answer> answers = new ArrayList<>();

        // answers 배열 추출
        Pattern answersPattern = Pattern.compile("\"answers\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher answersMatcher = answersPattern.matcher(questionJson);

        if (answersMatcher.find()) {
            String answersJson = answersMatcher.group(1);

            // 각 답변 객체 추출
            List<String> answerObjects = extractJsonObjects(answersJson);

            for (String answerJson : answerObjects) {
                // 타입 추출
                String type = extractStringValue(answerJson, "type");

                // 텍스트 추출
                String text = extractStringValue(answerJson, "text");

                // Answer 객체 생성 및 추가
                if (!type.isEmpty() && !text.isEmpty()) {
                    answers.add(new Answer(type, text));
                }
            }
        }

        return answers;
    }

    // 성격 유형 파싱
    public static List<PersonalityType> parsePersonalityTypes(String jsonContent) {
        List<PersonalityType> types = new ArrayList<>();

        // 각 객체 추출
        List<String> jsonObjects = extractJsonObjects(jsonContent);

        for (String typeJson : jsonObjects) {
            // MBTI 추출
            String mbti = extractStringValue(typeJson, "mbti");

            // 이름 추출
            String name = extractStringValue(typeJson, "name");

            // 해시태그 목록 추출
            List<String> hashTags = extractStringArray(typeJson, "hashTag");

            // 내용 목록 추출
            List<String> contents = extractStringArray(typeJson, "content");

            // 이미지 추출
            String image = extractStringValue(typeJson, "image");

            // PersonalityType 객체 생성 및 추가
            if (!mbti.isEmpty() && !name.isEmpty()) {
                types.add(new PersonalityType(mbti, name, hashTags, contents));
            }
        }

        return types;
    }

    // 결과를 JSON 문자열로 변환
    public static String resultToJson(UserResult result) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"userName\":\"").append(result.getUserName()).append("\",");
        json.append("\"mbtiResult\":\"").append(result.getMbtiResult()).append("\",");
        json.append("\"testDate\":\"").append(result.getTestDate()).append("\",");
        json.append("\"personalityType\":{");
        json.append("\"mbti\":\"").append(result.getPersonalityType().getMbti()).append("\",");
        json.append("\"name\":\"").append(result.getPersonalityType().getName()).append("\"");
        json.append("}");
        json.append("}");
        return json.toString();
    }

    // 결과 리스트를 JSON 문자열로 변환
    public static String resultsToJson(List<UserResult> results) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < results.size(); i++) {
            if (i > 0) {
                json.append(",");
            }
            json.append(resultToJson(results.get(i)));
        }
        json.append("]");
        return json.toString();
    }

    // JSON 객체 추출
    private static List<String> extractJsonObjects(String jsonContent) {
        List<String> objects = new ArrayList<>();
        int start = -1;
        int braceCount = 0;

        for (int i = 0; i < jsonContent.length(); i++) {
            char c = jsonContent.charAt(i);

            if (c == '{') {
                braceCount++;
                if (braceCount == 1) {
                    start = i;
                }
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0 && start >= 0) {
                    objects.add(jsonContent.substring(start, i + 1));
                    start = -1;
                }
            }
        }

        return objects;
    }

    // 문자열 값 추출 헬퍼 메서드
    private static String extractStringValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    // 문자열 배열 추출 헬퍼 메서드
    private static List<String> extractStringArray(String json, String key) {
        List<String> values = new ArrayList<>();

        // 배열 추출
        Pattern pattern = Pattern.compile("\"" + key + "\"\\s*:\\s*\\[(.*?)\\]", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String arrayJson = matcher.group(1);

            // 배열 내 문자열 추출
            Pattern valuePattern = Pattern.compile("\"(.*?)\"");
            Matcher valueMatcher = valuePattern.matcher(arrayJson);

            while (valueMatcher.find()) {
                values.add(valueMatcher.group(1));
            }
        }

        return values;
    }
}