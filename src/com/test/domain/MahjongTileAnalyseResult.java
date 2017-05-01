package com.test.domain;

/**
 * Created by Lotti on 01/05/2017.
 */
public class MahjongTileAnalyseResult {
    private int numberOfObjects = 0;

    private double redPercentage = 0;
    private double blueGreenPercentage = 0;
    private double blackPercentage = 0;


    public double getBlueGreenPercentage() {
        return blueGreenPercentage;
    }

    public void setBlueGreenPercentage(double blueGreenPercentage) {
        this.blueGreenPercentage = blueGreenPercentage;
    }

    public double getBlackPercentage() {
        return blackPercentage;
    }

    public void setBlackPercentage(double blackPercentage) {
        this.blackPercentage = blackPercentage;
    }


    public double getRedPercentage() {
        return redPercentage;
    }

    public void setRedPercentage(double redPercentage) {
        this.redPercentage = redPercentage;
    }

    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    public void setNumberOfObjects(int numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
    }


    @Override
    public String toString() {
        return "MahjongTileAnalyseResult{" +
                "numberOfObjects=" + numberOfObjects +
                ", redPercentage=" + redPercentage +
                ", blueGreenPercentage=" + blueGreenPercentage +
                ", blackPercentage=" + blackPercentage +
                '}';
    }
}
