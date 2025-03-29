package mbti;

import mbti.service.AppService;
import mbti.service.UserService;
import mbti.service.screenService;

import static mbti.service.screenService.scanner;

public class Main {
	public static void main(String[] args) {
		AppService app = new AppService();
		screenService screen = new screenService();
		UserService userService = new UserService();

		boolean isRunning = true;
		screen.clearScreen();

		while (isRunning) {
			screen.showMenu();

			int command;
			try {
				command = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("1~5 사이의 숫자를 입력하세요");
				continue;
			}

			switch (command) {
				case 1:
					screen.introduce();
					break;
				case 2:
					screen.showAllUsers();
					break;
				case 3:
					screen.clearScreen();
					userService.setUser();
					app.run();
					scanner.nextLine();
					break;
				case 4:
					screen.showAllResults();
					break;
				case 5:
					isRunning = false;
					break;
				default:
					break;
			}
		}
	}
}