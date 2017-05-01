package com.test.experiment;

import com.test.util.OpenCVUtil;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Lotti on 12/03/2017.
 */
public class Recognition {

    public static void process(Mat image1) throws IOException {

        /**
         * Experiment on comone ORB feature matching
         */
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
       // Mat image1 = Imgcodecs.imread("src/resources/data/standard/circles-five.jpg");

        image1 = Imgcodecs.imread("src/resources/data/demo-images/1wan.jpg");
        File folder = new File("src/resources/data/demo_photo_std1_chopped");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                processImage(image1,file.getCanonicalFile().toString());
            }
        }


    }

    private static void processImage(Mat image1, String fileName ) {
        System.out.println("standard library... "+fileName);


        Mat image2 = Imgcodecs.imread(fileName);
        Imgproc.resize(image1, image1, new Size(100,150));
        Imgproc.resize(image2, image2, new Size(100,150));

        Mat output = new Mat();
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

        detector.detect(image1, keypoints1);
        detector.detect(image2, keypoints2);


        //System.out.println("K1 Size: " + keypoints1.size());
        //System.out.println("K2 Size: " + keypoints2.size());


        Mat descriptros1 = new Mat();
        Mat descriptros2 = new Mat();
        extractor.compute(image1,keypoints1,descriptros1);
        extractor.compute(image2,keypoints2,descriptros2);



        // do matching
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
//        MatOfDMatch matches = new MatOfDMatch();
//        MatOfDMatch good = new MatOfDMatch();

        LinkedList<MatOfDMatch> dmatchesListOfMat = new LinkedList<>();
        matcher.knnMatch(descriptros1,descriptros2, dmatchesListOfMat,2);
        LinkedList<MatOfDMatch> good = new LinkedList<>();
        System.out.println("All Matched Size" + dmatchesListOfMat.size());
        LinkedList<DMatch> good_matchesList = new LinkedList<>();
        for (int matchIndx = 0; matchIndx < dmatchesListOfMat.size() ; matchIndx++) {
            double ratio = 0.8;
            if (dmatchesListOfMat.get(matchIndx).toArray()[0].distance  < ratio * dmatchesListOfMat.get(matchIndx).toArray()[1].distance) {
                good_matchesList.addLast(dmatchesListOfMat.get(matchIndx).toArray()[0]);
                good.add(dmatchesListOfMat.get(matchIndx));
            }
        }

       // System.out.println("Good Match Size" + good_matchesList.size());
        double a = good_matchesList.size();
        double b = keypoints2.size().height;
        System.out.println("Good Match size: " + a);
        //System.out.println("K2: " + b);
        if(a/b>0.1){
            Features2d.drawMatchesKnn(image1,keypoints1,image2,keypoints2,good,output);
            String ab = String.valueOf(a/b);
            OpenCVUtil.draw(output, ab);
            System.out.println("Good Match Percentage: " + a/b);
        }




    }


}
