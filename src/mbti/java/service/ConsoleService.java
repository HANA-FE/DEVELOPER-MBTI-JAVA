package mbti.java.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import mbti.java.Main;
import mbti.java.model.Answer;
import mbti.java.model.PersonalityType;
import mbti.java.model.Question;
import mbti.java.model.UserResult;
import mbti.java.util.FileService;

public class ConsoleService {
    private final List<Question> questions;
    private final List<PersonalityType> personalityTypes;
    private final Scanner scanner;
    private final Map<String, Integer> dimensionScores;


    public ConsoleService(List<Question> questions, List<PersonalityType> personalityTypes) {
        this.questions = questions;
        this.personalityTypes = personalityTypes;
        this.scanner = new Scanner(System.in);
        this.dimensionScores = new HashMap<>();

        dimensionScores.put("E", 0);
        dimensionScores.put("I", 0);
        dimensionScores.put("S", 0);
        dimensionScores.put("N", 0);
        dimensionScores.put("T", 0);
        dimensionScores.put("F", 0);
        dimensionScores.put("J", 0);
        dimensionScores.put("P", 0);
    }

    public void start() {
        introduce();
        String userName = getUserName();
        runTest(userName);
    }

    public void introduce() {
        System.out.println("=".repeat(50));
        System.out.println("          개발자 성향 테스트 프로그램          ");
        System.out.println("=".repeat(50));
        System.out.println("환영합니다! 이 테스트는 당신의 개발자 성향을 분석합니다.");
        System.out.println("12개의 질문에 답변하시면 결과를 알려드립니다.");
        System.out.println();
    }

    private String getUserName() {
        System.out.print("당신의 이름을 입력해주세요: ");
        return scanner.nextLine().trim();
    }

    private void runTest(String userName) {
        System.out.println("\n" + userName + "님, 테스트를 시작합니다!");
        System.out.println("각 질문에 해당하는 답변 번호를 선택해주세요.");
        System.out.println("-".repeat(50));

        // 질문 순회
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int choice = askQuestion(question, i + 1);

            // 선택한 답변 기록
            Answer selectedAnswer = question.getAnswers().get(choice - 1);
            recordAnswer(selectedAnswer.getType());
        }

        // 결과 계산 및 표시
        String mbtiResult = calculateMbti();
        PersonalityType personalityType = findPersonalityType(mbtiResult);
        UserResult result = new UserResult(userName, mbtiResult, personalityType);

        displayResult(result);

        // 결과 저장
        FileService.saveResult(result);

        // 다시 테스트할지 물어보기
        askForRestart();
    }

    private int askQuestion(Question question, int questionNumber) {
        System.out.println("\n[질문 " + questionNumber + "] " + question.getText());

        List<Answer> answers = question.getAnswers();
        for (int i = 0; i < answers.size(); i++) {
            System.out.println((i + 1) + ". " + answers.get(i).getText());
        }

        int choice;
        while (true) {
            System.out.print("\n선택 (1-" + answers.size() + "): ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= answers.size()) {
                    break;
                }
                System.out.println("유효한 번호를 입력해주세요.");
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }

        return choice;
    }

    private void recordAnswer(String answerType) {
        dimensionScores.put(answerType, dimensionScores.get(answerType) + 1);
    }

    private String calculateMbti() {
        StringBuilder mbti = new StringBuilder();

        // E vs I
        mbti.append(dimensionScores.get("E") > dimensionScores.get("I") ? "E" : "I");

        // S vs N
        mbti.append(dimensionScores.get("S") > dimensionScores.get("N") ? "S" : "N");

        // T vs F
        mbti.append(dimensionScores.get("T") > dimensionScores.get("F") ? "T" : "F");

        // J vs P
        mbti.append(dimensionScores.get("J") > dimensionScores.get("P") ? "J" : "P");

        return mbti.toString();
    }

    private PersonalityType findPersonalityType(String mbti) {
        for (PersonalityType type : personalityTypes) {
            if (type.getMbti().equals(mbti)) {
                return type;
            }
        }

        // 일치하는 타입이 없을 경우 기본값 (첫 번째 유형) 반환
        return personalityTypes.get(0);
    }

    private void displayResult(UserResult result) {
        PersonalityType personalityType = result.getPersonalityType();

        System.out.println("\n" + "=".repeat(50));
        System.out.println("              테스트 결과              ");
        System.out.println("=".repeat(50));
        System.out.println(result.getUserName() + "님의 개발자 유형은...");
        System.out.println("\n" + personalityType.getName() + " (" + personalityType.getMbti() + ")\n");

        System.out.println("[해시태그]");
        for (String tag : personalityType.getHashTag()) {
            System.out.println("- " + tag);
        }

        System.out.println("\n[" + personalityType.getName() + "의 특징]");
        for (String content : personalityType.getContent()) {
            System.out.println("- " + content);
        }

        System.out.println("\n테스트 결과가 저장되었습니다!");
        System.out.println("-".repeat(50));
    }

    private void askForRestart() {
        System.out.print("\n다른 테스트를 진행하시겠습니까? (Y/N): ");
        String answer = scanner.nextLine().trim().toUpperCase();

        if (answer.equals("Y")) {
            // 점수 초기화
            resetScores();

            // 새 테스트 시작
            start();
        } else {
            System.out.println("\n테스트를 종료합니다. 감사합니다!");
            scanner.close();
        }
    }

    private void resetScores() {
        dimensionScores.replaceAll((k, v) -> 0);
    }
}