import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class B {
    public static void main(String[] args) throws Exception {
        new BufferedReader(new InputStreamReader(System.in)).lines().forEach(line -> {
            var tile = Tile.Reference;
            int pos = 0;
            while (pos < line.length()) {
                if (line.charAt(pos) == 'e') {
                    tile = tile.east();
                    pos += 1;
                } else if (line.charAt(pos) == 'w') {
                    tile = tile.west();
                    pos += 1;
                } else if (line.charAt(pos) == 'n' && line.charAt(pos + 1) == 'w') {
                    tile = tile.northWest();
                    pos += 2;
                } else if (line.charAt(pos) == 'n' && line.charAt(pos + 1) == 'e') {
                    tile = tile.northEast();
                    pos += 2;
                } else if (line.charAt(pos) == 's' && line.charAt(pos + 1) == 'w') {
                    tile = tile.southWest();
                    pos += 2;
                } else if (line.charAt(pos) == 's' && line.charAt(pos + 1) == 'e') {
                    tile = tile.southEast();
                    pos += 2;
                } else {
                    throw new IllegalArgumentException(String.format("pos: %d char: %c", pos, line.charAt(pos)));
                }
            }
            tile.flip();
        });

        System.out.printf("Day %d: %d%n", 0, Tile.numBlackTiles());
        for (int d = 1; d <= 100; d++) {
            Tile.dailyArt();
            if (d % 10 == 0) System.out.printf("Day %d: %d%n", d, Tile.numBlackTiles());
        }
    }
}

class Tile {
    private final int x;
    private final int y;
    private int flips;
    private boolean shouldFlip;

    private final static Map<Integer, Tile> tiles = new HashMap<>();
    public final static Tile Reference = new Tile(0, 0);
    static {
        tiles.put(sig(Reference.x, Reference.y), Reference);
    }

    public Tile east() { return move(2, 0); }
    public Tile west() { return move(-2, 0); }
    public Tile southEast() { return move(1, 2); }
    public Tile southWest() { return move(-1, 2); }
    public Tile northEast() { return move(1, -2); }
    public Tile northWest() { return move(-1, -2); }

    public void flip() {
        this.flips++;
        this.east();
        this.west();
        this.northWest();
        this.northEast();
        this.southWest();
        this.southEast();
    }

    public boolean isBlack() {
        return flips % 2 == 1;
    }

    private Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tile move(final int dx, final int dy) {
        return tiles.computeIfAbsent(sig(x + dx, y + dy), k -> new Tile(x + dx, y + dy));
    }

    private static int sig(int sx, int sy) {
        return 131 * sx + sy;
    }

    public static long numBlackTiles() {
        return tiles.values().stream().filter(Tile::isBlack).count();
    }

    public static void dailyArt() {
        for (var tile: new ArrayList<>(tiles.values())) {
            var numAdjBlacks = 0;
            if (tile.east().isBlack()) numAdjBlacks++;
            if (tile.west().isBlack()) numAdjBlacks++;
            if (tile.southEast().isBlack()) numAdjBlacks++;
            if (tile.southWest().isBlack()) numAdjBlacks++;
            if (tile.northEast().isBlack()) numAdjBlacks++;
            if (tile.northWest().isBlack()) numAdjBlacks++;

            tile.shouldFlip = tile.isBlack() ? (numAdjBlacks == 0 || numAdjBlacks > 2) : numAdjBlacks == 2;
        }
        for (var tile: new ArrayList<>(tiles.values())) {
            if (tile.shouldFlip) tile.flip();
        }
    }
}
