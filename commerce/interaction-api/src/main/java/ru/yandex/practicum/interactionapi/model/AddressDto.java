package ru.yandex.practicum.interactionapi.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}
