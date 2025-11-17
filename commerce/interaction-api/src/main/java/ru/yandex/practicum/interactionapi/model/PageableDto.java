package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@AllArgsConstructor
@ToString

public class PageableDto {
    @Min(0)
    private Integer page;
    @Min(1)
    private Integer size;
    private List<String> sort;


}
