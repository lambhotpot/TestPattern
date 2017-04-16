package com.test.experiment;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotti on 09/04/2017.
 */
public class WanDetector {

    public static void process(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //Mat src = Imgcodecs.imread("src/resources/data/standard3/6wan.png");
        Mat src = Imgcodecs.imread("src/resources/data/standard/circles-nine.jpg");
        //Mat src = Imgcodecs.imread("src/resources/data/demo-images/1wan.jpg");
        //Imgproc.resize(src, src, new Size(100, 100));
        Imgproc.cvtColor(src, src, Imgproc.COLOR_BGR2HSV);
        List<Mat> channels = new ArrayList<>();
        Core.split(src,channels);

        Mat mask = new Mat();
        Mat mask1 = new Mat();
        Mat mask2 = new Mat();

        Core.inRange(src, new Scalar(0, 70, 50), new Scalar(10, 255, 255), mask1);
        Core.inRange(src, new Scalar(170, 70, 50),new Scalar(180, 255, 255), mask2);

        Core.bitwise_or(mask1, mask2,mask);

        double image_size = mask.rows()*mask.cols();
        System.out.println("Image size: "+ image_size);


        double red_percent = (((double) Core.countNonZero(mask))*100)/image_size;
        System.out.println("red"+ red_percent);
        OpenCVUtil.draw(mask, "result");


        //find black and white:

        Mat maskBlack = new Mat();
        Mat maskWhitle = new Mat();
        Mat maskBlue = new Mat();
        Mat maskGreen = new Mat();

        // for black
        Core.inRange(src, new Scalar(0, 0, 0),new Scalar(180, 255, 38), maskBlack);

        // for white
        Core.inRange(src, new Scalar(0, 0, 200), new Scalar(180, 255, 255), maskWhitle);



        double black_percent = (((double) Core.countNonZero(maskBlack))*100)/image_size;
        System.out.println("black:" + black_percent);
        OpenCVUtil.draw(maskBlack, "black");
        double white_percent = (((double) Core.countNonZero(maskWhitle))*100)/image_size;
        System.out.println("white:" + white_percent);


        // for blue:

        Core.inRange(src, new Scalar(220/2, 50, 50), new Scalar(260/2, 255, 255), maskBlue);
        double blue_percent = (((double) Core.countNonZero(maskBlue))*100)/image_size;
        System.out.println("blue:" + blue_percent);
        OpenCVUtil.draw(maskBlue, "blue");

        //for green
        Core.inRange(src, new Scalar(30, 30, 100), new Scalar(80, 255, 255), maskGreen);
        double green_percent = (((double) Core.countNonZero(maskGreen))*100)/image_size;
        System.out.println("green_percent:" + green_percent);
        OpenCVUtil.draw(maskGreen, "green");

        Mat maskGreenBlue = new Mat();

        Core.bitwise_or(maskGreen, maskBlue,maskGreenBlue);
        OpenCVUtil.draw(maskGreenBlue, "green blue");

    }


}
