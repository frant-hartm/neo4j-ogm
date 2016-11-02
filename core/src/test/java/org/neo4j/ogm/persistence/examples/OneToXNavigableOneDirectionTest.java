package org.neo4j.ogm.persistence.examples;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.ogm.domain.onetoone.Entity;
import org.neo4j.ogm.domain.onetoone.Owner;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.testutil.MultiDriverTestClass;

import static org.junit.Assert.assertEquals;

public class OneToXNavigableOneDirectionTest extends MultiDriverTestClass {

    private static Session session;
    private Entity entity;
    private Owner o1;
    private Owner o2;

    @BeforeClass
    public static void init() throws IOException {
        session = new SessionFactory("org.neo4j.ogm.domain.onetoone").openSession();
    }

    @Before
    public void setUp() throws Exception {
        session.purgeDatabase();
        entity = new Entity();
        o1 = new Owner("o1");
        o2 = new Owner("o2");

        session.save(entity);
        session.save(o1);
        session.save(o2);
    }

    @Test
    public void shouldDeleteChangedIncomingRelationship() throws Exception {
        entity.setOwner(o1);
        session.save(entity);

        assertOneRelationshipInDb();

        entity.setOwner(o2);
        session.save(entity);

        assertOneRelationshipInDb();
    }

    @Test
    public void shouldDeleteChangedIncomingRelationshipWithClearSession() throws Exception {
        entity.setOwner(o1);
        session.save(entity);

        assertOneRelationshipInDb();

        session.clear();
        entity.setOwner(o2);
        session.save(entity);

        assertOneRelationshipInDb();
    }

    @Test
    public void shouldDeleteChangedIncomingRelationshipWithClearSessionAndLoad() throws Exception {
        entity.setOwner(o1);
        session.save(entity);

        assertOneRelationshipInDb();

        session.clear();
        entity = session.load(Entity.class, entity.getId());
        entity.setOwner(o2);
        session.save(entity);

        assertOneRelationshipInDb();
    }

    private void assertOneRelationshipInDb() {
        Result result = session.query("MATCH (p:Entity)-[r]-() return count(r) as c", new HashMap<String, Object>());
        Map<String, Object> row = result.iterator().next();
        Integer count = (Integer) row.get("c");
        assertEquals(1, (int) count);
    }

}
