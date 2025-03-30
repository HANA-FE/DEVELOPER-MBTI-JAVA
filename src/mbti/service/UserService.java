package mbti.service;

import static mbti.service.ScreenService.scanner;

public class UserService {
	public static String userName;

	public void setUser() {
		System.out.println("이름을 입력해주세요!");
		System.out.print("> ");
		userName = scanner.nextLine();
	}
}