package mbti.service;

public class ConsoleService {
	public void clearScreen() {
		String os = System.getProperty("os.name").toLowerCase();
		try {
			if (os.contains("win")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				new ProcessBuilder("clear").inheritIO().start().waitFor();
			}
		} catch (Exception e) {
			System.out.println("Error clearing the screen: " + e.getMessage());
		}
	}

	public void showMenu() {
		System.out.println("========================================================================");
		System.out.println("          |                 |                 |               |         ");
		System.out.println(" 1. 소개  |  2.사용자 관리  |  3.서비스 시작  |  4.결과 관리  |  5.종료 ");
		System.out.println("          |                 |                 |               |         ");
		System.out.println("========================================================================");
		System.out.println();
		System.out.print("선택>> ");
	}

	public void showQuestion(String question, String answer1, String answer2) {
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println();
		System.out.println("Q. " + question);
		System.out.println();
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println();
		System.out.println("1. " + answer1);
		System.out.println();
		System.out.println("2. " + answer2);
		System.out.println();
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println();
		System.out.print("선택 > ");
	}
}