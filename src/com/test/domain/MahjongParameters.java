package com.test.domain;


public enum MahjongParameters {
    ;

    /** Resize the image paramters.
     * w100 * h150 * 牌数
     * Default number of tiles = 14
     */

    public static final double tileStdHeight = 150 ;
    public static final double tileStdWidth = 100 ;


    /**
     * Tile area for contour recognition.
     */
    public static double singleTileArea = tileStdHeight * tileStdWidth ;
    public static double singleTileAreaLowerThresh = singleTileArea * 0.4 ;
    public static double singleTileAreaUpperThresh = singleTileArea * 1.1 ;
    //default cut 1% from the tile
    public static double singleTileAreaPaddingRatio = 0.01;
    public static double contourPolyEpsilon = 0.02 ;

    /**
     * CLAHE Parameters: increase contrast for better edging
     */
    public static final int CLAHEPixel = 1;
    public static final int CLAHEClipLimit = 4;

    /**
     * Gaussian Blur
     */
    public static final int blurPixel = 7;


    /**
     * canny
     */

    public static int cannyLowerThresh = 10 ;
    public static int cannyUpperThresh = 30 ;
    public static int cannyApertureSize = 3 ;


    /**
     * knn ratio test
     */
    public static double knn_ratio = 0.8;

}
