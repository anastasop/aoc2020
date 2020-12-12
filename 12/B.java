import java.io.BufferedReader;
import java.io.InputStreamReader;

public class B {
    public static void main(String[] args) {
        final Ship ship = new Ship(0, 0);
        final Waypoint point = new Waypoint(10, 1);
        new BufferedReader(new InputStreamReader(System.in)).lines().forEach(instruction -> {
            if (instruction.startsWith("F")) {
                ship.move(instruction, point);
            } else {
                point.move(instruction);
            }
        });

        System.out.printf("@(%d, %d) d: %d%n", ship.getX(), ship.getY(), ship.distance());
    }
}

class Ship {
    private int x = 0;
    private int y = 0;

    Ship(int x, int y) {
        this.x = x;
        this.y = y;
    }


    private int abs(int a) {
        return a < 0 ? -a : a;
    }

    int getX() { return x; }
    int getY() { return y; }
    int distance() { return abs(x) + abs(y); }

    void move(String instruction, Waypoint point) {
        var cmd = instruction.charAt(0);
        var amount = Integer.parseInt(instruction.substring(1));

        switch (cmd) {
            case 'F':
                for (int i = 0; i < amount; i++) {
                    x += point.getX();
                    y += point.getY();
                }
                break;
            default: throw new IllegalArgumentException("move: " + new String(new char[]{cmd}));
        }
    }
}

class Waypoint {
    private int x = 0;
    private int y = 0;

    Waypoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() { return x; }
    int getY() { return y; }

    void move(String instruction) {
        var cmd = instruction.charAt(0);
        var amount = Integer.parseInt(instruction.substring(1));

        switch (cmd) {
            case 'N': y += amount; break;
            case 'S': y -= amount; break;
            case 'E': x += amount; break;
            case 'W': x -= amount; break;
            case 'R': for (int i = 0; i < amount / 90; i++) { rotateRight(); } break;
            case 'L': for (int i = 0; i < amount / 90; i++) { rotateLeft(); } break;
            default: throw new IllegalArgumentException("move: " + new String(new char[]{cmd}));
        }
    }

    void rotateLeft() {
        var t = x;
        x = -y;
        y = t;
    }

    void rotateRight() {
        var t = x;
        x = y;
        y = -t;
    }
}
