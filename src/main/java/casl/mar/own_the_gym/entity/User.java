package casl.mar.own_the_gym.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Calendar calendar;

    @OneToMany(mappedBy = "creator", cascade= CascadeType.ALL)
    @Builder.Default
    private List<Workout> createdWorkouts = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade= CascadeType.ALL)
    @Builder.Default
    private List<Exercise> createdExercises = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade= CascadeType.ALL)
    @Builder.Default
    private List<Challenge> createdChallenges = new ArrayList<>();
 }
