import java.io.BufferedReader;
import java.io.InputStreamReader;

public class B {
    public static void main(String[] args) {
        var grid = new byte[128][128];

        var lines = new BufferedReader(new InputStreamReader(System.in)).lines().toArray(String[]::new);
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                var c = lines[y].charAt(x);
                if (c == '.') {
                    grid[y][x] = Floor.FLOOR;
                } else if (c == '#') {
                    grid[y][x] = Floor.OCCUPIED;
                } else if (c == 'L') {
                    grid[y][x] = Floor.EMPTY;
                } else {
                    throw new IllegalArgumentException(new String(new char[]{c}));
                }
            }
        }

        var floor = new Floor(grid, lines[0].length(), lines.length);
        var rounds = 0;
        while (floor.nextRound() > 0) {
            rounds++;
        }
        System.out.printf("Rounds: %d Occupied: %d%n", rounds, floor.occupiedCount());
    }
}

class Floor {
    public static final byte FLOOR = 0;
    public static final byte OCCUPIED = 1;
    public static final byte EMPTY = 2;

    byte[][] layout;
    byte[][] layout1;
    final int dx;
    final int dy;

    Floor(byte[][] layout, int dx, int dy) {
        this.layout = new byte[dy + 2][];
        this.layout1 = new byte[dy + 2][];
        for (var y = 0; y < dy + 2; y++) {
            this.layout[y] = new byte[dx + 2];
            this.layout1[y] = new byte[dx + 2];
        }

        for (int y = 0; y < dy; y++) {
            System.arraycopy(layout[y], 0, this.layout[y + 1], 1, dx);
        }

        this.dx = dx;
        this.dy = dy;
    }

    public int nextRound() {
        int changes = 0;

        for (int y = 1; y <= dy; y++) {
            for (int x = 1; x <= dx; x++) {
                var occupiedNearby = occupiedAround(x, y);
                if (isEmpty(x, y) && occupiedNearby == 0) {
                    layout1[y][x] = OCCUPIED;
                    changes++;
                } else if (isOccupied(x, y) && occupiedNearby >= 5) {
                    layout1[y][x] = EMPTY;
                    changes++;
                } else {
                    layout1[y][x] = layout[y][x];
                }
            }
        }

        var t = layout;
        layout = layout1;
        layout1 = t;

        return changes;
    }

    public int occupiedCount() {
        int count = 0;

        for (int y = 1; y <= dy; y++) {
            for (int x = 1; x <= dx; x++) {
                if (isOccupied(x, y)) {
                    count++;
                }
            }
        }

        return count;
    }

    public boolean isOccupied(int x, int y) {
        return at(x, y) == OCCUPIED;
    }

    public boolean isFloor(int x, int y) {
        return at(x, y) == FLOOR;
    }

    public boolean isEmpty(int x, int y) {
        return at(x, y) == EMPTY;
    }

    public byte at(int x, int y) {
        return layout[y][x];
    }

    private int trackOccupied(final int x0, final int y0, int x1, int y1) {
        for (int x = x0 + x1, y = y0 + y1; 1 <= x && x <= dx && 1 <= y && y <= dy; x += x1, y += y1) {
            if (isOccupied(x, y)) {
                return 1;
            } else if (isEmpty(x, y)) {
                return 0;
            }
        }

        return 0;
    }

    public int occupiedAround(final int x0, final int y0) {
        return  trackOccupied(x0, y0, -1, -1) +
                trackOccupied(x0, y0, 0, -1) +
                trackOccupied(x0, y0, 1, -1) +
                trackOccupied(x0, y0, -1, 0) +
                trackOccupied(x0, y0, 1, 0) +
                trackOccupied(x0, y0, -1, 1) +
                trackOccupied(x0, y0, 0, 1) +
                trackOccupied(x0, y0, 1, 1);
    }
}