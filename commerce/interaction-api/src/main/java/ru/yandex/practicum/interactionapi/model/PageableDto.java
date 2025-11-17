package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

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
