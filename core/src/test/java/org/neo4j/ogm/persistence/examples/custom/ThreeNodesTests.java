package org.neo4j.ogm.persistence.examples.custom;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.neo4j.ogm.domain.threenodes.ConnectingNode;
import org.neo4j.ogm.domain.threenodes.MainNode;
import org.neo4j.ogm.domain.threenodes.Value;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.event.Event;
import org.neo4j.ogm.session.event.EventListenerAdapter;
import org.neo4j.ogm.testutil.MultiDriverTestClass;

import static org.junit.Assert.assertEquals;

public class ThreeNodesTests extends MultiDriverTestClass {

    private Session session;

    @Before
    public void init() throws IOException {
        session = new SessionFactory("org.neo4j.ogm.domain.threenodes").openSession();
        session.purgeDatabase();
    }

    @Test
    public void npeIssue305() throws Exception {
        session.register(new EventListenerAdapter() {
            @Override
            public void onPreSave(Event event) {
                System.out.println(event.getObject());
            }
        });

        MainNode m1 = new MainNode();
        Value v1 = new Value("v1");
        m1.addValue(v1);

        MainNode m2 = new MainNode();
        m2.addValue(new Value("v2"));

        ConnectingNode cn = new ConnectingNode(m1, m2);
        cn.setProperty("p1");

        session.save(cn);

        MainNode m3 = new MainNode();
        m3.addValue(new Value("v3"));
        session.save(m3);

        Map<String, Object> params = new HashMap<>();
        params.put("value", "p1");
        ConnectingNode result = session.queryForObject(ConnectingNode.class, "MATCH (cn:ConnectingNode {property:{value}}), " +
                                                                             "(from)-[]-(cn)-[]-(to) " +
                                                                             "RETURN cn, from, to", params);
        m1.getValues().clear();
        session.save(m1);
        m3.addValue(v1);
        session.save(m3);

        result.setFrom(m3);
        session.save(result);
    }

    @Test
    public void relationshipNotDeleted() throws Exception {
        MainNode m1 = new MainNode();
        MainNode m2 = new MainNode();

        ConnectingNode cn = new ConnectingNode(m1, m2);
        session.save(cn);

        MainNode m3 = new MainNode();
        session.save(m3);

        session.clear();


        cn.setTo(m3);
        session.save(cn);
        Result result = session.query("MATCH (cn:ConnectingNode)-[r]-(main) " +
                                     "RETURN count(r) as c", Collections.<String, Object>emptyMap());
        Number number = (Number) result.iterator().next().get("c");
        long count = number.longValue();
        assertEquals(2, count);
    }
}
