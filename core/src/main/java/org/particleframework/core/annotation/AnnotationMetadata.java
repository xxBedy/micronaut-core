/*
 * Copyright 2017 original authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.particleframework.core.annotation;

import org.particleframework.core.convert.value.ConvertibleValues;
import org.particleframework.core.value.OptionalValues;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * <p>An interface implemented at compile time by Particle that allows the inspection of annotation metadata and stereotypes</p>
 *
 * <p>This interface exposes fast and efficient means to expose annotation data at runtime without requiring reflective tricks to read
 * the annotation metadata</p>
 *
 * <p>Users of Particle should in general avoid the methods of the {@link java.lang.reflect.AnnotatedElement} interface and use this interface instead to obtain maximum efficiency</p>
 *
 * <p>Core framework types such as <tt>org.particleframework.inject.BeanDefinition</tt> and <tt>org.particleframework.inject.ExecutableMethod</tt> implement this interface</p>
 *
 * @author Graeme Rocher
 * @since 1.0
 */
public interface AnnotationMetadata {
    /**
     * A constant for representing empty metadata
     */
    AnnotationMetadata EMPTY_METADATA = new EmptyAnnotationMetadata();

    /**
     * The default <tt>value()</tt> member
     */
    String VALUE_MEMBER = "value";

    /**
     * Checks whether this object has the given annotation directly declared on the object
     *
     * @param annotation The annotation
     *
     * @return True if the annotation is present
     */
    boolean hasDeclaredAnnotation(@Nullable String annotation);

    /**
     * Checks whether this object has the given annotation on the object itself or inherited from a parent
     *
     * @param annotation The annotation
     *
     * @return True if the annotation is present
     */
    boolean hasAnnotation(@Nullable String annotation);


    /**
     * <p>Checks whether this object has the given annotation stereotype on the object itself or inherited from a parent</p>
     *
     * <p>An annotation stereotype is a meta annotation potentially applied to another annotation</p>
     *
     * @param annotation The annotation
     *
     * @return True if the annotation is present
     */
    boolean hasStereotype(@Nullable String annotation);

    /**
     * <p>Checks whether this object has the given annotation stereotype on the object itself and not inherited from a parent</p>
     *
     * <p>An annotation stereotype is a meta annotation potentially applied to another annotation</p>
     *
     * @param annotation The annotation
     *
     * @return True if the annotation is present
     */
    boolean hasDeclaredStereotype(@Nullable String annotation);

    /**
     * Resolve all of the annotation names that feature the given stereotype
     * @param stereotype The annotation names
     * @return A set of annotation names
     */
    Set<String> getAnnotationsByStereotype(String stereotype);
    /**
     * Get all of the values for the given annotation
     *
     * @param annotation The annotation name
     * @return A {@link ConvertibleValues} instance
     */
    ConvertibleValues<Object> getValues(String annotation);

    /**
     * Get all of the values for the given annotation and type of the underlying values
     *
     * @param annotation The annotation name
     * @return The {@link OptionalValues}
     */
    <T> OptionalValues<T> getValues(String annotation, Class<T> valueType);

    /**
     * Find the first annotation name for the given stereotype
     * @param stereotype The stereotype
     * @return The annotation name
     */
    default Optional<String> getAnnotationByStereotype(String stereotype) {
        return getAnnotationsByStereotype(stereotype).stream().findFirst();
    }

    /**
     * Find the first annotation name for the given stereotype
     * @param stereotype The stereotype
     * @return The annotation name
     */
    default Optional<String> getAnnotationByStereotype(Class<? extends Annotation> stereotype) {
        return getAnnotationByStereotype(stereotype.getName());
    }
    /**
     * Get all of the values for the given annotation
     *
     * @param annotation The annotation name
     * @return The {@link ConvertibleValues}
     */
    default <T> OptionalValues<T> getValues(Class<? extends Annotation> annotation, Class<T> valueType) {
        return getValues(annotation.getName(), valueType);
    }

    /**
     * Resolve all of the annotation names that feature the given stereotype
     * @param stereotype The annotation names
     * @return A set of annotation names
     */
    default Set<String> getAnnotationsByStereotype(Class<? extends Annotation> stereotype) {
        return getAnnotationsByStereotype(stereotype.getName());
    }
    /**
     * Get all of the values for the given annotation
     *
     * @param annotation The annotation name
     * @return The {@link ConvertibleValues}
     */
    default ConvertibleValues<Object> getValues(Class<? extends Annotation> annotation) {
        return getValues(annotation.getName());
    }

    /**
     * Get the value of the given annotation member
     *
     * @param annotation The annotation class
     * @param member The annotation member
     * @param requiredType The required type
     * @param <T> The value
     * @return An {@link Optional} of the value
     */
    default <T> Optional<T> getValue(String annotation, String member, Class<T> requiredType) {
        return getValues(annotation).get(member, requiredType);
    }

    /**
     * The value as an {@link OptionalLong} for the given annotation and member
     * @param annotation The annotation
     * @param member The member
     * @return THe {@link OptionalLong} value
     */
    default OptionalLong longValue(String annotation, String member) {
        Optional<Long> result = getValues(annotation).get(member, Long.class);
        return result.map(OptionalLong::of).orElseGet(OptionalLong::empty);
    }

    /**
     * The value as an {@link OptionalInt} for the given annotation and member
     * @param annotation The annotation
     * @param member The member
     * @return THe {@link OptionalInt} value
     */
    default OptionalInt intValue(String annotation, String member) {
        Optional<Integer> result = getValues(annotation).get(member, Integer.class);
        return result.map(OptionalInt::of).orElseGet(OptionalInt::empty);
    }

