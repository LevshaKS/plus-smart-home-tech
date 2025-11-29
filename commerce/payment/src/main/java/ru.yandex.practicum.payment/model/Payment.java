package ru.yandex.practicum.payment.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.interactionapi.enums.PaymentState;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    private UUID orderId;

    private double productTotal;
    private double deliveryTotal;
    private double totalPayment;
    private double feeTotal;
    @Enumerated(EnumType.STRING)
    private PaymentState status;


}
