package ru.practicum.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ViewStats {
    private String app;
    private String uri;
    private long hits;
}
