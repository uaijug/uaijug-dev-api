package br.com.uaijug.uaijugdevapi.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Validated
@Entity
@Table(name = "tb_event", schema = "devs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @NotNull
    private LocalDateTime date;

    private String local;

    @Column(name = "meeting_time")
    @NotNull
    private int meetingTime;

    @Column(name = "pricipal_banner")
    private String pricipalBanner;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull(message = "Situacao do Evento deve ser definido")
    private Boolean online;

    @ElementCollection
    @CollectionTable(schema = "devs", name="tb_event_tag", joinColumns=@JoinColumn(name="event_id"))
    @Column(name="tag_id")
    private Set<Tag> tags;

}
