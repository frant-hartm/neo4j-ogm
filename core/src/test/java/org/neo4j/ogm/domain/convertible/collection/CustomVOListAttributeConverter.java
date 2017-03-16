package org.neo4j.ogm.domain.convertible.collection;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.typeconversion.AttributeConverter;

public class CustomVOListAttributeConverter implements AttributeConverter<List<CustomVO>, String[]> {

    @Override
    public String[] toGraphProperty(List<CustomVO> values) {
        if (values != null) {
            String[] result = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                result[i] = values.get(i).getProperty();
            }
            return result;
        } else {
            return null;
        }
    }

    @Override
    public List<CustomVO> toEntityAttribute(String[] values) {
        ArrayList<CustomVO> customVOs = new ArrayList<>();
        for (String value : values) {
            customVOs.add(new CustomVO(value));
        }
        return customVOs;
    }
}
