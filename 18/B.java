import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class B {
    public static void main(String[] args) {
        System.out.println(new BufferedReader(new InputStreamReader(System.in)).lines().mapToLong(B::eval).sum());
    }

    /*
    expr -> expr * term | term
    term -> term + factor | factor
    factor -> ( expr ) | NUMBER

    A -> Aa | b =>
    A -> bA1
    A1 -> aA1 | ε

    expr -> term expr1
    expr1 -> * term expr1 | ε
    term -> factor term1
    term1 -> + factor term1 | ε
    factor -> ( expr ) | NUMBER
     */

    public static long eval(String expr) {
        return evalExpr(new Cursor(expr));
    }

    public static long evalExpr(Cursor cur) {
        var a = evalTerm(cur);
        return evalExpr1(cur).map(b -> a * b).orElse(a);
    }

    public static Optional<Long> evalExpr1(Cursor cur) {
        if (cur.follows("*")) {
            cur.take("*");
            var a = evalTerm(cur);
            var b = evalExpr1(cur);
            return b.isPresent() ? b.map(c -> c * a) : Optional.of(a);
        }
        return Optional.empty();
    }

    public static long evalTerm(Cursor cur) {
        var a = evalFactor(cur);
        return evalTerm1(cur).map(b -> a + b).orElse(a);
    }

    public static Optional<Long> evalTerm1(Cursor cur) {
        if (cur.follows("+")) {
            cur.take("+");
            var a = evalTerm(cur);
            var b = evalTerm1(cur);
            return b.isPresent() ? b.map(c -> c + a) : Optional.of(a);
        }
        return Optional.empty();
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
