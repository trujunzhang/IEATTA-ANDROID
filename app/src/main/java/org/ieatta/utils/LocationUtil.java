package org.ieatta.utils;

import android.location.Location;

public class LocationUtil {

    /**
     * ['dr5ru0r0y4xj','dr5ru1nwsk1v','dr5ru22mf339','dr5ru0r24xuf','dr5ru22mf339','dr5ru0nzfv5q','wtv8r4whgz2y']
     * @return
     */
    public static Location getLocation() {
        double[][] data = {
                // 'dr5ru0r0y4xj'
                // {40.738821,-73.994026},
                // 'dr5ru0rb5cej' (Region test)
                // matched: ['dr5ru0r0y4xj','dr5ru0r24xuf','dr5ru0nzfv5q']
                {40.738687, -73.993098},
        };
        Location location = new Location("");
        location.setLatitude(data[0][0]);
        location.setLongitude(data[0][1]);

        return location;
    }
}
