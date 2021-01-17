package org.wahlzeit.utils.patterns;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Needed for being able to repeat {@link PatternInstance} occurrences.
 */
@Retention(RetentionPolicy.SOURCE) // annotations should not be compiled as they serve documentation purposes
@Target(ElementType.TYPE) // annotation just for classes
public @interface PatternInstances {

    PatternInstance[] value();
}
