package mbti.model;

import java.sql.Timestamp;
import java.util.List;

public class Result {
    private String mbtiType;
    private String mbtiName;
    private Timestamp startTime;
    private Timestamp endTime;
    private String userName;
    private List<String> hashTag;
    private List<String> content;
    private boolean isCompleted = false;

    public Result() {
    }

    public Result(String mbtiType, Timestamp startTime, Timestamp endTime,
                String userName, String mbtiName, List<String> hashTag, List<String> contents) {
        this.mbtiType = mbtiType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userName = userName;
        this.mbtiName = mbtiName;
        this.hashTag = hashTag;
        this.content = contents;
    }

    // JSON 데이터를 Result 객체로 담기 위한 생성자
    public Result(String mbti, String name, List<String> hashTag, List<String> contents) {
        this.mbtiType = mbti;
        this.mbtiName = name;
        this.hashTag = hashTag;
        this.content = contents;
    }

    // 테스트 완료 여부 게터/세터
    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getMbtiType() {
        return mbtiType;
    }

    public void setMbtiType(String mbtiType) {
        this.mbtiType = mbtiType;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public List<String> getHashTag() {
        return hashTag;
    }

    public void setHashTag(List<String> hashTag) {
        this.hashTag = hashTag;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public String getMbtiName() {
        return mbtiName;
    }

    public void setMbtiName(String mbtiName) {
        this.mbtiName = mbtiName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("사용자 이름: ").append(userName).append("\n");
        sb.append("개발놈 유형: ").append(mbtiName).append("\n");

        
        // 해시태그 출력
        sb.append("특징: ");
        if (hashTag != null && !hashTag.isEmpty()) {
            for (int i = 0; i < hashTag.size(); i++) {
                sb.append(hashTag.get(i));
                if (i < hashTag.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("\n\n");
        
        // 콘텐츠 출력
        sb.append("3가지 특징\n");
        if (content != null && !content.isEmpty()) {
            for (String content : content) {
                sb.append("- ").append(content).append("\n");
            }
        }
        
        sb.append("\n테스트 시작 시간: ").append(startTime).append("\n");
        sb.append("테스트 종료 시간: ").append(endTime);
        
        return sb.toString();
    }
}