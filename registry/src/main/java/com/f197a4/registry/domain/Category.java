package com.f197a4.registry.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Table(name="categories")
@Entity
@Getter @Setter
@RequiredArgsConstructor @AllArgsConstructor @NoArgsConstructor
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!this.getClass().equals(obj.getClass())) {
            return false;
        }
        Category obj2 = (Category) obj;
        return name.equals(obj2.name) && id.equals(obj2.id);
    }

    /*@Override
    public int hashCode() {
        return (id+name).hashCode();
    }*/

    @Override
    public String toString() {
        return id + " " + name;
    }

}
