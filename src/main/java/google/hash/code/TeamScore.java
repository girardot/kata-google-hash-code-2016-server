package google.hash.code;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TeamScore {

    private final String name;
    private final Map<InputType, Score> scores = new HashMap<>();

    public TeamScore(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Collection<Score> getScores() {
        return scores.values();
    }

    public void addScore(Score score) {
        scores.put(score.getInputType(), score);
    }

    public Integer getTeamScore() {
        return scores.values().stream().mapToInt(Score::getValue).sum();
    }
}

