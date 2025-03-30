package mbti.java.util;

import java.util.ArrayList;
import mbti.java.model.PersonalityType;
import mbti.java.model.Question;
import mbti.java.model.UserResult;

import java.io.File;
import java.util.List;

public class FileService {
    private static final String DATA_DIRECTORY = "data";
    private static final String QUESTIONS_FILE = DATA_DIRECTORY + File.separator + "questions.json";
    private static final String PERSONALITY_TYPES_FILE = DATA_DIRECTORY + File.separator + "result_types.json";
    private static final String RESULTS_FILE = DATA_DIRECTORY + File.separator + "user_results.json";

    private List<Question> questions;
    private List<PersonalityType> personalityTypes;

    // 생성자
    public FileService() {
        initialize();
    }

    // 초기화 메서드
    public void initialize() {
        // 데이터 디렉터리 확인
        FileHandler.ensureDirectoryExists(DATA_DIRECTORY);

        // 파일 존재 확인
        if (!FileHandler.fileExists(QUESTIONS_FILE) || !FileHandler.fileExists(PERSONALITY_TYPES_FILE)) {
            System.err.println("필요한 JSON 파일이 존재하지 않습니다.");
            System.err.println("다음 위치에 파일을 배치해주세요: " + new File(QUESTIONS_FILE).getAbsolutePath());
            System.err.println("다음 위치에 파일을 배치해주세요: " + new File(PERSONALITY_TYPES_FILE).getAbsolutePath());
            return;
        }

        // JSON 파일 로드
        loadData();
    }

    private void loadData() {
        try {
            // 질문 로드
            String questionsJson = FileHandler.readFile(QUESTIONS_FILE);
            questions = JsonParser.parseQuestions(questionsJson);

            // 성격 유형 로드
            String typesJson = FileHandler.readFile(PERSONALITY_TYPES_FILE);
            personalityTypes = JsonParser.parsePersonalityTypes(typesJson);
        } catch (Exception e) {
            System.err.println("데이터 로드 중 오류 발생:");
            e.printStackTrace();
        }
    }

    public static void saveResult(UserResult result) {
        FileHandler.saveResultAsJson(result, RESULTS_FILE);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<PersonalityType> getPersonalityTypes() {
        return personalityTypes;
    }
}