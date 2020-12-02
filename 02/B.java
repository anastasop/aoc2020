import java.io.BufferedReader;
import java.io.InputStreamReader;

public class B {
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
    public final int pos1;
    public final int pos2;
    public final char letter;

    public String toString() {
        return String.format("%d-%d %c", pos1, pos2, letter);
    }

    public boolean matches(String passwd) {
        var atPos1 = passwd.charAt(pos1 - 1) == letter;
        var atPos2 = passwd.charAt(pos2 - 1) == letter;

        return atPos1 && !atPos2 || !atPos1 && atPos2;
    }

    public static Policy parse(CharSequence seq) {
        int i = 0;

        int pos1 = 0;
        for (; i < seq.length() && Character.isDigit(seq.charAt(i)); i++) {
            pos1 = 10 * pos1 + Character.getNumericValue(seq.charAt(i));
        }

        for (; i < seq.length() && seq.charAt(i) == '-'; i++) {}

        int pos2 = 0;
        for (; i < seq.length() && Character.isDigit(seq.charAt(i)); i++) {
            pos2 = 10 * pos2 + Character.getNumericValue(seq.charAt(i));
        }

        for (; i < seq.length() && Character.isWhitespace(seq.charAt(i)); i++) {}

        char letter = ' ';
        if (i < seq.length()) {
            letter = seq.charAt(i);
        }

        return new Policy(pos1, pos2, letter);
    }

    private Policy(int pos1, int pos2, char letter) {
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.letter = letter;
    }
}