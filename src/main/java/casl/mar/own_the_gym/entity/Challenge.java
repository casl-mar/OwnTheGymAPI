package casl.mar.own_the_gym.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "challenges")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    private String title;
    private String description;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ChallengeWorkout> challengeWorkouts = new ArrayList<>();
}
