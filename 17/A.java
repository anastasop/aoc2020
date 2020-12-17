import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class A {
    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var cube = new Cube();
        int y = 0;

        for (var s = reader.readLine(); s != null; s = reader.readLine()) {
            for (int x = 0; x < s.length(); x++) {
                var c = s.charAt(x);
                if (c == '#') {
                    cube.setActive(x, y, 0);
                } else {
                    cube.setInactive(x, y, 0);
                }
            }
            y++;
        }

        for (int i = 0; i < 6; i++) cube.nextRound();

        System.out.println(cube.numActive());
    }
}

class Cube {
    private static final int D = 6;
    private static final int N = 2 << D;
    private static final int N2 = N * N;
    private static final int N3 = N * N * N;
    private static final int G = 2 << (D - 1);

    private static final boolean[] grid = new boolean[N3];
    private static final int[][] neighboors;
    static {
        neighboors = new int[26][3];
        int next = 0;
        var indices = new int[]{ -1, 0, 1 };
        for (var x: indices) {
            for (var y: indices) {
                for (var z: indices) {
                    if (x != 0 || y != 0 || z != 0) {
                        neighboors[next][0] = x;
                        neighboors[next][1] = y;
                        neighboors[next][2] = z;
                        next++;
                    }
                }
            }
        }
    }

    private final List<Integer> becomeActive = new ArrayList<Integer>(N3);
    private final List<Integer> becomeInactive = new ArrayList<Integer>(N3);
    private final int[] min = new int[] {-G + 1, -G + 1, -G + 1};
    private final int[] max = new int[] {G - 2, G - 2, G - 2};

    public void nextRound() {
        becomeActive.clear();
        becomeInactive.clear();

        final int x0 = min[0], x1 = max[0];
        final int y0 = min[1], y1 = max[1];
        final int z0 = min[2], z1 = max[2];

        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    var n = numActiveNeighboors(x, y, z);
                    if (isActive(x, y, z)) {
                        if (n != 2 && n != 3) becomeInactive.add(position(x, y, z));
                    } else {
                        if (n == 3) becomeActive.add(position(x, y, z));
                    }
                }
            }
        }

        becomeActive.forEach(pos -> setPosition(pos, true));
        becomeInactive.forEach(pos -> setPosition(pos, false));
    }

    private int numActiveNeighboors(int x, int y, int z) {
        var count = 0;
        for (var d: neighboors) {
            if (isActive(x + d[0], y + d[1], z + d[2])) {
                count++;
            }
        }
        return count;
    }

    public int numActive() {
        int count = 0;
        for (var b: grid) {
            if (b) {
                count++;
            }
        }
        return count;
    }

    private int position(int x, int y, int z) {
        var pos = (z + G) * N2 + (y + G) * N + (x + G);
        if (pos < 0 || pos >= grid.length) {
            System.out.printf("%d %d %d%n", x, y, z);
            throw new IllegalArgumentException();
        }
        return pos;
    }

    private void setPosition(int x, int y, int z, boolean b) {
        setPosition(position(x, y, z), b);
    }

    private void setPosition(int pos, boolean b) {
        grid[pos] = b;
    }

    private boolean getPosition(int x, int y, int z) {
        return grid[position(x, y, z)];
    }

    public boolean isActive(int x, int y, int z) {
        return getPosition(x, y, z);
    }

    public void setActive(int x, int y, int z) {
        setPosition(x, y, z, true);
    }

    public void setInactive(int x, int y, int z) {
        setPosition(x, y, z, false);
    }
}
