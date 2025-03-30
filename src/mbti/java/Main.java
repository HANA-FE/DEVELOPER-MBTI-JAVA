package mbti.java;

import mbti.java.service.ConsoleService;
import mbti.java.util.FileService;

public class Main {

    public static void main(String[] args) {
        FileService fileService = new FileService();

        System.out.println("개발놈 테스트를 시작합니다...");

        ConsoleService console = new ConsoleService(fileService.getQuestions(), fileService.getPersonalityTypes());
        console.start();

    }


}