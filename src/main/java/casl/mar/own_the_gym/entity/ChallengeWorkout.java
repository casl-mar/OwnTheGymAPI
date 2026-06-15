package casl.mar.own_the_gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "challenge_workouts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeWorkout {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;
}
