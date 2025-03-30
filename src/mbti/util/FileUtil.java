package mbti.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtil {
	public static List<String> JSONLogToArray(String filePath, boolean onlyName) {
		JSONParser parser = new JSONParser();
		List<String> logArray = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			JSONArray dataArray = (JSONArray) jsonObject.get("data");

			for (Object obj : dataArray) {
				JSONObject data = (JSONObject) obj;
				String userName = (String) data.get("userName");

				if (onlyName) {
					logArray.add(userName);
					continue;
				}

				String result = (String) data.get("result");
				String timeStamp = (String) data.get("timeStamp");
				String formattedLog = String.format("%-15s %-20s %-20s", userName, result, timeStamp);
				logArray.add(formattedLog);
			}

		} catch (IOException | ParseException e) {
			System.out.println("아직 검사를 진행한 유저가 없어요!");
		}
		return logArray;
	}

	public static JSONArray jsonFileToArray(String filePath) {
		JSONArray jsonArray = null;
		try {
			JSONParser jsonParser = new JSONParser();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
			Object objmain = jsonParser.parse(reader);
			jsonArray = (JSONArray) objmain;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return jsonArray;
	}

	public static void writeJSONFile(String filePath, Map<String, String> data) {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		JSONArray dataArray = new JSONArray();

		File file = new File(filePath);

		if (file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

				jsonObject = (JSONObject) parser.parse(reader);
				dataArray = (JSONArray) jsonObject.get("data");
			} catch (IOException | ParseException e) {
				System.out.println("기존 JSON 파일 읽기 실패: " + e.getMessage());
			}
		}

		JSONObject newJsonObject = new JSONObject();
		newJsonObject.putAll(data);
		dataArray.add(newJsonObject);

		jsonObject.put("data", dataArray);

		if (file.getParentFile() != null && !file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(jsonObject.toJSONString());
		} catch (IOException e) {
			System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
		}

	}
}