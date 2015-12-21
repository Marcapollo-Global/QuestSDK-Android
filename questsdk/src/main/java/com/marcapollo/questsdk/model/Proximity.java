package com.marcapollo.questsdk.model;

import java.util.Comparator;

/**
 * Created by shinechen on 12/21/15.
 */
public enum  Proximity implements Comparable<Proximity> {
    IMMEDIATE(1),
    NEAR(2),
    FAR(3),
    UNKNOWN(Integer.MAX_VALUE);

    private final int level;
    Proximity(int level) {
        this.level = level;
    }

    public static Comparator<Proximity> levelCompatator = new Comparator<Proximity>() {
        @Override
        public int compare(Proximity lhs, Proximity rhs) {
            return lhs.level - rhs.level;
        }
    };
}
