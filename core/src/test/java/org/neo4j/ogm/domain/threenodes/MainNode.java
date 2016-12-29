package org.neo4j.ogm.domain.threenodes;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class MainNode {

    @GraphId
    Long gid;

    @Relationship(type = "HAS")
    Set<Value> values;

    public MainNode() {
    }

    public void addValue(Value value) {
        if (values == null) {
            values = new HashSet<>();
        }
        values.add(value);
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Set<Value> getValues() {
        return values;
    }

    public void setValues(Set<Value> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "MainNode{" +
               "gid=" + gid +
               ", values=" + values +
               '}';
    }
}
