package com.CareGenius.book.Model;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@Table(name = "care_givers")

public class CareGiver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uid;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[\\+]?[1-9][0-9]{7,14}$", message = "Phone number must be a valid format")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_uid")
    private User user;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Years of experience cannot be negative")
    @Max(value = 60, message = "Years of experience cannot exceed 60")
    private Integer yearExperience;

    @NotBlank(message = "Bio is required")
    @Size(min = 10, max = 500, message = "Bio must be between 10 and 500 characters")
    private String bio;

    @NotNull(message = "Fee is required")
    private Double fee;

    private String imageUrl;

    @OneToMany(mappedBy = "careGiver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("skills-reference")
    private Set<CareGiverSkill> skills;

    @OneToMany(mappedBy = "careGiver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("certifications-reference")
    private Set<Certification> certifications;


    @OneToOne(mappedBy = "careGiver", cascade = CascadeType.ALL)
    @JsonManagedReference("schedule-reference")
    private Schedule schedule;

    @OneToMany(mappedBy = "careGiver", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("recommendations-reference")
    private List<AIRecommendation> recommendations;

    @OneToMany(mappedBy = "careGiver", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings;

}
