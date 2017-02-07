package google.hash.code;

import google.hash.code.model.Color;
import org.junit.Test;

import static google.hash.code.InputType.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TeamScoreTest {

    @Test
    public void get_team_score() {
        // Given
        final TeamScore teamScore = new TeamScore("Team Name");
        teamScore.addScore(new Score(SIMPLE, 195));
        teamScore.addScore(new Score(BUSY_DAY, 11));
        teamScore.addScore(new Score(MOTHER_OF_ALL_WAREHOUSES, 45));
        teamScore.addScore(new Score(REDUNDANCY, 55));

        // When
        final Integer score = teamScore.getTeamScore();

        //Then
        assertThat(score).isEqualTo(306);
    }

    @Test
    public void generate_color_from_name() {
        // Given
        final TeamScore teamScore = new TeamScore("My team");

        // When
        final Color color = teamScore.getColor();

        //Then
        assertThat(color.toString()).isEqualTo("#e6b80a");
    }
}