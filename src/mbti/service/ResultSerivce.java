package mbti.service;

import mbti.model.Result;
import mbti.model.User;
import mbti.util.MbtiResultUtil;
import java.util.List;
import static mbti.util.MbtiResultUtil.getMbtiName;


public class ResultSerivce {

    private final ResultLoader resultLoader = new ResultLoader();

    public void showThisResult(User user, Result result) {
        String userName = user.getName();
        
        // MBTI 유형에 따른 추가 정보 가져오기
        String mbtiType = result.getMbtiType();
        
        // MbtiResultUtil을 사용하여 해당 MBTI 유형의 정보 가져오기
        String mbtiName = getMbtiName(mbtiType);
        List<String> hashTags = MbtiResultUtil.getHashTags(mbtiType);
        List<String> contents = MbtiResultUtil.getContents(mbtiType);
        
        // Result 객체에 정보 설정
        result.setName(userName);
        result.setMbtiName(mbtiName);
        result.setHashTags(hashTags);
        result.setContents(contents);
        
        // userName result 출력
        System.out.println(result);
    }
}