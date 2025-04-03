package mbti;

import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import mbti.config.AppConfig;
import mbti.model.Result;
import mbti.model.User;
import mbti.service.*;

import java.io.IOException;

public class Main {
	public static Terminal terminal;
	public static LineReader reader;

	public static void main(String[] args) throws IOException {
		terminal = TerminalBuilder.builder().system(true).build();
		terminal.enterRawMode();
		reader = LineReaderBuilder.builder().terminal(terminal).build();

		// AppConfig를 통한 의존성 주입 (싱글톤 인스턴스 사용)
		AppConfig appConfig = AppConfig.getInstance();

		// 콘솔 인코딩 정보 출력 (디버깅용)
		System.out.println("현재 콘솔 인코딩: " + System.getProperty("file.encoding"));
		System.out.println("현재 콘솔 Charset: " + java.nio.charset.Charset.defaultCharset().name());

		// UTF-8 인코딩을 명시적으로 사용하는 Scanner 생성
//		Scanner sc = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		TestService testService = appConfig.testService();
		InfoService infoService = appConfig.infoService();
		UserService userService = appConfig.userService();
		TestResultService testResultService = appConfig.testResultService();
		ConsoleService consoleService = appConfig.consoleService();

		// 커서 1번으로, 현재 보이는 메뉴는 없게 초기화
		int cursor = 1;
		int curMenu = -1;

		while (true) {
			// 메뉴 보여주기
			consoleService.showMenu(cursor);

			// 유저가 선택한 화면 보여주기. 첫 진입 시에는 아무 것도 안 보여줌
			switch (curMenu) {
				case 1:
					consoleService.showIntroduce();
					break;
				case 2:
					userService.showAllUser();
					break;
				case 3:
					consoleService.println("\n사용자 이름을 입력하세요 (최대 2글자):");
					String username;
					while (true) {
						username = reader.readLine().trim();

						if (username.isEmpty()) {
							consoleService.printWarning("이름을 입력해주세요.");
						} else if (username.length() > 2) {
							consoleService.printWarning("이름은 최대 2글자까지만 가능합니다. 다시 입력해주세요:");
						} else {
							break;
						}
					}
					User user = userService.createUser(username);
					Result result = testService.startTest();
					result = testResultService.showThisResult(user, result);
					System.out.println(result);
					curMenu = 0;
					continue;
				case 4:
					testResultService.showAllResult();
					break;
				default:
					break;
			}

			// 화면 다 보여주고 입력 받기
			int key = terminal.reader().read();

			// 커서 이동
			if (key == 68 && cursor != 1) {
				cursor--;
			} else if (key == 67 && cursor != 5) {
				cursor++;
			}

			// 커서 이동 후 이전 화면 지움
			consoleService.clearScreen();

			// 엔터 누르면 현재 화면 전환
			if (key == 13) {
				curMenu = cursor;
				if (curMenu == 5) {
					System.out.println("프로그램을 종료합니다.");
					testResultService.saveResultsToJson();
					userService.saveUsersToJson();
					break;
				}
			}
		}
	}
}