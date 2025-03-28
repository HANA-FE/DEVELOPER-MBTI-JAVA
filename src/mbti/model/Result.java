package mbti.model;

import java.sql.Timestamp;
import java.util.List;

public class Result {
    private String mbtiType;
    private String mbtiName;
    private Timestamp startTime;
    private Timestamp endTime;
    // 추가 필드
    private String userName;
    private List<String> hashTags;
    private List<String> contents;

    public Result() {
    }

    // 전체 필드를 초기화하는 생성자
    public Result(String mbtiType, Timestamp startTime, Timestamp endTime,
                String userName, String mbtiName, List<String> hashTags, List<String> contents) {
        this.mbtiType = mbtiType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userName = userName;
        this.mbtiName = mbtiName;
        this.hashTags = hashTags;
        this.contents = contents;
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

    public String getName() {
        return userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public List<String> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<String> hashTags) {
        this.hashTags = hashTags;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
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
        if (hashTags != null && !hashTags.isEmpty()) {
            for (int i = 0; i < hashTags.size(); i++) {
                sb.append(hashTags.get(i));
                if (i < hashTags.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("\n\n");
        
        // 콘텐츠 출력
        sb.append("3가지 특징\n");
        if (contents != null && !contents.isEmpty()) {
            for (String content : contents) {
                sb.append("- ").append(content).append("\n");
            }
        }
        
        sb.append("\n테스트 시작 시간: ").append(startTime).append("\n");
        sb.append("테스트 종료 시간: ").append(endTime);
        
        return sb.toString();
    }
}