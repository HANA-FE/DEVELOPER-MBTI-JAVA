package mbti.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static mbti.service.ScreenService.scanner;
import static mbti.service.UserService.userName;
import static mbti.util.DateUtil.getCurrentTime;
import static mbti.util.FileUtil.jsonFileToArray;
import static mbti.util.FileUtil.writeJSONFile;

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

	private static void calculateResult(String type) {
		resultHash.compute(type, (key, value) -> value + 1);
	}

	private static void showResult(String result) {
		JSONArray jsonArray = jsonFileToArray("src/constant/result.json");
		JSONObject resultObj = null;

		for (Object obj : jsonArray) {
			JSONObject curObj = (JSONObject) obj;
			String type = (String) curObj.get("mbti");
			if (type.equals(result)) {
				resultObj = curObj;
				break;
			}
		}

		if (resultObj == null) return;

		ScreenService screenService = new ScreenService();
		screenService.showTestResult(resultObj);
		String resultAnimal = (String) resultObj.get("name");

		Map<String, String> data = new HashMap<>();
		data.put("userName", userName);
		data.put("result", resultAnimal);
		data.put("timeStamp", getCurrentTime());

		writeJSONFile("src/output/log.json", data);
	}

	public static String getMBTI(Map<String, Integer> hash) {
		StringBuilder result = new StringBuilder();

		result.append(hash.get("E") > hash.get("I") ? "E" : "I");
		result.append(hash.get("S") > hash.get("N") ? "S" : "N");
		result.append(hash.get("T") > hash.get("F") ? "T" : "F");
		result.append(hash.get("J") > hash.get("P") ? "J" : "P");

		return result.toString();
	}

	public void run() {
		ScreenService screenService = new ScreenService();
		int questionIdx = 1;

		JSONArray jsonArray = jsonFileToArray("src/constant/questions.json");
		if (jsonArray == null) return;

		while (questionIdx < jsonArray.size()) {
			screenService.clearScreen();
			JSONObject object = (JSONObject) jsonArray.get(questionIdx);
			String question = (String) object.get("text");
			JSONArray answers = (JSONArray) object.get("answers");

			JSONObject answer1 = (JSONObject) answers.get(0);
			JSONObject answer2 = (JSONObject) answers.get(1);

			screenService.showQuestion(question, (String) answer1.get("text"), (String) answer2.get("text"));

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