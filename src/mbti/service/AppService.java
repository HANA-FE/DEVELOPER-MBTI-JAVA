package mbti.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static mbti.service.FileService.readJSONArrayFile;
import static mbti.service.FileService.writeTextFile;
import static mbti.service.UserService.userName;
import static mbti.service.screenService.scanner;
import static mbti.util.DateUtil.getCurrentTime;

public class AppService {
	private static final HashMap<String, Integer> resultHash = new HashMap<>() {{
		put("E", 0);
		put("I", 0);
		put("S", 0);
		put("N", 0);
		put("T", 0);
		put("F", 0);
		put("P", 0);
		put("J", 0);

	}};

	public static String getMBTI(Map<String, Integer> hash) {
		StringBuilder result = new StringBuilder();

		result.append(hash.get("E") > hash.get("I") ? "E" : "I");
		result.append(hash.get("S") > hash.get("N") ? "S" : "N");
		result.append(hash.get("T") > hash.get("F") ? "T" : "F");
		result.append(hash.get("J") > hash.get("P") ? "J" : "P");

		return result.toString();
	}

	private static void calculateResult(String type) {
		resultHash.compute(type, (key, value) -> value + 1);
	}

	private static void showResult(String result) {
		JSONArray jsonArray = readJSONArrayFile("src/constant/result.json");
		JSONObject resultObj = null;
		for (Object obj : jsonArray) {
			JSONObject curObj = (JSONObject) obj;
			String type = (String) curObj.get("mbti");
			if (type.equals(result)) {
				resultObj = curObj;
				break;
			}
		}
		String resultAnimal = (String) Objects.requireNonNull(resultObj).get("name");

		System.out.println();
		System.out.println(userName + "님은 " + resultAnimal + "에요!");
		System.out.println();

		JSONArray hashTags = (JSONArray) resultObj.get("hashTag");
		for (Object hashTag : hashTags) {
			System.out.print(hashTag.toString() + "   ");
		}
		System.out.println();
		System.out.println();

		JSONArray contents = (JSONArray) resultObj.get("content");
		for (Object content : contents) {
			System.out.println(content.toString());
		}
		System.out.println();
		System.out.println();

		writeTextFile("src/output/log.txt", userName + "    " + resultAnimal + "    " + getCurrentTime());
	}

	public void run() {
		screenService screenService = new screenService();
		int questionIdx = 1;

		JSONArray jsonArray = readJSONArrayFile("src/constant/questions.json");
		if (jsonArray == null) return;

		while (questionIdx < jsonArray.size()) {
			screenService.clearScreen();
			JSONObject object = (JSONObject) jsonArray.get(questionIdx);
			String quetion = (String) object.get("text");
			JSONArray answers = (JSONArray) object.get("answers");

			JSONObject answer1 = (JSONObject) answers.get(0);
			JSONObject answer2 = (JSONObject) answers.get(1);

			System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
			System.out.println();
			System.out.println("Q. " + quetion);
			System.out.println();
			System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
			System.out.println();
			System.out.println("1. " + answer1.get("text"));
			System.out.println();
			System.out.println("2. " + answer2.get("text"));
			System.out.println();
			System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
			System.out.println();
			System.out.print("선택 > ");

			int command = scanner.nextInt();
			switch (command) {
				case 1:
					calculateResult((String) answer1.get("type"));
					questionIdx++;
					break;
				case 2:
					calculateResult((String) answer2.get("type"));
					questionIdx++;
					break;
				default:
					break;
			}
			if (questionIdx == jsonArray.size()) {
				screenService.clearScreen();
				showResult(getMBTI(resultHash));
			}
		}
	}
}