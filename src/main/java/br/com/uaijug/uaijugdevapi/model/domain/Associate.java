package br.com.uaijug.uaijugdevapi.model.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Validated
@Entity
@Table(name = "tb_developer", schema = "devs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Associate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 100)
    private String code;

    @NotBlank
    @Size(max = 100)
    private String documentId;

    @Pattern(regexp ="^\\+?[0-9. ()-]{7,25}$", message = "Phone number")
    @Size(max = 25)
    private String phone;

    @Email(message = "Email Address")
    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String address;

    @NotNull(message = "Situacao do certificado deve ser definido")
    private Boolean certificate;
}
