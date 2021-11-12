package org.springframework.samples.petclinic.board;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Ricardo Nadal Garcia
 */

@Entity
@Table(name="boards")
public class Board extends BaseEntity{

    @Setter
    @Getter
    @Enumerated(value=EnumType.ORDINAL)
    @Column(name="tokyo_city_status")
    private LocationStatus tokyoCityStatus;

    @Setter
    @Getter
    @Enumerated(value=EnumType.ORDINAL)
    @Column(name="tokyo_bay_status")
    private LocationStatus tokyoBayStatus;
}
