package mbti;

import mbti.service.AppService;
import mbti.service.ScreenService;
import mbti.service.UserService;

import static mbti.service.ScreenService.scanner;

public class Main {
	public static void main(String[] args) {
		AppService app = new AppService();
		ScreenService screen = new ScreenService();
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

			screen.clearScreen();
			switch (command) {
				case 1:
					screen.showIntroduce();
					break;
				case 2:
					screen.showLogs(true);
					break;
				case 3:
					userService.setUser();
					app.run();
					scanner.nextLine();
					break;
				case 4:
					screen.showLogs(false);
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