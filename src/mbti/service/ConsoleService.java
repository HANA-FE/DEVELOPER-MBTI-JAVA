package mbti.service;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class ConsoleService {
	public static void print(String str, int colorInt) {
		AttributedString content = new AttributedString(
				str,
				AttributedStyle.DEFAULT.foreground(colorInt)
		);
		System.out.print(content.toAnsi());

	}

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

	public void println(String str) {
		AttributedString content = new AttributedString(
				str,
				AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)
		);
		System.out.println(content.toAnsi());
	}

	public void printWarning(String str) {
		AttributedString content = new AttributedString(
				str,
				AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)
		);
		System.out.println(content.toAnsi());
	}

	public void println(String str, int colorInt) {
		AttributedString content = new AttributedString(
				str,
				AttributedStyle.DEFAULT.foreground(colorInt)
		);
		System.out.println(content.toAnsi());
	}

	public void showMenu(int curMenu) {
		AttributedStyle defaultStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN);
		AttributedStyle selectedStyle = AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE).bold();

		// 각 메뉴 항목에 스타일 적용
		AttributedString menuOptions = new AttributedStringBuilder()
				.append("          |                  |                  |                |        \n", defaultStyle)
				.append(curMenu == 1 ? new AttributedString(" 1. 소개  ", selectedStyle) : new AttributedString(" 1. 소개  ", defaultStyle))
				.append("|", defaultStyle)
				.append(curMenu == 2 ? new AttributedString("  2. 사용자 관리  ", selectedStyle) : new AttributedString("  2. 사용자 관리  ", defaultStyle))
				.append("|", defaultStyle)
				.append(curMenu == 3 ? new AttributedString("  3. 서비스 시작  ", selectedStyle) : new AttributedString("  3. 서비스 시작  ", defaultStyle))
				.append("|", defaultStyle)
				.append(curMenu == 4 ? new AttributedString("  4. 결과 관리  ", selectedStyle) : new AttributedString("  4. 결과 관리  ", defaultStyle))
				.append("|", defaultStyle)
				.append(curMenu == 5 ? new AttributedString("  5. 종료 ", selectedStyle) : new AttributedString("  5. 종료 ", defaultStyle))
				.append("\n", defaultStyle)
				.append("          |                  |                  |                |        ", defaultStyle)
				.toAttributedString();

		AttributedString divider = new AttributedString(
				"=",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)
		);

		AttributedString keyBoardInfo = new AttributedString(
				"⬅️ ➡️ 방향키로 메뉴를 이동하고 엔터 키로 선택하세요!",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)
		);

		System.out.println(divider.toAnsi().repeat(80));
		System.out.println(menuOptions.toAnsi());
		System.out.println(divider.toAnsi().repeat(80));
		System.out.println(keyBoardInfo.toAnsi());
	}

	public void showQuestion(String question, String answer1, String answer2, int cursor, int index) {
		println("━".repeat(70), AttributedStyle.CYAN);
		println(" ".repeat(33)+(index+1)+"/12"+" ".repeat(33));
		System.out.println();
		println("Q. " + question, AttributedStyle.CYAN);
		System.out.println();
		println("━".repeat(70), AttributedStyle.CYAN);
		System.out.println();
		println((cursor == 1 ? "> 1." : "  1.") + answer1, AttributedStyle.CYAN);
		System.out.println();
		println((cursor == 2 ? "> 2." : "  2.") + answer2, AttributedStyle.CYAN);
		System.out.println();
		println("━".repeat(70), AttributedStyle.CYAN);
		System.out.println();
	}

	public void showIntroduce() {
		AttributedString catArt = new AttributedString(
				"                                /\\_/\\\n" +
						"                               ( o.o )\n" +
						"                         ___________________\n" +
						"                        |                   |\n" +
						"                        |   Hello, world!   |\n" +
						"                        |___________________|\n",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)
		);
		AttributedString divider = new AttributedString(
				"━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)
		);

		AttributedString title = new AttributedString(
				"                        💻  개발놈 테스트 💻\n",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).bold()
		);

		AttributedString description = new AttributedString(
				"        식상한 MBTI는 가라! 나의 개발 성향을 동물로 알려드려요!\n",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW).bold()
		);

		System.out.println(catArt.toAnsi());
		System.out.println(divider.toAnsi());
		System.out.println();
		System.out.println(title.toAnsi());
		System.out.println(description.toAnsi());
		System.out.println(divider.toAnsi());
		System.out.println();

	}
}