    /**
     * The value as an {@link OptionalDouble} for the given annotation and member
     * @param annotation The annotation
     * @param member The member
     * @return THe {@link OptionalDouble} value
     */
    default OptionalDouble doubleValue(String annotation, String member) {
        Optional<Double> result = getValues(annotation).get(member, Double.class);
        return result.map(OptionalDouble::of).orElseGet(OptionalDouble::empty);
    }
    /**
     * Get the value of default "value" the given annotation
     *
     * @param annotation The annotation class
     * @param requiredType The required type
     * @param <T> The value
     * @return An {@link Optional} of the value
     */
    default <T> Optional<T> getValue(String annotation, Class<T> requiredType) {
        return getValue(annotation, VALUE_MEMBER, requiredType);
    }

    /**
     * Get the value of the given annotation member
     *
     * @param annotation The annotation class
     * @param member The annotation member
     * @return An {@link Optional} of the value
     */
    default Optional<Object> getValue(String annotation, String member) {
        return getValue(annotation, member, Object.class);
    }

    /**
     * Get the value of the given annotation member
     *
     * @param annotation The annotation class
     * @param member The annotation member
     * @return An {@link Optional} of the value
     */
    default Optional<Object> getValue(Class<? extends Annotation> annotation, String member) {
        return getValue(annotation, member, Object.class);
    }


    /**
     * Returns whether the value of the given member is <em>true</em>
     *
     * @param annotation The annotation class
     * @param member The annotation member
     * @return True if the value is true
     */
    default boolean isTrue(String annotation, String member) {
        return getValue(annotation, member, Boolean.class).orElse(false);
    }

    /**
     * Returns whether the value of the given member is <em>true</em>
     *
     * @param annotation The annotation class
     * @param member The annotation member
     * @return True if the value is true
     */
    default boolean isTrue(Class<? extends Annotation> annotation, String member) {
        return getValue(annotation.getName(), member, Boolean.class).orElse(false);
    }

    /**
     * Returns whether the value of the given member is <em>true</em>
     *
     * @param annotation The annotation class
     * @param member The annotation member
     * @return True if the value is true
     */
    default boolean isFalse(Class<? extends Annotation> annotation, String member) {
        return !isTrue(annotation, member);
    }

    /**
     * Returns whether the value of the given member is <em>true</em>
     *
     * @param annotation The annotation class
     * @param member The annotation member
     * @return True if the value is true
     */
    default boolean isFalse(String annotation, String member) {
        return !isTrue(annotation, member);
    }
    /**
     * Get the value of default "value" the given annotation
     *
     * @param annotation The annotation class
     * @return An {@link Optional} of the value
     */
    default Optional<Object> getValue(String annotation) {
        return getValue(annotation, Object.class);
    }

    /**
     * Get the value of default "value" the given annotation
     *
     * @param annotation The annotation class
     * @return An {@link Optional} of the value
     */
    default Optional<Object> getValue(Class<? extends Annotation> annotation) {
        return getValue(annotation.getName(), Object.class);
    }

    /**
     * Get the value of default "value" the given annotation
     *
     * @param annotation The annotation class
     * @return An {@link Optional} of the value
     */
    default <T> Optional<T> getValue(Class<? extends Annotation> annotation, Class<T> requiredType) {
        return getValue(annotation.getName(), requiredType);
    }

    /**
     * Get the value of the given annotation member
     *
     * @param annotation The annotation class
     * @param member The annotation member
     * @param requiredType The required type
     * @param <T> The value
     * @return An {@link Optional} of the value
     */
    default <T> Optional<T> getValue(Class<? extends Annotation> annotation, String member, Class<T> requiredType) {
        return getValue(annotation.getName(), member, requiredType);
    }

    /**
     * Checks whether this object has the given annotation on the object itself or inherited from a parent
     *
     * @param annotation The annotation
     *
     * @return True if the annotation is present
     */
    default boolean hasAnnotation(@Nullable Class<? extends Annotation> annotation) {
        return annotation != null && hasAnnotation(annotation.getName());
    }

    /**
     * <p>Checks whether this object has the given annotation stereotype on the object itself or inherited from a parent</p>
     *
     * <p>An annotation stereotype is a meta annotation potentially applied to another annotation</p>
     *
     * @param annotation The annotation
     *
     * @return True if the annotation is present
     */
    default boolean hasStereotype(@Nullable Class<? extends Annotation> annotation) {
        return annotation != null && hasStereotype(annotation.getName());
    }

    /**
     * Check whether any of the given stereotypes is present
     * @param annotations The annotations
     * @return True if any of the given stereotypes are present
     */
    @SuppressWarnings("unchecked")
    default boolean hasStereotype(Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            if(hasStereotype(annotation)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks whether this object has the given annotation directly declared on the object
     *
     * @param annotation The annotation
     *
     * @return True if the annotation is present
     */
    default boolean hasDeclaredAnnotation(@Nullable Class<? extends Annotation> annotation) {
        return annotation != null && hasDeclaredAnnotation(annotation.getName());
    }

    /**
     * Checks whether this object has the given stereotype directly declared on the object
     *
     * @param stereotype The annotation
     *
     * @return True if the annotation is present
     */
    default boolean hasDeclaredStereotype(@Nullable Class<? extends Annotation> stereotype) {
        return stereotype != null && hasDeclaredStereotype(stereotype.getName());
    }
}