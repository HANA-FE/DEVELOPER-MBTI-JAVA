package mbti.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Scanner;

import static mbti.service.UserService.userName;
import static mbti.util.FileUtil.JSONLogToArray;

public class ScreenService {
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
		System.out.println("========================================================================");
		System.out.println("          |                 |                 |               |         ");
		System.out.println(" 1. 소개  |  2.사용자 관리  |  3.서비스 시작  |  4.결과 관리  |  5.종료 ");
		System.out.println("          |                 |                 |               |         ");
		System.out.println("========================================================================");
		System.out.println();
		System.out.print("선택>> ");
	}

	public void showLogs(boolean onlyName) {
		List<String> logList = JSONLogToArray("src/output/log.json", onlyName);
		if (logList.isEmpty()) return;
		
		if (onlyName) {
			System.out.println("userName");

		} else {
			System.out.printf("%-15s %-20s %-20s%n", "userName", "result", "timeStamp");
		}
		System.out.println("------------------------------------------------------------------------");

		for (String log : logList) {
			System.out.println(log);
		}
	}

	public void showTestResult(JSONObject resultObject) {
		String resultAnimal = (String) resultObject.get("name");

		System.out.println();
		System.out.println(userName + "님은 " + "'" + resultAnimal + "' 타입 이에요!");
		System.out.println();

		JSONArray hashTags = (JSONArray) resultObject.get("hashTag");
		for (Object hashTag : hashTags) {
			System.out.print(hashTag.toString() + "   ");
		}
		System.out.println();
		System.out.println();

		JSONArray contents = (JSONArray) resultObject.get("content");
		for (Object content : contents) {
			System.out.println(content.toString());
		}
		System.out.println();
		System.out.println();
	}

	public void showIntroduce() {
		System.out.println();
		System.out.println("                                /\\_/\\");
		System.out.println("                               ( o.o )");
		System.out.println("                         ___________________");
		System.out.println("                        |                   |");
		System.out.println("                        |   Hello, world!   |");
		System.out.println("                        |___________________|");
		System.out.println();
		System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		System.out.println();
		System.out.println("                        💻  개발놈 테스트 💻");
		System.out.println();
		System.out.println("        식상한 MBTI는 가라! 나의 개발 성향을 동물로 알려드려요!");
		System.out.println();
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