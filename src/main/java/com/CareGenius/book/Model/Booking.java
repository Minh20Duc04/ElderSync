package com.CareGenius.book.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    private LocalDate fromDate; //so luong booking 1 ngay va phai theo lich cua giver

    private Integer duration;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    private LocalTime startTime; //CINIC thi theo lich GIVER

    private LocalTime endTime;  //CINIC thi theo lich GIVER

    @ManyToOne
    @JoinColumn(name = "care_seeker_uid")
    private CareSeeker careSeeker;

    @ManyToOne
    @JoinColumn(name = "care_giver_uid")
    private CareGiver careGiver;

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

}
