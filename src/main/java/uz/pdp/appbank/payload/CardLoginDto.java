package uz.pdp.appbank.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardLoginDto {


    @NotNull(message = "Parolni kiriting")
    private String password;

}
