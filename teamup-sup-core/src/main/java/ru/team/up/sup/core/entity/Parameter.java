package ru.team.up.sup.core.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Сущность параметр
 */

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameter<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String parameterName;

    @Enumerated(EnumType.STRING)
    @Column
    private ParameterType parameterType;

    @Column(nullable = false)
    private String systemName;

    @Column(nullable = false)
    private String parameterValue;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column
    private LocalDateTime updateDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private User userWhoLastChangeParameters;
}
