package com.test.experiment;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;



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

        Mat lines = new Mat();
        Mat output = new Mat();


        /**
         *  Step: resize the image to 20%
         */

        Imgproc.resize(src, src, new Size(src.width() * 0.2, src.height() * 0.2));
        System.out.println("width = " + src.width() + " height: " + src.height());

        /**
         * Step: convert the color to grey
         */

        Imgproc.cvtColor(src, greyed, Imgproc.COLOR_RGB2GRAY);


        /** Unused
         * ClAHE is used to adjust contrast to help with clear edging
         */
        //CLAHE clahe = Imgproc.createCLAHE(50.0, new Size(5, 5));
        //clahe.apply(greyed,greyed);
        //draw(greyed,"clahe");

        /**
         * Step: blur the image with 5*5 pixel to smooth out background noise
         */
        Imgproc.GaussianBlur(greyed, greyed, new Size(5, 5), 0);


        /**
         * Unused: Sobel algorithm for edge detection
         */
        //Imgproc.Sobel(greyed,greyed, CvType.CV_8UC1,1,0);
        //Imgproc.Sobel(greyed,greyed, CvType.CV_8UC1,0,1);
        //Imgcodecs.imwrite("gsobelx.jpg",greyed);


        /**
         * Step: Use Canny algorithm to find edge.  Aperture size =3
         */
        Imgproc.Canny(greyed, greyed, 10, 30, 3, true);
        OpenCVUtil.draw(greyed, "canny");


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
         * Step: find contours
         */

        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(greyed, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);

        Imgproc.drawContours(src,contours,-1, new Scalar(0,255,0),1);
        OpenCVUtil.draw(src, "coutour");


        List<MatOfPoint> hulls = new ArrayList<MatOfPoint>();
        List<MatOfPoint> rois = new ArrayList<MatOfPoint>();

        /**
         * Step: for each contour, fit  convex hull
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
            if (/*area <4000 && area >50)*/
            /*approx.toArray().length==4 && */
                    area > 4000 && area < 10000)
            {
                System.out.println(area);
                rois.add(approxPoint);
            }
        }


        Vector<Rect> rectangles = new Vector<>();
        /**
         * Step6: find the bounding rectangle for each polygon
         */
        int index = 0;
        Mat temp = src.clone();
        System.out.println(rois.size());
        for (MatOfPoint tile : rois) {
            //Imgproc.drawContours(temp,rois,index,new Scalar(0, 0, 255));

            Rect rect = Imgproc.boundingRect(tile);

            rectangles.add(rect);
            //Draw rectangle
            Imgproc.rectangle(temp, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 3);

            index++;
        }
        OpenCVUtil.draw(temp, "rectangle");
        System.out.println("rectangles: " + rectangles.size());


        /**
         * Step7: remove bad rectangles that is too close to each other
         */
        Vector<Rect> clone = (Vector<Rect>) rectangles.clone();

        for (int i = 0; i < clone.size(); i++) {
            for (int j = 0; j < clone.size(); j++) {
                Rect rect1 = clone.get(i);
                Rect rect2 = clone.get(j);
                System.out.println("distance: " + OpenCVUtil.eudistance(rect1.x, rect1.y, rect2.x, rect2.y));
                System.out.println("distance : " + OpenCVUtil.eudistance(rect1.x, rect1.y, rect2.x, rect2.y));
                //remove those rectangle that are too close to each other.
                if (i < j && OpenCVUtil.eudistance(rect1.x, rect1.y, rect2.x, rect2.y) >= 0 &&
                        OpenCVUtil.eudistance(rect1.x, rect1.y, rect2.x, rect2.y) < 10
                        && OpenCVUtil.eudistance(rect1.x + rect1.width, rect1.y + rect1.height, rect2.x + rect2.width, rect2.y + rect2.height) < 10) {
                    System.out.println("i " + i);
                    System.out.println("j " + j);
                    System.out.println("Can remove i");
                    rectangles.remove(i);
                    return;
                }
            }

        }


        System.out.println("Size After removal " + rectangles.size());

        //

        /**
         * Step: now get a list of sub mat
         */

        ArrayList<Mat> listOfTiles = new ArrayList<>(Collections.nCopies(rectangles.size(), new Mat()));
        for(int i =0;i<rectangles.size();i++){
            //sub mat from src orignal image after resizing:
            //width = 652 height: 489
            src.submat(rectangles.get(i)).copyTo(listOfTiles.get(i));
            System.out.println("width = " + listOfTiles.get(i).width() + " height: " + listOfTiles.get(i).height());
            //OpenCVUtil.draw(listOfTiles.get(i),"sub"+i);
            //SimpleBlob.processImage(listOfTiles.get(i));


        }

        /**
         * Step: Now can process each tiles individually
         *
         */
        //TODO: loop through each tiles




    }


}



