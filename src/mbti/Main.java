package mbti;

import mbti.config.AppConfig;
import mbti.model.Result;
import mbti.model.User;
import mbti.service.InfoService;
import mbti.service.TestResultService;
import mbti.service.TestService;
import mbti.service.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // AppConfig를 통한 의존성 주입 (싱글톤 인스턴스 사용)
        AppConfig appConfig = AppConfig.getInstance();

        Scanner sc = new Scanner(System.in);
        TestService testService = appConfig.testService();
        InfoService infoService = appConfig.infoService();
        UserService userService = appConfig.userService();
        TestResultService testResultService = appConfig.testResultService();

        while (true) {
            System.out.println("===== MBTI 개발자 테스트 =====");
            System.out.println("1. 서비스 소개");
            System.out.println("2. 사용자 관리");
            System.out.println("3. 테스트 실행");
            System.out.println("4. 결과 관리");
            System.out.println("5. 종료하기");
            System.out.print("선택> ");

            int input;
            try {
                input = Integer.parseInt(sc.nextLine());
            }catch (NumberFormatException e){
                System.out.println("숫자를 입력하세요");
                continue;
            }


            if (input == 5) {
                userService.saveUsersToJson();
                testResultService.saveResultsToJson();
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            switch (input) {
                case 1:
                    infoService.showInfo();
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
                        } else if (username.length() > 2) {
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