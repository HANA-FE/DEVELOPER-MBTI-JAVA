package mbti.java.model;

public class Answer {
    private String type;
    private String text;

    public Answer(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
