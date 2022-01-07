package ru.team.up.sup.core.entity;

import lombok.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String parameterName;

    @Column(nullable = false)
    private String systemName;

    @Column(nullable = false)
    private String parameterValue;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column
    private LocalDateTime updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin userWhoLastChangeParameters;

}
