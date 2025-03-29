package mbti.java.model;

import java.util.List;

public class Question {
    private String type;
    private String text;
    private List<Answer> answers;

    public Question(String type, String text,  List<Answer> answers) {
        this.type = type;
        this.text = text;
        this.answers = answers;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }


    public List<Answer> getAnswers() {
        return answers;
    }
}