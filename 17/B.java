import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class B {
    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var cube = new Hypercube();
        int y = 0;

        for (var s = reader.readLine(); s != null; s = reader.readLine()) {
            for (int x = 0; x < s.length(); x++) {
                var c = s.charAt(x);
                if (c == '#') {
                    cube.setActive(x, y, 0, 0);
                } else {
                    cube.setInactive(x, y, 0, 0);
                }
            }
            y++;
        }

        for (int i = 0; i < 6; i++) cube.nextRound();

        System.out.println(cube.numActive());
    }
}

class Hypercube {
    private static final int D = 5;
    private static final int N = 2 << D;
    private static final int N2 = N * N;
    private static final int N3 = N * N * N;
    private static final int N4 = N * N * N * N;
    private static final int G = 2 << (D - 1);

    private static final boolean[] grid = new boolean[N4];
    private static final int[][] neighboors;
    static {
        neighboors = new int[80][4];
        int next = 0;
        var indices = new int[]{ -1, 0, 1 };
        for (var x: indices) {
            for (var y: indices) {
                for (var z: indices) {
                    for (var w: indices) {
                        if (x != 0 || y != 0 || z != 0 || w != 0) {
                            neighboors[next][0] = x;
                            neighboors[next][1] = y;
                            neighboors[next][2] = z;
                            neighboors[next][3] = w;
                            next++;
                        }
                    }
                }
            }
        }
    }

    private final List<Integer> becomeActive = new ArrayList<Integer>(N4);
    private final List<Integer> becomeInactive = new ArrayList<Integer>(N4);
    private final int[] min = new int[] {-G + 1, -G + 1, -G + 1, -G + 1};
    private final int[] max = new int[] {G - 2, G - 2, G - 2, G - 2};

    public void nextRound() {
        becomeActive.clear();
        becomeInactive.clear();

        final int x0 = min[0], x1 = max[0];
        final int y0 = min[1], y1 = max[1];
        final int z0 = min[2], z1 = max[2];
        final int w0 = min[3], w1 = max[3];

        for (int x = x0; x <= x1; x++) {
            for (int y = y0; y <= y1; y++) {
                for (int z = z0; z <= z1; z++) {
                    for (int w = w0; w <= w1; w++) {
                        var n = numActiveNeighboors(x, y, z, w);
                        if (isActive(x, y, z, w)) {
                            if (n != 2 && n != 3) becomeInactive.add(position(x, y, z, w));
                        } else {
                            if (n == 3) becomeActive.add(position(x, y, z, w));
                        }
                    }
                }
            }
        }

        becomeActive.forEach(pos -> setPosition(pos, true));
        becomeInactive.forEach(pos -> setPosition(pos, false));
    }

    private int numActiveNeighboors(int x, int y, int z, int w) {
        var count = 0;
        for (var d: neighboors) {
            if (isActive(x + d[0], y + d[1], z + d[2], w + d[3])) {
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

    private int position(int x, int y, int z, int w) {
        var pos = (w + G) * N3 + (z + G) * N2 + (y + G) * N + (x + G);
        if (pos < 0 || pos >= grid.length) {
            System.out.printf("%d %d %d %d%n", x, y, z, w);
            throw new IllegalArgumentException();
        }
        return pos;
    }

    private void setPosition(int x, int y, int z, int w, boolean b) {
        setPosition(position(x, y, z, w), b);
    }

    private void setPosition(int pos, boolean b) {
        grid[pos] = b;
    }

    private boolean getPosition(int x, int y, int z, int w) {
        return grid[position(x, y, z, w)];
    }

    public boolean isActive(int x, int y, int z, int w) {
        return getPosition(x, y, z, w);
    }

    public void setActive(int x, int y, int z, int w) {
        setPosition(x, y, z, w, true);
    }

    public void setInactive(int x, int y, int z, int w) {
        setPosition(x, y, z, w, false);
    }
}
