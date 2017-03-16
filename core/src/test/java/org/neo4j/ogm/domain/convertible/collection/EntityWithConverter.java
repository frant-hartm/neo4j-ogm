package org.neo4j.ogm.domain.convertible.collection;

import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@NodeEntity
public class EntityWithConverter {

    @GraphId
    Long id;

    @Convert(CustomVOAttributeConverter.class)
    List<CustomVO> customVOs;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CustomVO> getCustomVOs() {
        return customVOs;
    }

    public void setCustomVOs(List<CustomVO> customVOs) {
        this.customVOs = customVOs;
    }
}
