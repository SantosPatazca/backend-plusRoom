package com.ertedemo.domain.model.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="roomie_preferences")
public class RoomiePreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ElementCollection
    @CollectionTable(name = "preferences", joinColumns = @JoinColumn(name = "roomie_preference_id"))
    @Column(name = "preference")
    private List<String> preferences;

    @ElementCollection
    @CollectionTable(name = "hobbies", joinColumns = @JoinColumn(name = "roomie_preference_id"))
    @Column(name = "hobby")
    private List<String> hobbies;

    @NotNull
    private String locationPreference;

    @NotNull
    private Float budget;

    @NotNull
    private String genderPreference;

    @NotNull
    private Integer minAge;

    @NotNull
    private Integer maxAge;

    @NotNull
    private Boolean petFriendly;

    @NotNull
    private Boolean smokingPreference;

    @NotNull
    private String cleaningHabits;

    @NotNull
    private String sleepingHabits;
}