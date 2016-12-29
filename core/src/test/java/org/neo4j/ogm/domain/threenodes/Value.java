package org.neo4j.ogm.domain.threenodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Value {

    @GraphId
    Long gid;

    String value;

    public Value() {
    }

    public Value(String value) {
        this.value = value;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Value{" +
               "gid=" + gid +
               ", value='" + value + '\'' +
               '}';
    }
}
