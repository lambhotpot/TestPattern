package com.test.experiment;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lotti on 07/03/2017.
 */
public class Experiment {





    public static void process() {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = Imgcodecs.imread("src/resources/data/demo-images/multiple2.jpg");

        // Mat src = Imgcodecs.imread("src/resources/data/demo-images/scattered1.jpg");
        //Mat src = Imgcodecs.imread("src/resources/data/demo-images/multiple3.jpg");
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

        Imgproc.resize(src, src, new Size(src.width()*0.2,src.height()*0.2));
        Imgproc.cvtColor(src, greyed, Imgproc.COLOR_RGB2GRAY);

      /*  CLAHE clahe = Imgproc.createCLAHE(50.0, new Size(5, 5));
        clahe.apply(greyed,greyed);
        draw(greyed,"clahe");*/

        Imgproc.GaussianBlur(greyed, greyed, new Size(5, 5), 0);
        System.out.println("width = " +src.width() + " height: " + src.height());


        //Imgproc.Sobel(greyed,greyed, CvType.CV_8UC1,1,0);
        //Imgproc.Sobel(greyed,greyed, CvType.CV_8UC1,0,1);

        //Imgcodecs.imwrite("gsobelx.jpg",greyed);


        Imgproc.Canny(greyed, greyed, 10, 30, 3, true);
        draw(greyed,"canny");


        /*Imgproc.dilate(greyed,greyed, new Mat());
        draw(greyed,"dilate");*/

        //Imgproc.adaptiveThreshold(greyed,greyed,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,11,2);
        //Imgproc.threshold(greyed, greyed, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        //draw(greyed,"threshed");


        /**
         *    construct and apply a closing kernel to 'close' gaps between 'white'
         *    //pixels
         */

/*
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,new Size(3, 3));
        Imgproc.morphologyEx(greyed, closed,Imgproc.MORPH_CLOSE, kernel);
        draw(closed,"closed");*/



        /**
         * image: Output of the edge detector. It should be a grayscale image.
           lines: A vector that will store the parameters  of the detected lines
           rho: The resolution of the parameter in pixels. We use 1 pixel.
           theta: The resolution of the parameter in radians. We use 1 degree (CV_PI/180)
           threshold: The minimum number of intersections to “detect” a line
         */

/*
        Mat lineClone = src.clone();

        int threshold = 150;
        int minLineSize = 0;
        int lineGap = 0;
        Imgproc.HoughLinesP(greyed, lines, 1, Math.PI/180, threshold, minLineSize, lineGap);
*/








        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();


        Imgproc.findContours(greyed, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_TC89_L1);



        List<MatOfPoint> hulls = new ArrayList<MatOfPoint>();
        List<MatOfPoint> rois = new ArrayList<MatOfPoint>();

        for (MatOfPoint contour : contours) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(contour, hull);
            MatOfPoint convexHull = OpenCVUtil.getNewContourFromIndices(contour, hull);
            hulls.add(convexHull);
        }

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


        int index = 0;
        Mat temp = src.clone();
        System.out.println(rois.size());
        for (MatOfPoint tile : rois) {
            //Imgproc.drawContours(temp,rois,index,new Scalar(0, 255, 0));
            Rect rect = Imgproc.boundingRect(tile);
            Imgproc.rectangle(temp, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 3);

            index++;
        }

        draw(temp,"rectangle");
        System.out.println(rois.size());

        //drawCoutourBoxes(rois,temp2,"boudingRec.jpg");


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
            draw(src,"rectangle");
        }
    }


    public static void draw(Mat m, String name){
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            Mat m2 = new Mat();
            Imgproc.cvtColor(m,m2,Imgproc.COLOR_BGR2RGB);
            type = BufferedImage.TYPE_3BYTE_BGR;
            m = m2;
        }
        byte [] b = new byte[m.channels()*m.cols()*m.rows()];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        image.getRaster().setDataElements(0, 0, m.cols(),m.rows(), b);
        JFrame frame = new JFrame();
        frame.setTitle(name);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

}



