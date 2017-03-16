package org.neo4j.ogm.domain.convertible.collection;

import org.neo4j.ogm.typeconversion.AttributeConverter;

public class CustomVOAttributeConverter implements AttributeConverter<CustomVO, String> {

    @Override
    public String toGraphProperty(CustomVO value) {
        return value.getProperty();
    }

    @Override
    public CustomVO toEntityAttribute(String value) {
        return new CustomVO(value);
    }
}
