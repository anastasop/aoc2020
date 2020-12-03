import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class B {
    public static void main(String[] args) {
        var board = new BufferedReader(new InputStreamReader(System.in)).lines().map(line -> {
            return line.strip().toCharArray();
        }).toArray(char[][]::new);

        int dx = 0, dy = 0, treeCount = 0;
        List<Integer> treeCounts = new ArrayList<>(5);

        dx = 1; dy = 1; treeCount = countTrees(board, dx, dy); treeCounts.add(treeCount);
        System.out.printf("R: %d D: %d T: %d%n", dx, dy, treeCount);

        dx = 3; dy = 1; treeCount = countTrees(board, dx, dy); treeCounts.add(treeCount);
        System.out.printf("R: %d D: %d T: %d%n", dx, dy, treeCount);

        dx = 5; dy = 1; treeCount = countTrees(board, dx, dy); treeCounts.add(treeCount);
        System.out.printf("R: %d D: %d T: %d%n", dx, dy, treeCount);

        dx = 7; dy = 1; treeCount = countTrees(board, dx, dy); treeCounts.add(treeCount);
        System.out.printf("R: %d D: %d T: %d%n", dx, dy, treeCount);

        dx = 1; dy = 2; treeCount = countTrees(board, dx, dy);; treeCounts.add(treeCount);
        System.out.printf("R: %d D: %d T: %d%n", dx, dy, treeCount);

        long product = 1;
        for (var count: treeCounts) {
            product *= count;
        }

        System.out.printf("M: %d%n", product);
    }

    public static int countTrees(char[][] board, int dx, int dy) {
        int x = 0, y = 0;
        int treeCount = 0;

        while (y < board.length) {
            if (board[y][x] == '#') {
                treeCount++;
            }
            y += dy;
            x = (x + dx) % board[0].length;
        }

        return treeCount;
    }
}
