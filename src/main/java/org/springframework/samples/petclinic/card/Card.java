package org.springframework.samples.petclinic.card;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.samples.petclinic.model.BaseEntity;


/**
 * @author Jose Maria Delgado Sanchez
 */
@Entity
@Table(name = "cards")
public class Card extends BaseEntity {
    
    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "cost")
    @NotEmpty
    private Integer cost;

    @Column(name = "type")
    @NotEmpty
    private Integer type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Card [cost=" + cost + ", name=" + name + ", type=" + type + "]";
    }

    
}
