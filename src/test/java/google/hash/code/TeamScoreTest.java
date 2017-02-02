package google.hash.code;

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
}