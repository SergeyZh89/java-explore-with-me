package ru.practicum.request.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.RequestState;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requests")
@Builder(toBuilder = true)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    LocalDateTime created = LocalDateTime.now();

    @Positive
    @Column(name = "event_id")
    private long event;

    @Positive
    @Column(name = "requester_id")
    private long requester;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private RequestState status = RequestState.PENDING;
}
