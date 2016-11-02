package org.neo4j.ogm.domain.onetoone;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Entity {

    private Long id;
    private String name;

    // INCOMING - Also needs to be on getter/setter
    @Relationship(type = "OWNER", direction = "INCOMING")
    private Owner owner;

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

    @Relationship(type = "OWNER", direction = "INCOMING")
    public Owner getOwner() {
        return owner;
    }

    @Relationship(type = "OWNER", direction = "INCOMING")
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
