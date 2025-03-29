package mbti;

public class Calculator {
    static int e = 0, i = 0;
    static int n = 0, s = 0;
    static int t = 0, f = 0;
    static int p = 0, j = 0;

    public static void addscore(int questionIndex, String answer) {

        if(answer.equals("1")){
            if(questionIndex <3) e++;
            else if(questionIndex < 6) s++;
            else if(questionIndex < 9) t++;
            else if(questionIndex < 12) j++;
        } else if(answer.equals("2")){
            if(questionIndex <3) i++;
            else if(questionIndex < 6) n++;
            else if(questionIndex < 9) f++;
            else if(questionIndex < 12) j++;
        }
    }
    public static String getResult() {
        StringBuilder mbti = new StringBuilder();

        mbti.append(e >= i ? "E" : "I");
        mbti.append(s >= n ? "S" : "N");
        mbti.append(t >= f ? "T" : "F");
        mbti.append(j >= p ? "P" : "J");

        return mbti.toString();
    }
    public static void resetScore() {
        e = i = s = n = t = f = p = j = 0;
    }
}
