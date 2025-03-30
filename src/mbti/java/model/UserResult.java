package mbti.java.model;


import java.util.Date;

public class UserResult {
    private String userName;
    private String mbtiResult;
    private PersonalityType personalityType;
    private Date testDate;

    public UserResult(String userName, String mbtiResult, PersonalityType personalityType) {
        this.userName = userName;
        this.mbtiResult = mbtiResult;
        this.personalityType = personalityType;
        this.testDate = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public String getMbtiResult() {
        return mbtiResult;
    }

    public PersonalityType getPersonalityType() {
        return personalityType;
    }

    public Date getTestDate() {
        return testDate;
    }

    @Override
    public String toString() {
        return userName + "," + mbtiResult + "," + personalityType.getName() + "," + testDate;
    }
}
