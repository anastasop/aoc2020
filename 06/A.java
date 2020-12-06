import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class A {
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
    private Boolean[] answered = new Boolean[26];

    Group() {
	for (int i = 0; i < answered.length; i++) {
	    answered[i] = false;
	}
    }

    public void addAnswers(String answers) {
	answers.chars().forEach(c -> answered[c - 'a'] = true);
    }

    public int answeredYes() {
	return (int)Arrays.stream(answered).filter(b -> b).count();
    }
}
