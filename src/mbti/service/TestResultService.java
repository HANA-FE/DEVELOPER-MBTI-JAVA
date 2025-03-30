package mbti.service;

import mbti.model.Result;
import mbti.model.User;
import mbti.util.MbtiResultUtil;
import mbti.util.UserTestResultUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 테스트 결과를 관리하는 서비스 클래스
 * 비즈니스 로직을 처리하고 MbtiResultUtil과 UserTestResultUtil을 통해 파일 작업을 수행합니다.
 */
public class TestResultService {
    // 사용자 테스트 결과를 모아둘 리스트
    private List<Result> testResultList = new ArrayList<>();
    private final MbtiResultUtil mbtiResultUtil;
    private final UserTestResultUtil userTestResultUtil;

    /**
     * TestResultService 생성자
     * 
     * @param mbtiResultUtil MBTI 결과 템플릿 유틸리티
     * @param userTestResultUtil 사용자 테스트 결과 유틸리티
     */
    public TestResultService(MbtiResultUtil mbtiResultUtil, UserTestResultUtil userTestResultUtil) {
        this.mbtiResultUtil = mbtiResultUtil;
        this.userTestResultUtil = userTestResultUtil;
        loadResultsFromJson();
    }

    /**
     * JSON 파일에서 사용자 테스트 결과 데이터를 로드하여 testResultList 초기화
     */
    public void loadResultsFromJson() {
        // UserTestResultUtil을 사용하여 파일에서 데이터 로드
        testResultList = userTestResultUtil.getResults();
    }

    /**
     * 테스트 결과를 보여주고 리스트에만 저장하는 메서드
     * (파일 저장은 프로그램 종료 시에만 수행)
     * 
     * @param user 테스트를 수행한 사용자
     * @param result 테스트 결과 객체
     * @return 완성된 테스트 결과 객체
     */
    public Result showThisResult(User user, Result result) {
        String userName = user.getName();
        
        // MBTI 유형에 따른 추가 정보 가져오기
        String mbtiType = result.getMbtiType();
        
        // MbtiResultUtil을 사용하여 해당 MBTI 유형의 정보 가져오기
        Result resultJsonData = mbtiResultUtil.getMbtiResult(mbtiType);
        
        // Result 객체에 정보 설정
        result.setUserName(userName);
        result.setMbtiName(resultJsonData.getMbtiName());
        result.setHashTag(resultJsonData.getHashTag());
        result.setContent(resultJsonData.getContent());
        
        // 테스트가 완료되었음을 표시
        result.setCompleted(true);
        
        // 결과를 리스트에 저장 (파일 저장은 프로그램 종료 시에만 수행)
        testResultList.add(result);
        
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
    }
    
    /**
     * 모든 테스트 결과를 JSON 파일로 저장
     */
    public void saveResultsToJson() {
        // UserTestResultUtil을 사용하여 파일에 데이터 저장
        userTestResultUtil.saveResultsToJson(testResultList);
    }
}