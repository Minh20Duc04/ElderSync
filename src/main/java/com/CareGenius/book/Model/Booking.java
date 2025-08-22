package com.CareGenius.book.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "bookings")

public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Carelocation careLocation; //nhớ request và trả về response

    //careLocation, fromDate, duration, start and end time, String giverUid, note, payment

    @NotNull(message = "From date is required")
    @FutureOrPresent(message = "From date must be today or in the future")
    private LocalDate fromDate; //so luong booking 1 ngay va phai theo lich cua giver

    @NotNull(message = "Duration is required")
    @Size(min = 1, message = "Must being taken care at least 1 day")
    private Integer duration;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @NotNull(message = "Start time is required")
    private LocalTime startTime; //CINIC thi theo lich GIVER

    @NotNull(message = "End time is required")
    private LocalTime endTime;  //CINIC thi theo lich GIVER

    @ManyToOne
    @JoinColumn(name = "care_seeker_uid")
    private CareSeeker careSeeker;

    @ManyToOne
    @JoinColumn(name = "care_giver_uid")
    private CareGiver careGiver;

    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String note;

    private String meetingLink; //sau khi Giver confirm moi tao

    @Enumerated(value = EnumType.STRING)
    private Payment payment;

    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Tasks> tasks;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reviews> reviews;
}
