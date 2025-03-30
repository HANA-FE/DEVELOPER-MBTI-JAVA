package mbti.util;

import mbti.model.Question;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 파일에서 질문을 로드하는 유틸리티 클래스
 */
public class QuestionUtil {
    
    private static final Path QUESTIONS_FILE_PATH = Paths.get("resources", "importData", "questions.json");
    
    // 질문 데이터 캐시
    private List<Question> questionCache = null;
    
    /**
     * 질문 목록을 반환합니다.
     * 첫 호출 시 데이터를 로드하고 캐싱하여 이후 호출에서는 캐시된 데이터를 반환합니다.
     * 
     * @return 질문 목록
     */
    public List<Question> getQuestions() {
        // 캐시된 데이터가 없으면 로드
        if (questionCache == null) {
            questionCache = loadQuestionsFromJson();
        }
        return questionCache;
    }
    
    /**
     * JSON 파일에서 모든 질문을 로드
     * @return 질문 목록
     */
    private List<Question> loadQuestionsFromJson() {
        List<Question> questions = new ArrayList<>();
        JSONParser parser = new JSONParser();
        
        try (FileReader reader = new FileReader(QUESTIONS_FILE_PATH.toFile(), StandardCharsets.UTF_8)) {
            JSONArray questionsArray = (JSONArray) parser.parse(reader);

            for (int i = 0; i < questionsArray.size(); i++) {
                JSONObject questionObj = (JSONObject) questionsArray.get(i);
                Question question = parseQuestion(questionObj);
                questions.add(question);
            }
            
            System.out.println("JSON 파일에서 " + questions.size() + "개의 질문을 로드했습니다.");
        } catch (IOException e) {
            System.err.println("질문 파일을 읽는 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("질문 파일 파싱 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        return questions;
    }
    
    /**
     * JSON 객체에서 Question 객체 파싱
     * @param questionObj JSON 질문 객체
     * @return Question 객체
     */
    private Question parseQuestion(JSONObject questionObj) {
        String type = (String) questionObj.get("type");
        String text = (String) questionObj.get("text");
        
        // 선택지 배열 파싱
        JSONArray answersArray = (JSONArray) questionObj.get("answers");
        String[] choices = new String[answersArray.size()];
        
        for (int i = 0; i < answersArray.size(); i++) {
            JSONObject answerObj = (JSONObject) answersArray.get(i);
            choices[i] = (String) answerObj.get("text");
        }
        
        return new Question(text, type, choices);
    }
    
    /**
     * 캐시를 무효화하여 다음 호출 시 데이터를 다시 로드하도록 합니다.
     */
    public void invalidateCache() {
        questionCache = null;
    }
}