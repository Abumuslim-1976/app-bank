package uz.pdp.appbank.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull(message = "username bo`sh bo`lishi mumkin emas")
    private String username;

    @NotNull(message = "parol bo`sh bo`lishi mumkin emas")
    private String password;

}
