package ru.yandex.practicum.interactionapi.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressDto {
    private String country;
    private String city;
    private String street;
    private String house;
    private String flat;
}
