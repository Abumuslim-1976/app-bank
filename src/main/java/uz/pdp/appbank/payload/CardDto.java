package uz.pdp.appbank.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @NotNull(message = "Kartani ichiga pul mablag`i joylansin")
    private double balance;                                                     // kartani ichidagi pul miqdori

    @NotNull(message = "kartani maxsus raqami bo`lishi kerak")
    private String specialNumber;                                               // card ning 16 xonalik maxsus raqami

    private String cvvCode;                                                     // 3 xonali cvv kodi

    @NotNull(message = "kartani maxsus kodi kiritilsin")
    private Integer specialCode;                                                 // 4 xonali maxsus kod

    @NotEmpty(message = "kartani egasi kiritilsin")
    private UUID userId;                                                        // qaysi mijozga tegishli ekanligi

}


