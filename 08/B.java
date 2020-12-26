import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.function.Predicate;

public class B {
    public static void main(String[] args) throws Exception {
        var mach = new Machine();
        var insts = mach.compile(new InputStreamReader(System.in));

	if (mach.run(insts, instruction -> instruction.times > 0)) {
	    System.out.printf("Halt: %d%n", mach.getAcc());
	}

        for (var i = 0; i < insts.size(); i++) {
            var orig = insts.get(i);
            if (orig instanceof Machine.Nop) {
                var nop = (Machine.Nop) orig;
                insts.set(i, mach.new Jmp(nop.arg));
            } else if (orig instanceof Machine.Jmp) {
                var jmp = (Machine.Jmp) orig;
                insts.set(i, mach.new Nop(jmp.arg));
            }

            if (mach.run(insts, instruction -> instruction.times > 0)) {
                System.out.printf("Halt: %d%n", mach.getAcc());
            }

            insts.set(i, orig);
        }
    }
}

class Machine {
    private int accumulator = 0;
    private int pc = 0;

    ArrayList<Instruction> compile(Reader r) {
        ArrayList<Instruction> insts = new ArrayList<>(1024);

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
                insts.add(new Nop(arg));
                break;
            default:
                throw new IllegalArgumentException("Unknown command: " + cmd);
            }
        });

        return insts;
    }

    boolean run(ArrayList<Instruction> insts, Breakpoint breakpoint) {
        for (var inst: insts) {
            inst.times = 0;
        }
        accumulator = 0;
        pc = 0;

        while (pc < insts.size()) {
            var inst = insts.get(pc);

            if (breakpoint.test(inst)) {
                //System.out.printf("Breakpoint: pc: %d %d%n", pc, accumulator);
                return false;
            }

            inst.execute();
            pc = inst.nextInst();
        }

        return true;
    }

    @FunctionalInterface
    interface Breakpoint extends Predicate<Instruction> {}

    abstract class Instruction {
        public int times;

        void execute() {
            exec();
            times++;
        }

        int nextInst() { return pc + 1; }

        void exec() {}
    }

    public class Nop extends Instruction {
        int arg;

        Nop(int arg) { this.arg = arg; }
    }

    public class Acc extends Instruction {
        int arg;

        Acc(int arg) { this.arg = arg; }

        @Override void exec() { accumulator += arg; }
    }

    public class Jmp extends Instruction {
        int arg;

        Jmp(int arg) { this.arg = arg; }

        @Override int nextInst() { return pc + arg; }
    }

    int getAcc() { return accumulator; }
}
