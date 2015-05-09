package com.reed.tensteps.utilities;

/**
 * Created by reed on 5/8/15.
 */
public class PointPair {

    private String mDescription;
    private int mValue;

    public PointPair(String description, int value) {
        mDescription = description;
        mValue = value;
    }
    public int getValue() {
        return mValue;
    }

    public void setValue(int mValue) {
        this.mValue = mValue;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }


}
