package com.test.experiment;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lotti on 14/04/2017.
 */
public class HoleFiller {

    public static void analyseTile(Mat src){

        //OpenCVUtil.draw(blackWhite, "tileContour");
    }


    public void holeFilling(Mat src){
        System.out.println("tile width = " + src.width() + " height: " + src.height());
        Mat greyed = new Mat();
        Mat hierarchy = new Mat();
        Mat blackWhite = new Mat();

        Imgproc.cvtColor(src, greyed, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(greyed, greyed, new Size(5, 5), 0);
        Imgproc.adaptiveThreshold(greyed,blackWhite,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,11,2);
        Imgproc.Canny(greyed, greyed, 10, 30, 3, true);

        /**
         * Step : find contour
         */

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> filteredContoursByArea = new ArrayList<MatOfPoint>();
        Imgproc.findContours(greyed, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);


        System.out.println("Contour area............................");
        Iterator<MatOfPoint> coutour = contours.iterator();
        while (coutour.hasNext()) {
            MatOfPoint wrapper = coutour.next();
            double area = Imgproc.contourArea(wrapper);
            System.out.println("Filter Contour area: " + area);
            if(area < 5000){
                filteredContoursByArea.add(wrapper);
            }

        }
        System.out.println("End of Contour area............................");


        //only draw count
        Imgproc.drawContours(blackWhite,filteredContoursByArea,-1, new Scalar(0,255,0),1);



        for (MatOfPoint contour: filteredContoursByArea) {
            Imgproc.fillPoly(blackWhite, Arrays.asList(contour), new Scalar(0, 0, 0));
        }

        OpenCVUtil.draw(blackWhite, "tileContour");
    }


}
