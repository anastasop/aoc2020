import java.io.BufferedReader;
import java.io.InputStreamReader;

public class A {
    public static void main(String[] args) {
        var board = new BufferedReader(new InputStreamReader(System.in)).lines().map(line -> {
           return line.strip().toCharArray();
        }).toArray(char[][]::new);

        int x = 0, y = 0;
        int treeCount = 0;
        
        while (y < board.length) {
            if (board[y][x] == '#') {
                treeCount++;
            }
            y++;
            x = (x + 3) % board[0].length;
        }
        System.out.printf("Tree encountered %d%n", treeCount);
    }
}
