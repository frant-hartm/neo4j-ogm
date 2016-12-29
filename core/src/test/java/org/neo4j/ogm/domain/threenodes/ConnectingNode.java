package org.neo4j.ogm.domain.threenodes;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class ConnectingNode {

    @GraphId
    Long gid;

    MainNode from;

    MainNode to;

    String property;

    public ConnectingNode() {
    }

    public ConnectingNode(MainNode from, MainNode to) {
        this.from = from;
        this.to = to;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }


    @Relationship(type = "CONNECTED", direction = Relationship.INCOMING)
    public MainNode getFrom() {
        return from;
    }


    @Relationship(type = "CONNECTED", direction = Relationship.INCOMING)
    public void setFrom(MainNode from) {
        this.from = from;
    }


    @Relationship(type = "CONNECTED", direction = Relationship.OUTGOING)
    public MainNode getTo() {
        return to;
    }


    @Relationship(type = "CONNECTED", direction = Relationship.OUTGOING)
    public void setTo(MainNode to) {
        this.to = to;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "ConnectingNode{" +
               "gid=" + gid +
               ", from=" + from +
               ", to=" + to +
               '}';
    }
}
