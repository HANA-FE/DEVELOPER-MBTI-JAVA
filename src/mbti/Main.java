package mbti;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.println("=== 개발놈 TEST ===");
        System.out.println("테스트를 시작하려면 시작을 입력해주세요");
        String input = scanner.nextLine().trim();

        if (input.equalsIgnoreCase("시작")) {
            String name = "";

            while (true) {
                System.out.println("이름을 두글자 이내로 입력해주세요");
                name = scanner.nextLine().trim();

                if (name.length() <= 2 && name.length() > 0) {
                    break;
                } else {
                    System.out.println("두글자 이내로 입력해주세요");
                }
            }
            // 질문 시작 + 점수 누적
            Question.startQuestions(scanner, name);

            //결과 계산
            String mbti = Calculator.getResult();

            //결과 출력
            Result.showResult(mbti, name);

        }
    }
}

