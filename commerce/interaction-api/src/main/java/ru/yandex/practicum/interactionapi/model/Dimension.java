package ru.yandex.practicum.interactionapi.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
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
