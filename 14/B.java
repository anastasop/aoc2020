import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class B {
    public static final Pattern cmdMask = Pattern.compile("mask = ([X01]{36})");
    public static final Pattern cmdMem = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");

    public static void main(String[] args) throws Exception{
        var decoder = new Decoder("000000000000000000000000000000000000");
        var mem = new HashMap<Long, Long>();

        var rd = new BufferedReader(new InputStreamReader(System.in));
        for (String s = rd.readLine(); s != null; s = rd.readLine()) {
            var matcher = cmdMask.matcher(s);
            if (matcher.matches()) {
                decoder = new Decoder(matcher.group(1));
            } else {
                matcher = cmdMem.matcher(s);
                if (matcher.matches()) {
                    var addr = Long.parseLong(matcher.group(1));
                    var val = Long.parseLong(matcher.group(2));
                    for (var loc: decoder.apply(addr)) {
                        mem.put(loc, val);
                    }
                } else {
                    throw new IllegalArgumentException(s);
                }
            }
        }

        System.out.println(mem.values().stream().mapToLong(t -> t).sum());
    }
}

class Decoder {
    private int numMasks;
    private final long []masks;

    private int numMutations;
    private final long []mutations;

    Decoder(String s) {
        masks = new long[s.length()];
        mutations = new long[s.length()];

        for (int pos = 0, i = s.length() - 1; i >= 0; pos++, i--) {
            switch (s.charAt(i)) {
                case 'X': mutations[numMutations++] = 1L << pos; break;
                case '1': masks[numMasks++] = 1L << pos; break;
                case '0': break;
                default: throw new IllegalArgumentException();
            }
        }
    }

    List<Long> apply(long val) {
        for (int i = 0; i < numMasks; i++) {
            val |= masks[i];
        }

        List<Long> addrs = new ArrayList<>();
        for (long i = 0; i < (1L << numMutations); i++) {
            var nextVal = val;
            for (int j = 0; j < numMutations; j++) {
                if ((i & (1L << j)) > 0) {
                    nextVal |= mutations[j];
                } else {
                    nextVal &= ~mutations[j];
                }
            }
            addrs.add(nextVal);
        }
        return addrs;
    }
}
