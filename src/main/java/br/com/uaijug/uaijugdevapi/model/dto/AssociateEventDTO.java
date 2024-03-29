package br.com.uaijug.uaijugdevapi.model.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AssociateEventDTO implements Serializable {

    private Long id;


    @NotNull(message = "Event can not be null!!")
    @NotEmpty(message = "Event can not be empty!!")
    private Long eventId;

    private String eventName;

    @Size(max = 100)
    private String name;

    @NotNull(message = "Code can not be null!!")
    @NotEmpty(message = "Code can not be empty!!")
    @Size(max = 100)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String documentId;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number")
    @Size(max = 25)
    private String phone;

    @NotNull(message = "e-mail can not be null!!")
    @NotEmpty(message = "e-mail can not be empty!!")
    @Email(message = "Email Address")
    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String address;

}
