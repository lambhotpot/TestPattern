package com.test.domain;


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

    public int getDominantColor(){
        if (redPercentage > blackPercentage && redPercentage > blueGreenPercentage){
            return MahjongParameters.RED;
        }
        if (blackPercentage > redPercentage && blackPercentage > blueGreenPercentage){
            return MahjongParameters.BLACK;
        }
        if (blueGreenPercentage > redPercentage && blueGreenPercentage > blackPercentage){
            return MahjongParameters.GREEN;
        }
        //return invalid
        return MahjongParameters.INVALID_COLOR ;
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
