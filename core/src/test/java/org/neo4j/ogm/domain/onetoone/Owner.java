package org.neo4j.ogm.domain.onetoone;

import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Owner {

    private Long id;
    public String name;

    public Owner() {
    }

    public Owner(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
