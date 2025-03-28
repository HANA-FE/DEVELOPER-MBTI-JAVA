package mbti.service;

import mbti.model.Result;
import mbti.model.User;
import mbti.util.MbtiResultTemplateUtil;
import mbti.util.UserTestResultUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 테스트 결과를 관리하는 서비스 클래스
 * 비즈니스 로직을 처리하고 MbtiResultTemplateUtil과 UserTestResultUtil을 통해 파일 작업을 수행합니다.
 */
public class TestResultService {
    // 사용자 테스트 결과를 모아둘 리스트
    private List<Result> testResultList = new ArrayList<>();
    private final MbtiResultTemplateUtil mbtiResultTemplateUtil;
    private final UserTestResultUtil userTestResultUtil;

    /**
     * TestResultService 생성자
     * 
     * @param mbtiResultTemplateUtil MBTI 결과 템플릿 유틸리티
     * @param userTestResultUtil 사용자 테스트 결과 유틸리티
     */
    public TestResultService(MbtiResultTemplateUtil mbtiResultTemplateUtil, UserTestResultUtil userTestResultUtil) {
        this.mbtiResultTemplateUtil = mbtiResultTemplateUtil;
        this.userTestResultUtil = userTestResultUtil;
        loadResultsFromJson();
    }

    /**
     * JSON 파일에서 사용자 테스트 결과 데이터를 로드하여 testResultList 초기화
     */
    public void loadResultsFromJson() {
        // UserTestResultUtil을 사용하여 파일에서 데이터 로드
        testResultList = userTestResultUtil.loadResultsFromJson();
    }

    /**
     * 테스트 결과를 보여주고 저장하는 메서드
     * 
     * @param user 테스트를 수행한 사용자
     * @param result 테스트 결과 객체
     * @return 완성된 테스트 결과 객체
     */
    public Result showThisResult(User user, Result result) {
        String userName = user.getName();
        
        // MBTI 유형에 따른 추가 정보 가져오기
        String mbtiType = result.getMbtiType();
        
        // MbtiResultTemplateUtil을 사용하여 해당 MBTI 유형의 정보 가져오기
        String mbtiName = mbtiResultTemplateUtil.getMbtiName(result.getMbtiType());
        List<String> hashTag = mbtiResultTemplateUtil.getHashTag(result.getMbtiType());
        List<String> contents = mbtiResultTemplateUtil.getContents(result.getMbtiType());
        
        // Result 객체에 정보 설정
        result.setUserName(userName);
        result.setMbtiName(mbtiName);
        result.setHashTag(hashTag);
        result.setContents(contents);
        
        // 결과를 리스트에 저장
        testResultList.add(result);
        
        // 결과를 JSON 파일에 자동 저장
        saveResultsToJson();
        
        return result;
    }
    
    /**
     * 결과 목록을 표시
     */
    public void showAllResult() {
        if (testResultList.isEmpty()) {
            System.out.println("아직 테스트 결과가 없습니다.");
            return;
        }
        
        System.out.println("===== 테스트 결과 목록 =====");
        for (int i = 0; i < testResultList.size(); i++) {
            System.out.println(testResultList.get(i));
            System.out.println("==========================");

        }
        System.out.println("==========================");
    }
    
    /**
     * 모든 테스트 결과를 JSON 파일로 저장
     */
    public void saveResultsToJson() {
        // UserTestResultUtil을 사용하여 파일에 데이터 저장
        userTestResultUtil.saveResultsToJson(testResultList);
    }
}