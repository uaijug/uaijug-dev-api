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
@Table(name = "tb_course", schema = "devs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O titulo nao pode ser vazio")
    @Size(max = 255, message = "Titulo deve ter no maximo de 255 caracteres")
    private String title;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") //2022-07-13T11:42
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull(message = "A data inicial nao pode ser vazio")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull(message = "A data final nao pode ser vazio")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @NotNull(message = "A Carga Horaria nao pode ser vazio")
    private int workload;

    @NotNull(message = "Situacao do certificado deve ser definido")
    private Boolean certificate;
}
