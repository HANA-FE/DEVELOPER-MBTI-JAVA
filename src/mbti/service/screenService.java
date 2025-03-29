package mbti.service;

import java.util.Scanner;

import static mbti.service.FileService.readTextFileAndPrint;

public class screenService {
	public static Scanner scanner = new Scanner(System.in);

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
		System.out.println("================================================================");
		System.out.println("1. 소개 | 2.사용자 관리 | 3.서비스 시작 | 4.결과 관리 | 5.종료");
		System.out.println("================================================================");
	}

	public void showAllResults() {readTextFileAndPrint("src/output/log.txt");}

	public void showAllUsers() {
		readTextFileAndPrint("src/output/user.txt");
	}

	public void introduce() {
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println();
		System.out.println("                        /\\_/\\");
		System.out.println("                       ( o.o )");
		System.out.println("                 ___________________");
		System.out.println("                |                   |");
		System.out.println("                |   Hello, world!   |");
		System.out.println("                |___________________|");
		System.out.println();
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println("           나의 개발 성향을 알아보자!");
		System.out.println("식상한 MBTI는 가라! 나의 개발 성향을 동물로 알려드립니다.");
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println();
	}

}