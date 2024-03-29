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
import java.time.LocalDate;

@Validated
@Entity
@Table(name = "tb_associate", schema = "devs")
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
    @Column(name = "document_id")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$", message = "CPF number")
    private String documentId; //CPF


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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthdate;

    //@Size(max = 50)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotNull(message = "Situacao do certificado deve ser definido")
    private Boolean certificate;

    public void update(Long id, Associate associate) {
        this.id = id;
        this.name = associate.getName();
        this.code = associate.getCode();
        this.documentId = associate.getDocumentId();
        this.phone = associate.getPhone();
        this.email = associate.getEmail();
        this.address = associate.getAddress();
        this.birthdate = associate.getBirthdate();
        this.gender = associate.getGender();
        this.certificate = associate.getCertificate();
    }
}
