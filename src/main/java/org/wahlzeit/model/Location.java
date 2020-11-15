package org.wahlzeit.model;

/**
 * This class represents a location in the real world.
 */
public class Location {

    public Coordinate coordinate;

    /**
     * Creates a default location instance where its coordinate are initialized with zeros.
     * @methodtype constructor
     */
    public Location() {
        this.coordinate = new Coordinate();
    }

    /**
     * Creates a location instance where the provided coordinate is used for initialization.
     * @param coordinate the coordinate of the location to be created
     * @methodtype constructor
     */
    public Location(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

}
