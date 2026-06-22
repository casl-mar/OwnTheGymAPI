package casl.mar.own_the_gym.entity;

import casl.mar.own_the_gym.entity.enums.DifficultyLevel;
import casl.mar.own_the_gym.entity.enums.WorkoutType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workouts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Workout {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    private String title;
    private String Description;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel level;

    @Enumerated(EnumType.STRING)
    private WorkoutType type;

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkSet> sets = new ArrayList<>();

    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<WorkoutDay> workoutDays = new ArrayList<>();
}
