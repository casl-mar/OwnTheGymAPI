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
@Table(name = "calendars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique =true)
    private User user;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
    @Builder.Default
    private List<WorkoutDay> workoutDays = new ArrayList<>();
}
