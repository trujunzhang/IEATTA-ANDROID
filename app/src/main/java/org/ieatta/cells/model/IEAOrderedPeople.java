package org.ieatta.cells.model;

import java.util.LinkedList;
import java.util.List;

public class IEAOrderedPeople {
    private final String teamUUID;
    private final String displayName;
    private final String eventUUID;

    public IEAOrderedPeople(String teamUUID, String displayName, String eventUUID) {
        this.teamUUID = teamUUID;
        this.displayName = displayName;
        this.eventUUID = eventUUID;
    }

    public String getTeamUUID() {
        return teamUUID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEventUUID() {
        return eventUUID;
    }
}
