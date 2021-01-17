package org.wahlzeit.utils.patterns;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE) // annotations should not be compiled as they serve documentation purposes
@Target(ElementType.TYPE) // annotation just for classes
@Repeatable(PatternInstances.class)
public @interface PatternInstance {

    /**
     * The design pattern in which the annotated class is involved.
     */
    Pattern pattern();

    /**
     * The role of the annotated class within this pattern instance.
     */
    PatternParticipant role();

}
