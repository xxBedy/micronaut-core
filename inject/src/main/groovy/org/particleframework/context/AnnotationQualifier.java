package org.particleframework.context;

import org.particleframework.context.exceptions.NonUniqueBeanException;
import org.particleframework.inject.ComponentDefinition;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Qualifies using an annotation
 *
 * @author Graeme Rocher
 * @since 1.0
 */
class AnnotationQualifier<T> implements Qualifier<T> {

    private final Annotation qualifier;

    AnnotationQualifier(Annotation qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public ComponentDefinition<T> qualify(Class<T> beanType, Stream<ComponentDefinition<T>> candidates) throws NonUniqueBeanException {

        Stream<ComponentDefinition<T>> filtered;
        String name;
        if (qualifier instanceof Named) {
            Named named = (Named) qualifier;
            String v = named.value();
            name = Character.toUpperCase(v.charAt(0)) + v.substring(1);

        } else {
            name = qualifier.annotationType().getSimpleName();
        }

        filtered = candidates.filter(candidate -> {
                    String candidateName = candidate.getType().getSimpleName();

                    if(candidateName.equalsIgnoreCase(name)) {
                        return true;
                    }
                    else {

                        String qualified = name + beanType.getSimpleName();
                        if(qualified.equals(candidateName)) {
                            return true;
                        }
                    }
                    return false;
                }
        );

        Optional<ComponentDefinition<T>> first = filtered.findFirst();
        return first.orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnnotationQualifier<?> that = (AnnotationQualifier<?>) o;

        return qualifier.equals(that.qualifier);
    }

    @Override
    public int hashCode() {
        return qualifier.hashCode();
    }
}