package mbti;

import mbti.config.AppConfig;
import mbti.model.Result;
import mbti.model.User;
import mbti.service.*;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // AppConfig를 통한 의존성 주입 (싱글톤 인스턴스 사용)
        AppConfig appConfig = AppConfig.getInstance();

        // 콘솔 인코딩 정보 출력 (디버깅용)
        System.out.println("현재 콘솔 인코딩: " + System.getProperty("file.encoding"));
        System.out.println("현재 콘솔 Charset: " + java.nio.charset.Charset.defaultCharset().name());

        // UTF-8 인코딩을 명시적으로 사용하는 Scanner 생성
        Scanner sc = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        TestService testService = appConfig.testService();
        InfoService infoService = appConfig.infoService();
        UserService userService = appConfig.userService();
        TestResultService testResultService = appConfig.testResultService();
        ConsoleService consoleService = appConfig.consoleService();

        while (true) {
            consoleService.showMenu();
//            System.out.println("===== MBTI 개발자 테스트 =====");
//            System.out.println("1. 서비스 소개");
//            System.out.println("2. 사용자 관리");
//            System.out.println("3. 테스트 실행");
//            System.out.println("4. 결과 관리");
//            System.out.println("5. 종료하기");
//            System.out.print("선택> ");

            int input;
            try {
                input = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력하세요");
                continue;
            }


            if (input == 5) {
                System.out.println("데이터를 저장하고 프로그램을 종료합니다...");
                // 사용자 데이터와 완료된 테스트 결과만 저장
                userService.saveUsersToJson();
                testResultService.saveResultsToJson();
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            consoleService.clearScreen();
            switch (input) {
                case 1:
                    infoService.showIntroduce();
                    break;
                case 2:
                    userService.showAllUser();
                    break;
                case 3:
                    System.out.println("사용자 이름을 입력하세요 (최대 2글자):");
                    String username;
                    while (true) {
                        username = sc.nextLine().trim();
                        if (username.isEmpty()) {
                            System.out.println("이름을 입력해주세요.");
                        } else if (username.length() > 5) {
                            System.out.println("이름은 최대 2글자까지만 가능합니다. 다시 입력해주세요:");
                        } else {
                            break;
                        }
                    }
                    User user = userService.createUser(username);
                    Result result = testService.startTest();
                    result = testResultService.showThisResult(user, result);
                    System.out.println(result);
                    break;
                case 4:
                    testResultService.showAllResult();
                    break;
                default:
                    System.out.println("올바른 메뉴를 다시 입력해 주세요");
            }
        }
    }
}