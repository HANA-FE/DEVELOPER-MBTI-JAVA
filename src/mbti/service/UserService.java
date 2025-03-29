package mbti.service;

import static mbti.service.FileService.writeTextFile;
import static mbti.service.screenService.scanner;

public class UserService {
	public static String userName;

	public void setUser() {
		System.out.println("이름을 입력해주세요!");
		System.out.print("> ");
		userName = scanner.nextLine();
		writeTextFile("src/output/user.txt", userName);
	}
}