package com.test.experiment;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class DominateColor {
    //TODO: find dominating color: threshhold the percentage

    public static void process (){
        //convert to hsv

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = Imgcodecs.imread("src/resources/data/standard/honor-red.jpg");
        Mat orig = Imgcodecs.imread("src/resources/data/standard/honor-red.jpg");
        Imgproc.resize(src, src, new Size(src.width() * 0.2, src.height() * 0.2));
        Imgproc.resize(orig, orig, new Size(orig.width() * 0.2, orig.height() * 0.2));
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
        System.out.println(red_percent);
        OpenCVUtil.draw(mask, "demo");


    }


}
