package mbti.service;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileService {
	public static void readTextFileAndPrint(String filePath) {
		try {
			String content = Files.readString(Paths.get(filePath));
			System.out.println(content);
		} catch (IOException e) {
			System.out.println("아직 검사를 진행한 유저가 없어요!");
		}
	}

	public static void writeTextFile(String filePath, String content) {
		File file = new File(filePath);
		File parentDir = file.getParentFile();

		if (parentDir != null && !parentDir.exists()) {
			parentDir.mkdirs();
		}

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter writer = new FileWriter(filePath, true);
			writer.write(content + "\n");
			writer.close();
		} catch (IOException e) {
			System.out.println("파일 저장 중 오류 발생: " + e.getMessage());
		}
	}

	public static JSONArray readJSONArrayFile(String filePath) {
		JSONArray jsonArray = null;
		try {
			JSONParser jsonParser = new JSONParser();
			Reader reader = new FileReader(filePath);
			Object objmain = jsonParser.parse(reader);
			jsonArray = (JSONArray) objmain;

		} catch (Exception e) {
			System.out.println(e);
		}
		return jsonArray;
	}

}