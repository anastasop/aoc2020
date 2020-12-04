import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class A {
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
                        builder.setByr();
                    } else if (pair.startsWith("iyr:")) {
                        builder.setIyr();
                    } else if (pair.startsWith("eyr:")) {
                        builder.setEyr();
                    } else if (pair.startsWith("hgt:")) {
                        builder.setHgt();
                    } else if (pair.startsWith("hcl:")) {
                        builder.setHcl();
                    } else if (pair.startsWith("ecl:")) {
                        builder.setEcl();
                    } else if (pair.startsWith("pid:")) {
                        builder.setPid();
                    } else if (pair.startsWith("cid:")) {
                        builder.setCid();
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
        private final boolean[] filled = new boolean[8];

        public PassportBuilder setByr() { filled[0] = true; return this; }
        public PassportBuilder setIyr() { filled[1] = true; return this; }
        public PassportBuilder setEyr() { filled[2] = true; return this; }
        public PassportBuilder setHgt() { filled[3] = true; return this; }
        public PassportBuilder setHcl() { filled[4] = true; return this; }
        public PassportBuilder setEcl() { filled[5] = true; return this; }
        public PassportBuilder setPid() { filled[6] = true; return this; }
        public PassportBuilder setCid() { filled[7] = true; return this; }

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