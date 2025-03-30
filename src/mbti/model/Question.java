package mbti.model;

public class Question {
    private String text;
    private String type;
    private String[] choices;

    public Question(String text, String type, String[] choices) {
        this.text = text;
        this.type = type;
        this.choices = choices;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String[] getChoices() {
        return choices;
    }
}