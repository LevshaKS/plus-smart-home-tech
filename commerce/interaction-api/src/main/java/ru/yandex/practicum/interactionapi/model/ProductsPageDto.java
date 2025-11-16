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
    List<ProductDto> content;

    List<Sort.Order> sort;
}