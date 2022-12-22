package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndPointHit {
    private int id;
    private String app;
    private String uri;
    private String ip;
    private String timeStamp;
}
