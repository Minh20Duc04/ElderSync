package com.CareGenius.book.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "care_seekers")

public class CareSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^[\\+]?[1-9][\\d]{0,15}$", message = "Phone number must be a valid international format")
    private String phoneNumber;

    @ElementCollection(targetClass = CareNeed.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "care_needs_description", joinColumns = @JoinColumn(name = "care_seeker_uid"))
    @Enumerated(value = EnumType.STRING)
    private Set<CareNeed> careNeedsDescription;

    @ElementCollection(targetClass = HealthCondition.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "health_conditions", joinColumns = @JoinColumn(name = "care_seeker_uid"))
    @Enumerated(value = EnumType.STRING)
    private Set<HealthCondition> healthConditions;

    @Enumerated(value = EnumType.STRING)
    private Gender preferredGiverGender;

    @OneToOne
    @JoinColumn(name = "user_uid")
    private User user;

    @OneToMany(mappedBy = "careSeeker", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<AIRecommendation> recommendations;

    @OneToMany(mappedBy = "careSeeker", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings;

}
