import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.function.Predicate;

public class A {
    public static void main(String[] args) {
        var mach = new Machine();
        mach.compile(new InputStreamReader(System.in));
        mach.run(instruction -> instruction.times > 0);
        System.out.println(mach.getAcc());
    }
}

class Machine {
    private int accumulator = 0;
    private int pc = 0;
    private final ArrayList<Instruction> insts = new ArrayList<>(1024);

    void compile(Reader r) {
        insts.clear();
        pc = 0;

        new BufferedReader(r).lines().forEach(line -> {
            var toks = line.split("\\s+");
            var cmd = toks[0];
            var arg = Integer.parseInt(toks[1]);
            switch (cmd) {
            case "acc":
                insts.add(new Acc(arg));
                break;
            case "jmp":
                insts.add(new Jmp(arg));
                break;
            case "nop":
                insts.add(new Nop());
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + cmd);
            }

        });
    }

    void run(Breakpoint breakpoint) {
        while (pc < insts.size()) {
            var inst = insts.get(pc);
            if (breakpoint.test(inst)) {
                break;
            }
            inst.execute();
        }

    }

    @FunctionalInterface
    interface Breakpoint extends Predicate<Instruction> {}

    static abstract class Instruction {
        public int times;

        void execute() {
            exec();
            times++;
        }

        abstract void exec();
    }

    class Acc extends Instruction {
        int arg;

        Acc(int arg) { this.arg = arg; }

        @Override void exec() { accumulator += arg; pc++; }
    }

    class Nop extends Instruction {
        @Override void exec() { pc++; }
    }

    class Jmp extends Instruction {
        int arg;

        Jmp(int arg) { this.arg = arg; }

        @Override void exec() { pc += arg; }
    }

    int getAcc() { return accumulator; }
}
