package mbti.util;

import mbti.model.Question;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 파일에서 질문을 로드하는 유틸리티 클래스
 */
public class QuestionUtil {
    
    private static final Path QUESTIONS_FILE_PATH = Paths.get("resources", "importData", "questions.json");
    
    /**
     * JSON 파일에서 모든 질문을 로드
     * @return 질문 목록
     */
    public List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        JSONParser parser = new JSONParser();
        
        try (FileReader reader = new FileReader(QUESTIONS_FILE_PATH.toFile())) {
            JSONArray questionsArray = (JSONArray) parser.parse(reader);

            for (int i = 0; i < questionsArray.size(); i++) {
                JSONObject questionObj = (JSONObject) questionsArray.get(i);
                Question question = parseQuestion(questionObj);
                questions.add(question);
            }
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
}