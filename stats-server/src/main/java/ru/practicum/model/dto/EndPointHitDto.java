package ru.practicum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndPointHitDto {
    private int id;
    private String app;
    private String uri;
    private String ip;
    private String timeStamp;
}