package uz.pdp.appbank.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDto {

    @NotNull
    private int amount;

    @NotNull
    private UUID bankomatId;

    @NotNull
    private UUID cardId;

    @NotNull
    private Integer specialCode;

}
