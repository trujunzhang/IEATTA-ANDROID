package org.ieatta.parse;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ParseAPI {

    public static void setup(Context context) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(context);

        // Add your initialization code here
        // Parse.initialize(context,"VCaFXiKwdCYUkY9fOe1gK4lcmvexwXPjEt6r0txq","DcqY8cnikXrRaYwteQyCX4lkN3ksrhROBXuarXpB");

        Parse.initialize(context, "O98z3RfcgTA1aO2QpbpDpY4WirJbHa49Om1uM6pg", "iNAZA6eJ0xGpC9ad4ce69On7Oe3lRmf9b4Wtic5Z");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        // Finally, cleanup the last cache.
        ParseQuery.clearAllCachedResults();
    }
}
