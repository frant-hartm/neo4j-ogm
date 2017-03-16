package org.neo4j.ogm.domain.convertible.collection;

import static java.util.Objects.requireNonNull;

public class CustomVO {

    private final String property;

    public CustomVO(String property) {
        this.property = requireNonNull(property);
    }

    public String getProperty() {
        return property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomVO customVO = (CustomVO) o;

        return property.equals(customVO.property);
    }

    @Override
    public int hashCode() {
        return property.hashCode();
    }
}
