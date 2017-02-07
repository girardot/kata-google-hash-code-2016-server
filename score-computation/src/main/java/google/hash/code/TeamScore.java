package google.hash.code;

import google.hash.code.model.Color;
import google.hash.code.score.ScoreDrone;

import java.util.*;

public class TeamScore {

    private final String name;
    private final Map<InputType, Score> scores = new HashMap<>();
    private final List<ScoreDrone> drones = new ArrayList<>();
    private final Color color;

    public TeamScore(String name) {
        this.name = name;
        final Random random = new Random(name.hashCode());
        this.color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
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

    public Color getColor() {
        return color;
    }

    public List<ScoreDrone> getDrones() {
        return drones;
    }
}

