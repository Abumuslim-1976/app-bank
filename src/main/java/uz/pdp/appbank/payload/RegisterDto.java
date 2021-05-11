package uz.pdp.appbank.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {

    @NotNull(message = "first name bo`sh bo`lishi mumkin emas")
    private String firstName;

    @NotNull(message = "last name bo`sh bo`lishi mumkin emas")
    private String lastName;

    @NotNull(message = "password bo`sh bo`lishi mumkin emas")
    private String password;

    @Email
    @NotNull(message = "email bo`sh bo`lishi mumkin emas")
    private String email;

}
