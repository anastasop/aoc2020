import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Pattern;

public class A {
    public static final Pattern cmdMask = Pattern.compile("mask = ([X01]{36})");
    public static final Pattern cmdMem = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");

    public static void main(String[] args) throws Exception{
        var mask = new Mask("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        var mem = new HashMap<Long, Long>();

        var rd = new BufferedReader(new InputStreamReader(System.in));
        for (String s = rd.readLine(); s != null; s = rd.readLine()) {
            var matcher = cmdMask.matcher(s);
            if (matcher.matches()) {
                mask = new Mask(matcher.group(1));
            } else {
                matcher = cmdMem.matcher(s);
                if (matcher.matches()) {
                    mem.put(Long.parseLong(matcher.group(1)), mask.apply(Long.parseLong(matcher.group(2))));
                } else {
                    throw new IllegalArgumentException(s);
                }
            }
        }

        System.out.println(mem.values().stream().mapToLong(t -> t).sum());
    }
}

class Mask {
    private int numMasks;
    private final long []masks;

    Mask(String s) {
        masks = new long[s.length()];

        for (int pos = 0, i = s.length() - 1; i >= 0; pos++, i--) {
            switch (s.charAt(i)) {
                case 'X': break;
                case '1': masks[numMasks++] = (1L << pos); break;
                case '0': masks[numMasks++] = ~(1L << pos); break;
                default: throw new IllegalArgumentException();
            }
        }
    }

    long apply(long val) {
        for (var i = 0; i < numMasks; i++) {
            var mask = masks[i];
            if (mask < 0) {
                val &= mask;
            } else {
                val |= mask;
            }
        }

        return val;
    }
}