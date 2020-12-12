import java.io.BufferedReader;
import java.io.InputStreamReader;

public class A {
    public static void main(String[] args) {
        final Ship ship = new Ship();
        new BufferedReader(new InputStreamReader(System.in)).lines().forEach(ship::move);
        System.out.printf("@(%d, %d) d: %d%n", ship.getX(), ship.getY(), ship.distance());
    }
}

class Ship {
    private int x = 0;
    private int y = 0;

    private int direction = 2;
    private final int[][] directions = {
            { 0, 1 },
            { 1, 1 },
            { 1, 0 },
            { 1, -1 },
            { 0, -1 },
            { -1, -1 },
            { -1, 0 },
            { -1, 1 }
    };

    private int abs(int a) {
        return a < 0 ? -a : a;
    }

    int getX() { return x; }
    int getY() { return y; }
    int distance() { return abs(x) + abs(y); }


    void move(String instruction) {
        var cmd = instruction.charAt(0);
        var amount = Integer.parseInt(instruction.substring(1));

        switch (cmd) {
            case 'N': y += amount; break;
            case 'S': y -= amount; break;
            case 'E': x += amount; break;
            case 'W': x -= amount; break;
            case 'R': rotate(true, amount); break;
            case 'L': rotate(false, amount); break;
            case 'F': x += directions[direction][0] * amount; y += directions[direction][1] * amount; break;
            default: throw new IllegalArgumentException("direction: " + new String(new char[]{cmd}));
        }
    }

    void rotate(boolean right, int degrees) {
        if (degrees % 45 != 0) {
            throw new IllegalArgumentException("I know only of integer arithmetic");
        }

        int times = degrees / 45;

        if (right) {
            direction = (direction + times) % directions.length;
        } else {
            direction = (direction - times + directions.length) % directions.length;
        }
    }
}
