package com.test.domain;

/**
 * Created by Lotti on 17/05/2017.
 */
public class MahjongMainColor {

    private double redPercentage = 0;
    private double blueGreenPercentage = 0;
    private double blackPercentage = 0;


    public MahjongMainColor(){

    }

    public MahjongMainColor(double redPercentage, double blackPercentage,double blueGreenPercentage) {
        this.redPercentage = redPercentage;
        this.blueGreenPercentage = blueGreenPercentage;
        this.blackPercentage = blackPercentage;
    }

    public double getRedPercentage() {
        return redPercentage;
    }

    public void setRedPercentage(double redPercentage) {
        this.redPercentage = redPercentage;
    }

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


    public int getDominantColor() {
        if (redPercentage > blackPercentage && redPercentage > blueGreenPercentage) {
            return MahjongParameters.RED;
        }
        if (blackPercentage > redPercentage && blackPercentage > blueGreenPercentage) {
            return MahjongParameters.BLACK;
        }
        if (blueGreenPercentage > redPercentage && blueGreenPercentage > blackPercentage) {
            return MahjongParameters.GREEN;
        }
        //return invalid
        return MahjongParameters.INVALID_COLOR;
    }

    public double getRedRatio (){
        return redPercentage / (redPercentage+blackPercentage+blueGreenPercentage);
    }

    public double getBlueRatio (){
        return blueGreenPercentage / (redPercentage+blackPercentage+blueGreenPercentage);
    }


    public double getBlackRatio (){
        return blackPercentage / (redPercentage+blackPercentage+blueGreenPercentage);
    }


    public double getRedBlackRatio (){
        return (redPercentage+blackPercentage)/(redPercentage+blackPercentage+blueGreenPercentage);
    }

    @Override
    public String toString() {
        return "MahjongMainColor{" +
                "redPercentage=" + redPercentage +
                ", blueGreenPercentage=" + blueGreenPercentage +
                ", blackPercentage=" + blackPercentage +
                '}';
    }
}

