import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class A {
    private static final int ALIGN = 100000;

    public static void main(String[] args) {
        final var rules = new HashMap<Integer, List<List<Integer>>>();
        final var matchesZero = new AtomicInteger();

        new BufferedReader(new InputStreamReader(System.in)).lines().forEach(line -> {
            if (line.contains(":")) {
                var toks = line.split(":");
                var number = Integer.parseInt(toks[0]);
                var rule = new ArrayList<List<Integer>>();
                for (var tRule : toks[1].split("\\|")) {
                    var lrule = new ArrayList<Integer>();
                    for (var t : tRule.strip().split("\\s+")) {
                        var tt = t.strip();
                        if (tt.charAt(0) == '"') {
                            lrule.add(ALIGN + tt.charAt(1));
                        } else {
                            lrule.add(Integer.parseInt(tt));
                        }
                    }
                    rule.add(lrule);
                }
                rules.put(number, rule);
            } else if (line.isEmpty()) {
                // empty line empty code
            } else {
                var pos = advanceRule(line, 0, 0, rules);
                if (pos >= line.length()) {
                    matchesZero.addAndGet(1);
                }
            }
        });

        System.out.println(matchesZero.get());
    }

    public static int advanceRule(final String s, final int pos, final int num, final Map<Integer, List<List<Integer>>> rules) {
        if (num > ALIGN) {
            return advanceCharRule(s, pos, num - ALIGN);
        }

        int maxPos = 0;
        for (var altRule : rules.get(num)) {
            var altPos = advanceAltRule(s, pos, altRule, rules);
            if (altPos > maxPos) {
                maxPos = altPos;
            }
        }
        return maxPos;
    }

    public static int advanceAltRule(final String s, final int pos, final List<Integer> rule, final Map<Integer, List<List<Integer>>> rules) {
        if (pos >= s.length() || rule.isEmpty()) {
            return pos;
        }

        int advPos = pos;
        for (int r: rule) {
            var newPos = advanceRule(s, advPos, r, rules);
            if (newPos <= advPos) {
                break;
            }
            advPos = newPos;
        }
        return advPos;
    }

    public static int advanceCharRule(final String s, final int pos, int c) {
        return s.charAt(pos) == c ? pos + 1: pos;
    }
}
