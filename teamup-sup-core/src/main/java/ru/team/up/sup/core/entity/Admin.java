package ru.team.up.sup.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * Сущность администратор
 */
@Entity
@Getter
@Setter
@SuperBuilder
@Table(name = "ADMIN_ACCOUNT")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Admin extends Account { // под снос!

    public Admin() {
        super();
    }
}
