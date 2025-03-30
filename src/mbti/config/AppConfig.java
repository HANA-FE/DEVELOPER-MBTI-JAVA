package mbti.config;

import mbti.service.*;
import mbti.util.QuestionUtil;
import mbti.util.MbtiResultUtil;
import mbti.util.UserTestResultUtil;
import mbti.util.UserUtil;

/**
 * 애플리케이션의 의존성을 관리하는 싱글톤 설정 클래스
 * 모든 유틸리티와 서비스 인스턴스를 중앙에서 관리하고 단일 인스턴스로 제공
 */
public class AppConfig {
    
    // 싱글톤 인스턴스를 저장할 private static 변수
    private static AppConfig instance;

    // 각 인스턴스를 저장할 private 필드
    private QuestionUtil questionUtilInstance;
    private UserUtil userUtilInstance;
    private MbtiResultUtil mbtiResultUtilInstance;
    private UserTestResultUtil userTestResultUtilInstance;
    
    private TestService testServiceInstance;
    private InfoService infoServiceInstance;
    private UserService userServiceInstance;
    private TestResultService testResultServiceInstance;
    private ConsoleService consoleService;

    /**
     * 싱글톤 인스턴스를 반환하는 public static 메서드
     * 인스턴스가 없는 경우 새로 생성하고, 이미 존재하는 경우 기존 인스턴스 반환
     * 
     * @return AppConfig의 단일 인스턴스
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    /**
     * 외부에서 인스턴스 생성을 방지하는 private 생성자
     */
    private AppConfig() {
    }

    /**
     * QuestionUtil의 싱글톤 인스턴스를 반환
     * 인스턴스가 없으면 새로 생성
     * 
     * @return QuestionUtil의 단일 인스턴스
     */
    public QuestionUtil questionUtil() {
        if (questionUtilInstance == null) {
            questionUtilInstance = new QuestionUtil();
        }
        return questionUtilInstance;
    }

    /**
     * UserUtil의 싱글톤 인스턴스를 반환
     * 인스턴스가 없으면 새로 생성
     * 
     * @return UserUtil의 단일 인스턴스
     */
    public UserUtil userUtil() {
        if (userUtilInstance == null) {
            userUtilInstance = new UserUtil();
        }
        return userUtilInstance;
    }

    /**
     * mbtiResultUtil 싱글톤 인스턴스를 반환
     * 인스턴스가 없으면 새로 생성
     * 
     * @return mbtiResultUtil 단일 인스턴스
     */
    public MbtiResultUtil mbtiResultUtil() {
        if (mbtiResultUtilInstance == null) {
            mbtiResultUtilInstance = new MbtiResultUtil();
        }
        return mbtiResultUtilInstance;
    }

    /**
     * UserTestResultUtil의 싱글톤 인스턴스를 반환
     * 인스턴스가 없으면 새로 생성
     * 
     * @return UserTestResultUtil의 단일 인스턴스
     */
    public UserTestResultUtil userTestResultUtil() {
        if (userTestResultUtilInstance == null) {
            userTestResultUtilInstance = new UserTestResultUtil();
        }
        return userTestResultUtilInstance;
    }

    /**
     * TestService의 싱글톤 인스턴스를 반환
     * 인스턴스가 없으면 새로 생성
     * 
     * @return TestService의 단일 인스턴스
     */
    public TestService testService() {
        if (testServiceInstance == null) {
            testServiceInstance = new TestService(questionUtil(),consoleService());
        }
        return testServiceInstance;
    }

    /**
     * InfoService의 싱글톤 인스턴스를 반환
     * 인스턴스가 없으면 새로 생성
     * 
     * @return InfoService의 단일 인스턴스
     */
    public InfoService infoService() {
        if (infoServiceInstance == null) {
            infoServiceInstance = new InfoService();
        }
        return infoServiceInstance;
    }

    /**
     * UserService의 싱글톤 인스턴스를 반환
     * 인스턴스가 없으면 새로 생성
     * 
     * @return UserService의 단일 인스턴스
     */
    public UserService userService() {
        if (userServiceInstance == null) {
            userServiceInstance = new UserService(userUtil());
        }
        return userServiceInstance;
    }

    /**
     * TestResultService의 싱글톤 인스턴스를 반환
     * 인스턴스가 없으면 새로 생성
     * 
     * @return TestResultService의 단일 인스턴스
     */
    public TestResultService testResultService() {
        if (testResultServiceInstance == null) {
            testResultServiceInstance = new TestResultService(mbtiResultUtil(), userTestResultUtil());
        }
        return testResultServiceInstance;
    }

    public ConsoleService consoleService() {
        if (consoleService == null) {
            consoleService = new ConsoleService();
        }
        return consoleService;
    }
}