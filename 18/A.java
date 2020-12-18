import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class A {
    public static void main(String[] args) {
        System.out.println(new BufferedReader(new InputStreamReader(System.in)).lines().mapToLong(A::eval).sum());
    }

    public static long eval(String expr) {
        return evalExpr(new Cursor(expr));
    }

    public static long evalExpr(Cursor cur) {
        long val = evalFactor(cur);
        for (;;) {
            if (cur.follows("+")) {
                cur.take();
                val += evalFactor(cur);
            } else if (cur.follows("*")) {
                cur.take();
                val *= evalFactor(cur);
            } else {
                break;
            }
        }
        return val;
    }

    public static long evalFactor(final Cursor cur) {
        if (cur.follows(s -> Character.isDigit(s.charAt(0)))) {
            return Long.parseLong(cur.take());
        } else if (cur.follows("(")) {
            cur.take("(");
            var v = evalExpr(cur);
            cur.take(")");
            return v;
        } else {
            throw new IllegalArgumentException("can't match Factor");
        }
    }
}

class Cursor {
    private static final Pattern tokenPattern = Pattern.compile("\\d+|[*+()]");
    private final Matcher matcher;
    private String lookAhead;

    public Cursor(String expr) {
        matcher = tokenPattern.matcher(expr);
        advance();
    }

    private Optional<String> getLookahead() {
        return Optional.ofNullable(lookAhead);
    }

    private void advance() {
        lookAhead = matcher.find() ? matcher.group(0) : null;
    }

    public String take() {
        var s = getLookahead().orElseThrow();
        advance();
        return s;
    }

    public boolean follows(Predicate<String> pred) {
        return getLookahead().filter(pred).isPresent();
    }

    public boolean follows(String t) {
        return getLookahead().filter(s -> s.equals(t)).isPresent();
    }

    public void take(final String t) {
        if (!follows(t)) {
            throw new IllegalArgumentException("can't match " + t);
        }
        take();
    }
}
