package mbti.java.model;

import java.util.List;

public class PersonalityType {
    private String mbti;
    private String name;
    private List<String> hashTag;
    private List<String> content;
    private String image;

    public PersonalityType(String mbti, String name, List<String> hashTag, List<String> content) {
        this.mbti = mbti;
        this.name = name;
        this.hashTag = hashTag;
        this.content = content;
    }

    public String getMbti() {
        return mbti;
    }

    public String getName() {
        return name;
    }

    public List<String> getHashTag() {
        return hashTag;
    }

    public List<String> getContent() {
        return content;
    }

}
