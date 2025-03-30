package mbti.java.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import mbti.java.model.UserResult;

public class FileHandler {

    // 파일 읽기
    public static String readFile(String filePath) {
        try {
            if (!fileExists(filePath)) {
                return "[]";  // 파일이 없으면 빈 JSON 배열 반환
            }
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + filePath);
            e.printStackTrace();
            return "[]";
        }
    }

    // 디렉터리 존재 확인 및 생성
    public static void ensureDirectoryExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // 사용자 결과 JSON으로 저장
    public static void saveResultAsJson(UserResult result, String filePath) {
        try {
            // 기존 결과 읽기
            String jsonContent = readFile(filePath);

            // JSON 문자열 생성
            String newJsonContent;

            // 기존 파일이 비어 있거나 "[]"인 경우
            if (jsonContent.isEmpty() || jsonContent.trim().equals("[]")) {
                newJsonContent = "[" + JsonParser.resultToJson(result) + "]";
            } else {
                // 기존 내용이 있는 경우
                // 마지막 ']' 제거 후 새 결과 추가
                if (jsonContent.trim().endsWith("]")) {
                    int lastBracketIndex = jsonContent.lastIndexOf("]");
                    if (lastBracketIndex > 0) {
                        String withoutLastBracket = jsonContent.substring(0, lastBracketIndex);
                        if (withoutLastBracket.trim().endsWith("}")) {
                            newJsonContent = withoutLastBracket + "," + JsonParser.resultToJson(result) + "]";
                        } else {
                            newJsonContent = withoutLastBracket + JsonParser.resultToJson(result) + "]";
                        }
                    } else {
                        // 잘못된 형식의 JSON이 있는 경우 새로 시작
                        newJsonContent = "[" + JsonParser.resultToJson(result) + "]";
                    }
                } else {
                    // 잘못된 형식의 JSON이 있는 경우 새로 시작
                    newJsonContent = "[" + JsonParser.resultToJson(result) + "]";
                }
            }

            // 파일에 저장
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.println(newJsonContent);
                System.out.println("결과가 JSON으로 저장되었습니다: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("결과 저장 오류: " + e.getMessage());
        }
    }

    // 파일 존재 확인
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
}