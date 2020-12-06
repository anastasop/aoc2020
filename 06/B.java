import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class B {
    public static void main(String[] args) throws Exception {
	var groups = groups(new BufferedReader(new InputStreamReader(System.in)));
	for (var group: groups) {
	    System.out.println(group.answeredYes());
	}

	var count = groups.stream().mapToInt(g -> g.answeredYes()).sum();
        System.out.printf("count: %d%n", count);
    }

    static List<Group> groups(BufferedReader reader) throws IOException {
	var groups = new ArrayList<Group>();
	
	Group group = null;
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                if (group != null) {
                    groups.add(group);
                    group = null;
                }
            } else {
                if (group == null) {
                    group = new Group();
                }
		group.addAnswers(line);
            }
        }

        if (group != null) {
            groups.add(group);
        }

        return groups;
    }
}

class Group {
    private boolean[] answered;

    public void addAnswers(String answers) {
	boolean[] all = new boolean[26];
	answers.chars().forEach(c -> all[c - 'a'] = true);

	if (answered == null) {
	    answered = all;
	} else {
	    for (int i = 0; i < 26; i++) {
		answered[i] &= all[i];
	    }
	}
    }

    public int answeredYes() {
	int count = 0;

	if (answered != null) {
	    for (var b: answered) {
		if (b) {
		    count++;
		}
	    }
	}

	return count;
    }
}
