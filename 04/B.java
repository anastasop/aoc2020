import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class B {
    public static void main(String[] args) throws Exception {
        var builders = passportBuilders(new BufferedReader(new InputStreamReader(System.in)));
        var numValid = builders.stream().filter(Passport.PassportBuilder::valid).count();

        System.out.printf("Valid passports: %d%n", numValid);
    }

    static List<Passport.PassportBuilder> passportBuilders(BufferedReader reader) throws IOException {
        var builders = new ArrayList<Passport.PassportBuilder>();

        Passport.PassportBuilder builder = null;
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                if (builder != null) {
                    builders.add(builder);
                    builder = null;
                }
            } else {
                if (builder == null) {
                    builder = new Passport.PassportBuilder();
                }
                for (var pair: line.split("\\s+")) {
                    if (pair.startsWith("byr:")) {
                        builder.setByr(pair.substring(4));
                    } else if (pair.startsWith("iyr:")) {
                        builder.setIyr(pair.substring(4));
                    } else if (pair.startsWith("eyr:")) {
                        builder.setEyr(pair.substring(4));
                    } else if (pair.startsWith("hgt:")) {
                        builder.setHgt(pair.substring(4));
                    } else if (pair.startsWith("hcl:")) {
                        builder.setHcl(pair.substring(4));
                    } else if (pair.startsWith("ecl:")) {
                        builder.setEcl(pair.substring(4));
                    } else if (pair.startsWith("pid:")) {
                        builder.setPid(pair.substring(4));
                    } else if (pair.startsWith("cid:")) {
                        builder.setCid(pair.substring(4));
                    }
                }
            }
        }

        if (builder != null) {
            builders.add(builder);
        }

        return builders;
    }
}

class Passport {
    static class PassportBuilder {
        static class Criterion {
            final Pattern pattern;
            final Predicate<String> pred;

            Criterion(String pattern, Predicate<String> pred) {
                this.pattern = Pattern.compile(pattern);
                this.pred = pred;
            }

            public boolean matches(String s) {
                return pattern.matcher(s).matches() && pred.test(s);
            }
        }

        static Criterion[] criteria = new Criterion[]{
            new Criterion("^\\d{4}$", s -> { var year = Integer.valueOf(s); return 1920 <= year && year <= 2002; }),
            new Criterion("^\\d{4}$", s -> { var year = Integer.valueOf(s); return 2010 <= year && year <= 2020; }),
            new Criterion("^\\d{4}$", s -> { var year = Integer.valueOf(s); return 2020 <= year && year <= 2030; }),
            new Criterion("^\\d+(in|cm)$", s -> {
                var ht = Integer.valueOf(s.substring(0, s.length() - 2));
                if (s.endsWith("cm")) {
                    return 150 <= ht && ht <= 193;
                }
                return 59 <= ht && ht <= 76;
            }),
            new Criterion("^#[a-z0-9]{6}$", s -> { return true; }),
            new Criterion("^(amb|blu|brn|gry|grn|hzl|oth)$", s -> { return true; }),
            new Criterion("^\\d{9}$", s -> { return true; }),
            new Criterion(".?", s -> { return true; }),
        };

        private final boolean[] filled = new boolean[8];

        public PassportBuilder setByr(String s) { filled[0] = criteria[0].matches(s); return this; }
        public PassportBuilder setIyr(String s) { filled[1] = criteria[1].matches(s); return this; }
        public PassportBuilder setEyr(String s) { filled[2] = criteria[2].matches(s); return this; }
        public PassportBuilder setHgt(String s) { filled[3] = criteria[3].matches(s); return this; }
        public PassportBuilder setHcl(String s) { filled[4] = criteria[4].matches(s); return this; }
        public PassportBuilder setEcl(String s) { filled[5] = criteria[5].matches(s); return this; }
        public PassportBuilder setPid(String s) { filled[6] = criteria[6].matches(s); return this; }
        public PassportBuilder setCid(String s) { filled[7] = true; return this; }

        public boolean valid() {
            return filled[0] && filled[1] && filled[2] && filled[3] &&
                    filled[4] && filled[5] && filled[6];
        }

        public Passport build() {
            if (!valid()) {
                throw new IllegalStateException();
            }

            return new Passport();
        }
    }

    private Passport() {}
}