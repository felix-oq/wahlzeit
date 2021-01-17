package org.wahlzeit.utils.patterns;

public enum Pattern {

    Singleton("Singleton",
            PatternParticipant.Singleton),

    TemplateMethod("Template Method",
            PatternParticipant.AbstractClass, PatternParticipant.ConcreteClass),

    Decorator("Decorator",
            PatternParticipant.Component, PatternParticipant.ConcreteComponent, PatternParticipant.Decorator, PatternParticipant.ConcreteDecorator),

    ObjectPool("Object Pool",
            PatternParticipant.ReusablePool, PatternParticipant.Reusable),

    NullObject("Null Object",
            PatternParticipant.NullObject);

    private final String name;
    private final PatternParticipant[] participants;

    Pattern(String name, PatternParticipant... participants) {
        this.name = name;
        this.participants = participants;
    }

    public String getName() {
        return name;
    }

    public PatternParticipant[] getParticipants() {
        return participants.clone();
    }
}
