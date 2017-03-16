package org.neo4j.ogm.domain.convertible.collection;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@NodeEntity
public class EntityWithField {

    @GraphId
    Long id;

    @Convert(CustomVOAttributeConverter.class)
    CustomVO customVO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomVO getCustomVO() {
        return customVO;
    }

    public void setCustomVO(CustomVO customVO) {
        this.customVO = customVO;
    }
}
