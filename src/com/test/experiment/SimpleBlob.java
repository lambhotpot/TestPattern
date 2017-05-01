package com.test.experiment;

import com.test.domain.MahjongParameters;
import com.test.util.OpenCVUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Size;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Lotti on 19/03/2017.
 */
public class SimpleBlob {

    //TODO: Java blob parameters in a file: blob by area

    //TODO: blob analysis, fill the holes

    public static  void processImage(Mat image1){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat output = new Mat();
        // image1 = Imgcodecs.imread("src/resources/data/standard/circles-three.jpg");


        //Imgproc.cvtColor(image1, image1, Imgproc.COLOR_RGB2GRAY);
        //Imgproc.GaussianBlur(image1, image1, new Size(5, 5), 0);
        //Imgproc.adaptiveThreshold(image1,image1,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,11,2);
        //Imgproc.Canny(image1, image1, 10, 30, 3, true);

        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        detector.read("src/resources/config/simpleBlob.xml");

        MatOfKeyPoint keypoints = new MatOfKeyPoint();

        detector.detect(image1,keypoints);

        Features2d.drawKeypoints(image1,keypoints,output);
        System.out.println("Number of blobs: "+ keypoints.size());
        OpenCVUtil.draw(output, String.valueOf(keypoints.size().height));
    }


    public static void process() {

        /**
         * Experiment on common ORB feature matching
         */
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat image1 = Imgcodecs.imread("src/resources/tileLibrary/demo_photo_std1_chopped/tong6.png");
        Imgproc.resize(image1, image1, new Size(100,150));
        System.out.println(image1.width()+" height: "+image1.height());
        Mat output = new Mat();

        //Imgproc.cvtColor(image1, image1, Imgproc.COLOR_RGB2GRAY);
        //Imgproc.GaussianBlur(image1, image1, new Size(7, 7), 0);
        //Imgproc.adaptiveThreshold(image1,image1,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,11,2);*/
        /*CLAHE clahe = Imgproc.createCLAHE(MahjongParameters.CLAHEClipLimit, new Size(MahjongParameters.CLAHEPixel, MahjongParameters.CLAHEPixel));
        clahe.apply(image1, image1);*/


        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        detector.read("src/resources/config/simpleBlob.xml");
        //detector.write("src/resources/config/simpleBlob.xml");

        MatOfKeyPoint keypoints = new MatOfKeyPoint();
        detector.detect(image1,keypoints);


        Features2d.drawKeypoints(image1,keypoints,output);
        System.out.println("Number of blobs: "+ keypoints.size());
        OpenCVUtil.draw(output,"Map");



    }
}
