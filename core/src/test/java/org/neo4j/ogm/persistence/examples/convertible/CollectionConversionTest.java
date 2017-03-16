/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with
 * separate copyright notices and license terms. Your use of the source
 * code for these subcomponents is subject to the terms and
 *  conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package org.neo4j.ogm.persistence.examples.convertible;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.ogm.domain.convertible.collection.CustomVO;
import org.neo4j.ogm.domain.convertible.collection.EntityWithConverter;
import org.neo4j.ogm.domain.convertible.collection.EntityWithField;
import org.neo4j.ogm.domain.convertible.collection.EntityWithListConverter;
import org.neo4j.ogm.domain.convertible.parametrized.JsonNode;
import org.neo4j.ogm.domain.convertible.parametrized.StringMapEntity;
import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.Utils;
import org.neo4j.ogm.testutil.MultiDriverTestClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CollectionConversionTest extends MultiDriverTestClass {

    private Session session;

    @Before
    public void init() throws IOException {
        session = new SessionFactory("org.neo4j.ogm.domain.convertible.collection").openSession();
    }

    @After
    public void tearDown() {
        session.purgeDatabase();
    }

    @Test
    public void givenAttributeWithConverter_whenSaveAndLoad_thenLoadAttribute() throws Exception {
        CustomVO customVO = new CustomVO("my-custom-value");

        EntityWithField entity = new EntityWithField();
        entity.setCustomVO(customVO);

        session.save(entity);

        session.clear();

        Result result = session.query("match (e) return e", Collections.<String, Object>emptyMap());
        EntityWithField loaded = (EntityWithField) result.iterator().next().get("e");
        assertEquals(loaded.getCustomVO(), entity.getCustomVO());
    }

    @Test
    public void givenListAttributeWithConverter_whenSaveAndLoad_thenLoadAttribute() throws Exception {
        CustomVO customVO = new CustomVO("my-custom");

        EntityWithConverter entity = new EntityWithConverter();
        List<CustomVO> customVOSet = new ArrayList<>();
        customVOSet.add(customVO);
        entity.setCustomVOs(customVOSet);

        session.save(entity);

        session.clear();

        Result result = session.query("match (e) return e", Collections.<String, Object>emptyMap());
        EntityWithConverter loaded = (EntityWithConverter) result.iterator().next().get("e");
        assertTrue(loaded.getCustomVOs().contains(customVO));
    }


    @Test
    public void givenListAttributeWithListConverter_whenSaveAndLoad_thenLoadAttribute() throws Exception {
        CustomVO customVO = new CustomVO("my-custom");

        EntityWithListConverter entity = new EntityWithListConverter();
        List<CustomVO> customVOSet = new ArrayList<>();
        customVOSet.add(customVO);
        entity.setCustomVOs(customVOSet);

        session.save(entity);

        session.clear();

        Result result = session.query("match (e) return e", Collections.<String, Object>emptyMap());
        EntityWithListConverter loaded = (EntityWithListConverter) result.iterator().next().get("e");
        assertTrue(loaded.getCustomVOs().contains(customVO));
    }


    @Test
    public void convertWithListEmpty() throws Exception {
        EntityWithListConverter entity = new EntityWithListConverter();

        session.save(entity);

        session.clear();

        HashMap<String, Object> params = new HashMap<>();
        Result result = session.query("match (e) return e", params);
        EntityWithListConverter loaded = (EntityWithListConverter) result.iterator().next().get("e");
        assertNotNull(loaded);
    }
}
