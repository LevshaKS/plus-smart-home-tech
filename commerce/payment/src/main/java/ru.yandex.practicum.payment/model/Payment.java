package ru.yandex.practicum.payment.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.interactionapi.enums.PaymentState;

import java.math.BigDecimal;
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

    private BigDecimal productTotal;
    private BigDecimal deliveryTotal;
    private BigDecimal totalPayment;
    private BigDecimal feeTotal;
    @Enumerated(EnumType.STRING)
    private PaymentState status;


}
