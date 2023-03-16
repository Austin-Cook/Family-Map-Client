package com.example.familymapclient.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class Settings {
    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fathersSide;
    private boolean motherSide;
    private boolean maleEvents;
    private boolean femaleEvents;

    public Settings() {
        this.lifeStoryLines = true;
        this.familyTreeLines = true;
        this.spouseLines = true;
        this.fathersSide = true;
        this.motherSide = true;
        this.maleEvents = true;
        this.femaleEvents = true;
    }

    public boolean isLifeStoryLines() {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines() {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public boolean isFathersSide() {
        return fathersSide;
    }

    public void setFathersSide(boolean fathersSide) {
        this.fathersSide = fathersSide;
    }

    public boolean isMotherSide() {
        return motherSide;
    }

    public void setMotherSide(boolean motherSide) {
        this.motherSide = motherSide;
    }

    public boolean isMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (lifeStoryLines) {
            hash = hash * 3;
        }
        if (familyTreeLines) {
            hash = hash * 3;
        }
        if (spouseLines) {
            hash = hash * 3;
        }
        if (fathersSide) {
            hash = hash * 3;
        }
        if (motherSide) {
            hash = hash * 3;
        }
        if (maleEvents) {
            hash = hash * 3;
        }
        if (femaleEvents) {
            hash = hash * 3;
        }

        return hash;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Settings r = (Settings) obj;
        return (isLifeStoryLines() == r.isLifeStoryLines()) &&
                (isFamilyTreeLines() == r.isFamilyTreeLines()) &&
                (isSpouseLines() == r.isSpouseLines()) &&
                (isFathersSide() == r.isFathersSide()) &&
                (isMotherSide() == r.isMotherSide()) &&
                (isMaleEvents() == r.isMaleEvents()) &&
                (isFemaleEvents() == r.isFemaleEvents());
    }

    @NonNull
    @Override
    public String toString() {
        return "Settings Object:" + "\n" +
                "lifeStoryLines: " + isLifeStoryLines() + "\n" +
                "familyTreeLines: " + isFamilyTreeLines() + "\n" +
                "spouseLines: " + isSpouseLines() + "\n" +
                "fatherSide: " + isFathersSide() + "\n" +
                "motherSide: " + isMotherSide() + "\n" +
                "maleEvents: " + isMaleEvents() + "\n" +
                "femaleEvents: " + isFemaleEvents() + "\n";
    }
}
