import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

class A {
    public static void main(String[] args) throws Exception {
	var rd = new BufferedReader(new InputStreamReader(System.in));

	var when = Integer.parseInt(rd.readLine());
	var timestamps = Arrays.stream(rd.readLine().split(","))
	    .filter(s -> !s.equals("x"))
	    .map(Integer::parseInt)
	    .collect(Collectors.toList());

	int min = Integer.MAX_VALUE;
	int id = 0;
	for (var ts: timestamps) {
	    int i = when / ts;
	    if (when % ts > 0) {
		i++;
	    }

	    int at = i * ts;
	    if (at < min) {
		min = at;
		id = ts;
	    }
	}

	System.out.printf("%d %d %d%n", id, min, id * (min - when));
    }
}
