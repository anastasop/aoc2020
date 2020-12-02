import java.io.BufferedReader;
import java.io.InputStreamReader;

public class A {
    public static void main(String[] args) throws Exception {
        var nvalid = new BufferedReader(new InputStreamReader(System.in)).lines().filter(line -> {
            var toks = line.split(":");
            var policy = Policy.parse(toks[0].strip());
            var passwd = toks[1].strip();

            var matches = policy.matches(passwd);
            var tick = matches ? "-" : "X";
            System.out.println(tick + " -- " + policy + " -- " + passwd);
            return matches;
        }).count();

        System.out.println("Valid passwords: " + nvalid);
    }
}

class Policy {
    public final int min;
    public final int max;
    public final char letter;

    public String toString() {
        return String.format("%d-%d %c", min, max, letter);
    }

    public boolean matches(String passwd) {
        int count = 0;
        for (var i = 0; i < passwd.length(); i++) {
            if (passwd.charAt(i) == letter) {
                count++;
            }
        }

        return min <= count && count <= max;
    }

    public static Policy parse(CharSequence seq) {
        int i = 0;

        int min = 0;
        for (; i < seq.length() && Character.isDigit(seq.charAt(i)); i++) {
            min = 10 * min + Character.getNumericValue(seq.charAt(i));
        }

        for (; i < seq.length() && seq.charAt(i) == '-'; i++) {}

        int max = 0;
        for (; i < seq.length() && Character.isDigit(seq.charAt(i)); i++) {
            max = 10 * max + Character.getNumericValue(seq.charAt(i));
        }

        for (; i < seq.length() && Character.isWhitespace(seq.charAt(i)); i++) {}

        char letter = ' ';
        if (i < seq.length()) {
            letter = seq.charAt(i);
        }

        return new Policy(min, max, letter);
    }

    private Policy(int min, int max, char letter) {
        this.min = min;
        this.max = max;
        this.letter = letter;
    }
}