package br.com.uaijug.uaijugdevapi.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Validated
@Entity
@Table(name = "tb_supplier", schema = "devs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Supplier implements Serializable {

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

    @NotBlank(message = "O Campo telefone não pode estar vazio!")
    @Pattern(regexp ="^\\(\\d{2}\\)[ ]\\d{5}-\\d{4}$", message = "O Campo telefone deve ter o seguinte formato (99) 99203-1938")
    @Size(max = 25)
    private String phone;
    @NotBlank(message = "O Email telefone não pode estar vazio!")
    @Email(message = "O Email tem que ter o formato correto")
    @Size(max = 100)
    private String email;

    @Size(max = 500)
    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime birthdate;

    //@Size(max = 50)
    @Enumerated(EnumType.STRING)
    @Column(name = "supplier_type")
    private SupplierType supplierType;

    @NotNull(message = "Situacao do certificado deve ser definido")
    private Boolean active;
}
