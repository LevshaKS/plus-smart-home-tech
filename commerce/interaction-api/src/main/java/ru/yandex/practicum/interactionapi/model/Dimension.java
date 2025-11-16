package ru.yandex.practicum.interactionapi.model;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dimension {
    Double width;
    Double height;
    Double depth;
}
