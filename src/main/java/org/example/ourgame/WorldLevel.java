package org.example.ourgame;

import java.io.Serializable;
import java.util.*;

// antud klass on mõeldut tasemete moodustamiseks, ning nende töötlemiseks
public class WorldLevel implements Serializable {
    //iga maailma jaoks on omad karbid, boonused ning klounid (isiklik HashMap)

    private static final long serialVersionUID = 1L;
    private int level; // maailma tase
    private int minClownLevel; // minimaalne kluni tase, mis kuulub selle maailmale
    private int maxClownLevel; // maksimaalne klouni tase, mis kuulun selle maailmale
    private String backgroundImage;
    // iga maailma tagaplaan
    private HashMap<Integer, ClownsClass> clowns;
    // maailma klounid

    // konstruktor
    public WorldLevel(int level, int minClownLevel, String backgroundImage) {
        this.level = level;
        this.minClownLevel = minClownLevel;
        this.maxClownLevel = minClownLevel + 5;
        this.backgroundImage = backgroundImage;
        this.clowns = new HashMap<>();
    }

    // getterid
    public int getLevel() {
        return level;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public HashMap<Integer, ClownsClass> getClowns() {
        return clowns;
    }

    public int getMinClownLevel() {
        return minClownLevel;
    }

    public int getMaxClownLevel() {
        return maxClownLevel;
    }

}