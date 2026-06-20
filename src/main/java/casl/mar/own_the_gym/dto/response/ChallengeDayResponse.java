package casl.mar.own_the_gym.dto.response;

import casl.mar.own_the_gym.entity.ChallengeDay;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "day", "challenge"})
public class ChallengeDayResponse {

    private UUID id;
    private LocalDateTime day;
    private ChallengeResponse challenge;

    public static ChallengeDayResponse from(ChallengeDay challengeDay) {
        return ChallengeDayResponse.builder()
                .id(challengeDay.getId())
                .day(challengeDay.getDay())
                .challenge(ChallengeResponse.from(challengeDay.getChallenge()))
                .build();
    }
}
