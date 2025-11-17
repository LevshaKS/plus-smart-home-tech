package ru.yandex.practicum.interactionapi.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ProductsPageDto {
    private List<ProductDto> content;

    private List<Sort.Order> sort;
}