package com.test.experiment;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotti on 07/03/2017.
 */
public class Experiment {



    public static void process() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //Mat src = Imgcodecs.imread("src/resources/data/demo-images/multiple2.jpg");
        //Mat src = Imgcodecs.imread("src/resources/data/demo-images/scattered1.jpg");
        Mat src = Imgcodecs.imread("src/resources/data/demo-images/multiple3.jpg");
        //Mat src = Imgcodecs.imread("src/resources/data/demo-images/bambu2.jpg");
        Mat greyed = new Mat();
        Mat blurred = new Mat();
        Mat resize = new Mat();
        Mat thresh = new Mat();
        Mat hierarchy = new Mat();
        Mat canny = new Mat();
        Mat result = new Mat();
        Mat lines = new Mat();
        Mat closed =new Mat();


        /**
         *  Step 1: resize the image to 20%
         */

        Imgproc.resize(src, src, new Size(src.width()*0.2,src.height()*0.2));
        System.out.println("width = " +src.width() + " height: " + src.height());

        /**
         * Step 2: convert the color to grey
         */

        Imgproc.cvtColor(src, greyed, Imgproc.COLOR_RGB2GRAY);


        /** Unused
         * ClAHE is used to adjust contrast to help with clear edging
         */
        //CLAHE clahe = Imgproc.createCLAHE(50.0, new Size(5, 5));
        //clahe.apply(greyed,greyed);
        //draw(greyed,"clahe");

        /**
         * Step 3: blur the image with 5*5 pixel to smooth out background noise
         */
        Imgproc.GaussianBlur(greyed, greyed, new Size(5, 5), 0);


        /**
         * Unused: Sobel algorithm for edge detection
         */
        //Imgproc.Sobel(greyed,greyed, CvType.CV_8UC1,1,0);
        //Imgproc.Sobel(greyed,greyed, CvType.CV_8UC1,0,1);
        //Imgcodecs.imwrite("gsobelx.jpg",greyed);


        /**
         * Step 4: Use Canny algorithm to find edge.  Aperture size =3
         */
        Imgproc.Canny(greyed, greyed, 10, 30, 3, true);
        OpenCVUtil.draw(greyed,"canny");


        /**
         * Unused: Dilate is to fill holes on disconnect lines
         */
        /*Imgproc.dilate(greyed,greyed, new Mat());
        draw(greyed,"dilate");*/


        /**
         * Unused: adaptiveThreshold with OTSU is to process image to make edge clear
         */
        //Imgproc.adaptiveThreshold(greyed,greyed,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,11,2);
        //Imgproc.threshold(greyed, greyed, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        //draw(greyed,"threshed");




        /**
         * Unused: construct and apply a closing kernel to 'close' gaps between 'white' pixels
         */
        //Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3, 3));
        //Imgproc.morphologyEx(greyed, closed,Imgproc.MORPH_CLOSE, kernel);
        //draw(closed,"closed");



        /** Unused:HoughLinesP for line detection
         *
         * Parameters
         * image: Output of the edge detector. It should be a grayscale image.
           lines: A vector that will store the parameters  of the detected lines
           rho: The resolution of the parameter in pixels. We use 1 pixel.
           theta: The resolution of the parameter in radians. We use 1 degree (CV_PI/180)
           threshold: The minimum number of intersections to “detect” a line
         */


        //Mat lineClone = src.clone();
        //int threshold = 150;
        //int minLineSize = 0;
        //int lineGap = 0;
        //Imgproc.HoughLinesP(greyed, lines, 1, Math.PI/180, threshold, minLineSize, lineGap);







        /**
         * Step4: find contours
         */

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(greyed, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        List<MatOfPoint> hulls = new ArrayList<MatOfPoint>();
        List<MatOfPoint> rois = new ArrayList<MatOfPoint>();

        /**
         * Step5: for each contour, fit  convex hull
         */
        for (MatOfPoint contour : contours) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(contour, hull);
            MatOfPoint convexHull = OpenCVUtil.getNewContourFromIndices(contour, hull);
            hulls.add(convexHull);
        }

        /**
         * Step6: approximate each convex hull using polygon, only add those polygon with area in certain range
         */
        for (MatOfPoint hull : hulls) {
            MatOfPoint2f contour2f = new MatOfPoint2f(hull.toArray());
            MatOfPoint2f approx = new MatOfPoint2f(hull.toArray());
            MatOfPoint approxPoint = new MatOfPoint();
            double perimeter = Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approx, 0.02 * perimeter, true);
            double area = Imgproc.contourArea(approx);

            approx.convertTo(approxPoint, CvType.CV_32S);
            if (/*approx.toArray().length==4 && */area>4000 && area<10000) {
                System.out.println(area);
                rois.add(approxPoint);
            }
        }


        /**
         * Step6: find the bounding rectangle for each polygon
         */
        int index = 0;
        Mat temp = src.clone();
        System.out.println(rois.size());
        for (MatOfPoint tile : rois) {
            //Imgproc.drawContours(temp,rois,index,new Scalar(0, 255, 0));
            Rect rect = Imgproc.boundingRect(tile);
            Imgproc.rectangle(temp, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 3);

            index++;
        }

        OpenCVUtil.draw(temp,"rectangle");
        System.out.println(rois.size());



    }


    private static void drawCoutourBoxes(List<MatOfPoint> contours, Mat src, String filename) {

        MatOfPoint2f approxCurve = new MatOfPoint2f();
        for (int i = 0; i < contours.size(); i++) {
            //Convert contours(i) from MatOfPoint to MatOfPoint2f
            MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
            //Processing on mMOP2f1 which is in type MatOfPoint2f
            double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
            Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

            //Convert back to MatOfPoint
            MatOfPoint points = new MatOfPoint(approxCurve.toArray());

            // Get bounding rect of contour
            Rect rect = Imgproc.boundingRect(points);

            // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 3);
            OpenCVUtil.draw(src,"rectangle");
        }
    }




}



