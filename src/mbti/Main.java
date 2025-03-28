package mbti;

import mbti.model.Result;
import mbti.model.User;
import mbti.service.InfoService;
import mbti.service.ResultSerivce;
import mbti.service.TestService;
import mbti.service.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TestService testService = new TestService();
        InfoService infoService = new InfoService();
        UserService userService = new UserService();
        ResultSerivce resultSerivce = new ResultSerivce();


        while (true) {
            System.out.println("===== MBTI 개발자 테스트 =====");
            System.out.println("1. 서비스 소개");
            System.out.println("2. 사용자 관리");
            System.out.println("3. 테스트 실행");
            System.out.println("4. 결과 관리");
            System.out.println("5. 종료하기");
            System.out.print("선택> ");

            int input = Integer.parseInt(sc.nextLine());
            if (input == 5) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            switch (input) {
                case 1:
                    infoService.showInfo();
                    break;
                case 2:
//                    userService.showAllUser();
                    break;
                case 3:
                    String username = sc.nextLine();
                    User user = userService.createUser(username);
                    Result result = testService.startTest();
                    resultSerivce.showThisResult(user,result);
                    break;
                case 4:
//                    resultSerivce.showAllResult();
                    break;
                default:
                    System.out.println("올바른 메뉴를 다시 입력해 주세요");
            }
        }

    }
}
