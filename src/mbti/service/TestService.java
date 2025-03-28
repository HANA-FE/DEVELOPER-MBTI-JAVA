package mbti.service;

import mbti.model.Question;
import mbti.model.Result;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class TestService {
    private final FileService fileService = new FileService();
    private final QuestionLoader questionLoader = new QuestionLoader();
    
    // MBTI 대립 쌍 정의
    private static final char[][] TYPE_PAIRS = {
        {'E', 'I'},
        {'S', 'N'},
        {'T', 'F'},
        {'J', 'P'}
    };
    
    public Result startTest() {
        Scanner sc = new Scanner(System.in);
        Result result = new Result();
        Timestamp startTime = Timestamp.valueOf(LocalDateTime.now());

        // 질문 로드
        List<Question> questions = questionLoader.loadQuestions();
        
        // 각 타입별 개수를 추적하기 위한 맵
        HashMap<Character, Integer> typeCounts = new HashMap<>();
        // 8가지 기본 타입 초기화
        for (char[] pair : TYPE_PAIRS) {
            for (char type : pair) {
                typeCounts.put(type, 0);
            }
        }

        // 질문별 답변 받기
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println((i + 1) + ". " + question.getText());
            System.out.println("선택지:");
            for (int j = 0; j < question.getChoices().length; j++) {
                System.out.println((j + 1) + ": " + question.getChoices()[j]);
            }

            System.out.print("답변을 선택하세요 (1-" + question.getChoices().length + "): ");
            int choice = sc.nextInt();
            
            // 선택이 유효한 범위 내에 있는지 확인
            if (choice < 1 || choice > question.getChoices().length) {
                System.out.println("유효하지 않은 선택입니다. 1부터 " + question.getChoices().length + " 사이의 숫자를 입력하세요.");
                i--; // 같은 질문을 다시 반복하기 위해 인덱스 감소
                continue;
            }
            
            // 선택지 인덱스는 0부터 시작하므로 -1 해줌
            int selectedIndex = choice - 1;
            
            // 질문의 타입에 따라 처리
            String typeStr = question.getType();
            
            if (typeStr.length() == 2) {
                // 타입이 두 글자인 경우 (EI, SN, TF, JP)
                // 첫 번째 선택지는 첫 번째 타입, 두 번째 선택지는 두 번째 타입에 해당
                char selectedType = (selectedIndex == 0) ? typeStr.charAt(0) : typeStr.charAt(1);
                typeCounts.put(selectedType, typeCounts.get(selectedType) + 1);
            } else if (typeStr.length() == 1) {
                // 타입이 한 글자인 경우 (E, I, S, N, T, F, J, P)
                char type = typeStr.charAt(0);
                typeCounts.put(type, typeCounts.get(type) + 1);
            }
        }

        // MBTI 결과 계산
        String mbtiResult = calculateMBTI(typeCounts);
        result.setMbtiType(mbtiResult);
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.now());
        result.setStartTime(startTime);
        result.setEndTime(endTime);

        return result;
    }
    
    /**
     * 타입별 개수를 기반으로 MBTI 결과 계산
     */
    private String calculateMBTI(HashMap<Character, Integer> typeCounts) {
        StringBuilder mbti = new StringBuilder();

        for (char[] pair : TYPE_PAIRS) {
            char first = pair[0];
            char second = pair[1];
            mbti.append(typeCounts.get(first) >= typeCounts.get(second) ? first : second);
        }

        return mbti.toString();
    }

}