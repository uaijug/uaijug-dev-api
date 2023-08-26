package br.com.uaijug.uaijugdevapi.model.dto;

import lombok.*;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AssociateDrawDTO {
    private Long id;

    @NotNull(message = "Event can not be null!!")
    @NotEmpty(message = "Event can not be empty!!")
    private Long eventId;

    private int total;
}
