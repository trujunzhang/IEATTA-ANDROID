package org.ieatta.provide;

public enum IEAEditKey {

    Unknow("Unknow"),

    //******************************************
    // For Section title
    Section_Title ("section_title"),

    //******************************************
    // Photo Gallery
    photo_gallery ("photo_gallery"),

    // Restaurant
    rest_name ("rest_name"),
    rest_address ("rest_address"),
    rest_citystate ("rest_citystate"),

    // Event
    event_name ("event_name"),

    event_nameofserver ("event_nameofserver"),
    event_writeareview ("event_writeareview"),
    event_starttime("event_starttime"),
    event_endtime ("event_endtime"),
    event_GPS_Location ("event_GPS_Location"),

    // People
    person_name ("person_name"),
    person_address("person_address"),
    person_email ("person_email"),

    // Recipe
    recipe_name ("recipe_name"),
    recipe_price ("recipe_price"),
    add_Photo ("add_photo");

    private String name;

    IEAEditKey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return  this.name;
    }
}
