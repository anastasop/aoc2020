import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class B {
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
                var positions = advanceRule(line, Set.of(0), 0, rules);
                if (positions.contains(line.length())) {
                    matchesZero.addAndGet(1);
                }
            }
        });

        System.out.println(matchesZero.get());
    }

    public static Set<Integer> advanceRule(final String s, final Set<Integer> positions, final int num, final Map<Integer, List<List<Integer>>> rules) {
        if (num > ALIGN) {
            return advanceCharRule(s, positions, num - ALIGN);
        }

        var next = new HashSet<Integer>();
        for (var altRule: rules.get(num)) {
            next.addAll(advanceAltRule(s, positions, altRule, rules));
        }
        return next;
    }

    public static Set<Integer> advanceAltRule(final String s, final Set<Integer> positions, final List<Integer> rule, final Map<Integer, List<List<Integer>>> rules) {
        var next = new HashSet<Integer>();
	advance: for (var pos: positions) {
	    var prev = Set.of(pos);
	    Set<Integer> curr = null;
	    for (int r: rule) {
		curr = advanceRule(s, prev, r, rules);
		if (curr.containsAll(prev)) {
		    continue advance;
		}
		prev = curr;
	    }
	    next.addAll(curr);
	}
        return next;
    }

    public static Set<Integer> advanceCharRule(final String s, final Set<Integer> positions, int c) {
	var next = new HashSet<Integer>();
	for (var pos: positions) {
	    if (pos >= s.length()) {
		next.add(pos);
	    } else if (s.charAt(pos) == c) {
	        next.add(pos + 1);
	    }
	}
	return next;
    }
}
