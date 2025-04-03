package mbti.service;

import mbti.model.Question;
import mbti.model.Result;
import mbti.util.QuestionUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static mbti.Main.terminal;

/**
 * MBTI 테스트를 관리하는 서비스 클래스
 * 사용자로부터 질문에 대한 응답을 받고 MBTI 유형을 계산
 */
public class TestService {
    // MBTI 대립 성격 유형 쌍
    private static final char[][] TYPE_PAIRS = {
            {'E', 'I'},
            {'S', 'N'},
            {'T', 'F'},
            {'J', 'P'}
    };

    private final QuestionUtil questionUtil;
    private final ConsoleService consoleService;

    /**
     * QuestionUtil을 주입받는 생성자
     * 
     * @param questionUtil 질문을 로드하는 유틸리티 클래스
     */
    public TestService(QuestionUtil questionUtil,ConsoleService consoleService) {
        this.questionUtil = questionUtil;
        this.consoleService = consoleService;
    }

	/**
	 * MBTI 테스트를 시작하고 결과를 반환
	 * 사용자로부터 질문에 대한 응답을 받아 MBTI 유형을 계산
	 *
	 * @return 테스트 결과 객체 (시작/종료 시간, MBTI 유형 포함)
	 */
	public Result startTest() throws IOException {
		Result result = new Result();
		Timestamp startTime = Timestamp.valueOf(LocalDateTime.now());
		result.setStartTime(startTime);

		List<Question> questions = questionUtil.getQuestions();

        // 각 성격 유형별 응답 횟수를 추적
        HashMap<Character, Integer> typeCounts = initializeTypeCounts();

		consoleService.println("아무키나 누르면 테스트가 시작됩니다!");
		terminal.reader().read();

		// 모든 질문에 대해 사용자 응답 수집
		for (int i = 0; i < questions.size(); i++) {
			processQuestion(questions.get(i), typeCounts);
		}

        // MBTI 결과 계산 및 설정
        String mbtiResult = calculateMBTI(typeCounts);
        result.setMbtiType(mbtiResult);

        Timestamp endTime = Timestamp.valueOf(LocalDateTime.now());
        result.setEndTime(endTime);

        return result;
    }

    /**
     * MBTI 성격 유형 계산 메서드
     * 각 성격 유형 쌍에서 더 높은 빈도의 유형을 선택
     * 
     * @param typeCounts 각 성격 유형별 응답 횟수
     * @return 계산된 4자리 MBTI 문자열
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

    /**
     * 성격 유형 카운트 맵 초기화
     * 모든 MBTI 성격 유형을 0으로 초기화
     * 
     * @return 초기화된 성격 유형 카운트 맵
     */
    private HashMap<Character, Integer> initializeTypeCounts() {
        HashMap<Character, Integer> typeCounts = new HashMap<>();
        for (char[] pair : TYPE_PAIRS) {
            for (char type : pair) {
                typeCounts.put(type, 0);
            }
        }
        return typeCounts;
    }

	/**
	 * 단일 질문에 대한 사용자 응답 처리
	 * 사용자 입력 유효성 검사 및 성격 유형 카운트 증가
	 *
	 * @param question   현재 질문 객체
	 * @param typeCounts 성격 유형별 카운트 맵
	 */
	private void processQuestion(Question question, HashMap<Character, Integer> typeCounts) {

		int cursor = 1;
		while (true) {
			try {
				consoleService.clearScreen();
				consoleService.showQuestion(question.getText(), question.getChoices()[0], question.getChoices()[1], cursor);
				int key = terminal.reader().read();

				if (key == 66 && cursor == 1) {
					cursor = 2;
				} else if (key == 65 && cursor == 2) {
					cursor = 1;
				} else if (key == 13) {
					break;
				}
			} catch (Exception e) {
				System.out.println("올바른 입력이 아닙니다.");
				System.exit(0);
			}
		}

		int selectedIndex = cursor - 1;
		String typeStr = question.getType();

		// 성격 유형 카운트 증가
		updateTypeCount(typeStr, selectedIndex, typeCounts);
	}

    /**
     * 선택된 질문의 성격 유형 카운트 업데이트
     * 
     * @param typeStr 성격 유형 문자열
     * @param selectedIndex 선택된 답변 인덱스
     * @param typeCounts 성격 유형별 카운트 맵
     */
    private void updateTypeCount(String typeStr, int selectedIndex, 
                                 HashMap<Character, Integer> typeCounts) {
        if (typeStr.length() == 2) {
            // 타입이 두 글자인 경우 (EI, SN, TF, JP)
            char selectedType = (selectedIndex == 0) ? typeStr.charAt(0) : typeStr.charAt(1);
            typeCounts.put(selectedType, typeCounts.get(selectedType) + 1);
        } else if (typeStr.length() == 1) {
            // 타입이 한 글자인 경우
            char type = typeStr.charAt(0);
            typeCounts.put(type, typeCounts.get(type) + 1);
        }
    }
}