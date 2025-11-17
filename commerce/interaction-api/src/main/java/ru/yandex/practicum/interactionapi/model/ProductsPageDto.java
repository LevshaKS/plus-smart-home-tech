package ru.yandex.practicum.interactionapi.model;

import lombok.*;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@ToString

@NoArgsConstructor
@AllArgsConstructor
public class ProductsPageDto {
    private List<ProductDto> content;

    private List<Sort.Order> sort;
}