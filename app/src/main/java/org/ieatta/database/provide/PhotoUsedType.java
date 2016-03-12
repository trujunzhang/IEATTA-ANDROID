package org.ieatta.database.provide;

/**
 * Created by djzhang on 7/16/15.
 */
public enum PhotoUsedType {
    PU_Restaurant(0), // 0
    PU_Waiter(1),
    PU_Recipe(2), // 2
    PU_People(3),
    PU_Unknow(4);// Default

    private int type;

    PhotoUsedType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static PhotoUsedType getInstance(int type) {
        switch (type) {
            case 0:
                return PU_Restaurant;
            case 1:
                return PU_Waiter;
            case 2:
                return PU_Recipe;
            case 3:
                return PU_People;
            case 4:
                return PU_Unknow;
        }
        return null;
    }


    @Override
    public String toString() {
        String[] names = {
                "Restaurant",
                "Waiter",
                "Recipe",
                "People",
                "unknow",
        };
        return names[type];
    }
}
