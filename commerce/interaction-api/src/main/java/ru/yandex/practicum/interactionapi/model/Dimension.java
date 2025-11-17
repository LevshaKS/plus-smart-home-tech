package ru.yandex.practicum.interactionapi.model;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dimension {
    private Double width;
    private Double height;
    private Double depth;
}
