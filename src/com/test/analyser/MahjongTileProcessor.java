package com.test.analyser;

import com.test.domain.MahjongParameters;
import com.test.domain.MahjongTileAnalyseResult;
import com.test.util.OpenCVUtil;
import org.opencv.core.*;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


public class MahjongTileProcessor {
    //default value 14
    private int numberOfTiles = 14;

    public MahjongTileProcessor(int numberOfTiles) {
        this.numberOfTiles = numberOfTiles;
    }

    /**
     * @param orig: user input taken from camera
     * @return : a list of Mahjong TileAnalyseResult
     */
    public ArrayList<MahjongTileAnalyseResult> process(Mat orig, Mat clone) {

        preProcess(orig, clone);

        ArrayList<Mat> tiles = findContours(orig, clone);

        //for each tile
        ArrayList<MahjongTileAnalyseResult> results = new ArrayList<>();
        for (Mat tile : tiles) {
            results.add(analyseTile(tile));
        }
        return results;
    }


    public ArrayList<MahjongTileAnalyseResult> process(Mat orig) {
        //make a clone of the orig
        Mat clone = orig.clone();
        return process(orig, clone);
    }


    private MahjongTileAnalyseResult analyseTile(Mat tile) {
        //TODO: complete this


        MahjongTileAnalyseResult result = new MahjongTileAnalyseResult();
        analyseSimpleBlob(tile, result);
        //color
        analyseColor(tile, result);
        //ORB extraction
        analysePattern(tile, result);
        System.out.println(result.toString());
        return result;
    }

    private void analysePattern(Mat tile, MahjongTileAnalyseResult result) {
        //Read library file:
        //TODO: introduce multiple libraries for scoring
        File folder = new File("src/resources/tileLibrary/demo_photo_std1_chopped");
        File[] listOfFiles = folder.listFiles();

        //for each lib image from one set of tiles calculate
        for (File file : listOfFiles) {
            if (file.isFile()) {
                Mat libImage = null;
                try {
                    libImage = Imgcodecs.imread(file.getCanonicalFile().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final double pctScore = compareTileWithLibImage(tile, libImage);
            }
        }

    }


    private double compareTileWithLibImage(Mat tile, Mat libImage) {
        Imgproc.resize(tile, tile, new Size(MahjongParameters.tileStdWidth, MahjongParameters.tileStdHeight));
        Imgproc.resize(libImage, libImage, new Size(tile.width(), tile.height()));

        Mat output = new Mat();
        FeatureDetector detector = FeatureDetector.create(FeatureDetector.ORB);
        DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        MatOfKeyPoint keypointsTile = new MatOfKeyPoint();
        MatOfKeyPoint keypointsLib = new MatOfKeyPoint();

        detector.detect(tile, keypointsTile);
        detector.detect(libImage, keypointsLib);

        //System.out.println("tile KeyPoint Size: " + keypointsTile.size());
        //System.out.println("libImage KeyPoint Size: " + keypointsLib.size());

        Mat descriptrosTile = new Mat();
        Mat descriptrosLib = new Mat();
        extractor.compute(tile, keypointsTile, descriptrosTile);
        extractor.compute(libImage, keypointsLib, descriptrosLib);

        // do matching
        DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
        LinkedList<MatOfDMatch> dmatchesListOfMat = new LinkedList<>();
        matcher.knnMatch(descriptrosTile, descriptrosLib, dmatchesListOfMat, 2);
        LinkedList<MatOfDMatch> good = new LinkedList<>();
        //System.out.println("All Matched Size without Ratio Test: " + dmatchesListOfMat.size());
        LinkedList<DMatch> good_matchesList = new LinkedList<>();
        for (int matchIndx = 0; matchIndx < dmatchesListOfMat.size(); matchIndx++) {
            double ratio = MahjongParameters.knn_ratio;
            if (dmatchesListOfMat.get(matchIndx).toArray()[0].distance < ratio * dmatchesListOfMat.get(matchIndx).toArray()[1].distance) {
                good_matchesList.addLast(dmatchesListOfMat.get(matchIndx).toArray()[0]);
                good.add(dmatchesListOfMat.get(matchIndx));
            }
        }

        double goodMatch = good_matchesList.size();
        double libTotalKeyPoints = keypointsLib.size().height;
        double matchPercentage = goodMatch * 100 / libTotalKeyPoints;
        //System.out.println("Good Match size: " + goodMatch);
        //Features2d.drawMatchesKnn(tile,keypointsTile,libImage,keypointsLib,good,output);
        System.out.println("Good Match Percentage: " + matchPercentage);
        return matchPercentage;
    }


    private void analyseColor(Mat tile, MahjongTileAnalyseResult result) {
        Mat colorMask = tile.clone();
        double[] fullTileColor = computeColorPercentage(colorMask);
        result.setRedPercentage(fullTileColor[0]);
        result.setBlackPercentage(fullTileColor[1]);
        result.setBlueGreenPercentage(fullTileColor[2]);
        //TODO: need to detect bottom half top half color too
        //Mat croppedFrame = frame(Rect(0, frame.rows/2, frame.cols, frame.rows/2));


    }

    private double[] computeColorPercentage(Mat colorMask) {

        double[] result = new double[3];

        //convert to hsv space
        Imgproc.cvtColor(colorMask, colorMask, Imgproc.COLOR_BGR2HSV);
        List<Mat> channels = new ArrayList<>();
        Core.split(colorMask, channels);
        //detect red
        Mat redMasterMask = new Mat();
        Mat redMask1 = new Mat();
        Mat redMask2 = new Mat();
        Core.inRange(colorMask, new Scalar(0, 70, 50), new Scalar(10, 255, 255), redMask1);
        Core.inRange(colorMask, new Scalar(170, 70, 50), new Scalar(180, 255, 255), redMask2);
        Core.bitwise_or(redMask1, redMask2, redMasterMask);
        double image_size = redMasterMask.rows() * redMasterMask.cols();
        double red_percent = (((double) Core.countNonZero(redMasterMask)) * 100) / image_size;

        result[0] = (red_percent);
        //find black and white:

        Mat maskBlack = new Mat();
        Mat maskBlue = new Mat();
        Mat maskGreen = new Mat();

        // for black
        Core.inRange(colorMask, new Scalar(0, 0, 0), new Scalar(180, 255, 38), maskBlack);
        double black_percent = (((double) Core.countNonZero(maskBlack)) * 100) / image_size;
        System.out.println("Tile Black Percentage: " + black_percent);
        result[1] = (black_percent);


        // for blue:
        Core.inRange(colorMask, new Scalar(220 / 2, 50, 50), new Scalar(260 / 2, 255, 255), maskBlue);
        double blue_percent = (((double) Core.countNonZero(maskBlue)) * 100) / image_size;

        //for green
        Core.inRange(colorMask, new Scalar(30, 30, 100), new Scalar(80, 255, 255), maskGreen);
        double green_percent = (((double) Core.countNonZero(maskGreen)) * 100) / image_size;

        result[2] = blue_percent + green_percent;

        return result;
    }

    private void analyseSimpleBlob(Mat tile, MahjongTileAnalyseResult result) {
        //simple blob, no preprocessing to the tile

        FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
        //read a file for parameters
        detector.read("src/resources/config/simpleBlob.xml");
        MatOfKeyPoint keyPoints = new MatOfKeyPoint();
        detector.detect(tile, keyPoints);
        int numberOfObjects = (int) keyPoints.size().height;
        result.setNumberOfObjects(numberOfObjects);
        OpenCVUtil.draw(tile, String.valueOf(numberOfObjects));

    }

    private ArrayList<Mat> findContours(Mat orig, Mat copy) {

        //Step: find contours

        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        List<MatOfPoint> hulls = new ArrayList<>();
        List<MatOfPoint> rois = new ArrayList<>();
        Imgproc.findContours(copy, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        //Step: for each contour, fit  convex hull

        for (MatOfPoint contour : contours) {
            MatOfInt hull = new MatOfInt();
            Imgproc.convexHull(contour, hull);
            MatOfPoint convexHull = OpenCVUtil.getNewContourFromIndices(contour, hull);
            hulls.add(convexHull);
        }


        // Step: approximate each convex hull using polygon, only add those polygon with area in certain range
        for (MatOfPoint hull : hulls) {
            MatOfPoint2f contour2f = new MatOfPoint2f(hull.toArray());
            MatOfPoint2f approx = new MatOfPoint2f(hull.toArray());
            MatOfPoint approxPoint = new MatOfPoint();
            double perimeter = Imgproc.arcLength(contour2f, true);
            Imgproc.approxPolyDP(contour2f, approx, MahjongParameters.contourPolyEpsilon * perimeter, true);
            double area = Imgproc.contourArea(approx);
            approx.convertTo(approxPoint, CvType.CV_32S);
            // cut out each tile based on area
            if (area > MahjongParameters.singleTileAreaLowerThresh && area < MahjongParameters.singleTileAreaUpperThresh) {
                rois.add(approxPoint);
            }
        }
        Vector<Rect> rectangles = new Vector<>();

        //Step : find the bounding rectangle for each polygon

        Mat temp = orig.clone();
        for (MatOfPoint tile : rois) {
            Rect rect = Imgproc.boundingRect(tile);
            rectangles.add(rect);
            // padding each rectangle
            double padding = MahjongParameters.singleTileAreaPaddingRatio;
            int xpadding = (int) (rect.width * padding);
            int ypadding = (int) (rect.height * padding);
            Imgproc.rectangle(temp, new Point(rect.x + xpadding, rect.y - ypadding), new Point(rect.x + rect.width - xpadding, rect.y + rect.height - ypadding), new Scalar(0, 255, 0), 3);
        }


        //Step: remove bad rectangles that is too close to each other

        Vector<Rect> clone = (Vector<Rect>) rectangles.clone();
        for (int i = 0; i < clone.size(); i++) {
            for (int j = 0; j < clone.size(); j++) {
                Rect rect1 = clone.get(i);
                Rect rect2 = clone.get(j);
                if (i < j && OpenCVUtil.eudistance(rect1.x, rect1.y, rect2.x, rect2.y) >= 0 &&
                        OpenCVUtil.eudistance(rect1.x, rect1.y, rect2.x, rect2.y) < 10
                        && OpenCVUtil.eudistance(rect1.x + rect1.width, rect1.y + rect1.height, rect2.x + rect2.width, rect2.y + rect2.height) < 10) {
                    rectangles.remove(i);
                    break;
                }
            }
        }

        ArrayList<Mat> listOfTiles = new ArrayList<>();
        for (int i = 0; i < rectangles.size(); i++) {
            listOfTiles.add(new Mat(orig, rectangles.get(i)));
        }

        return listOfTiles;

    }


    private void preProcess(Mat orig, Mat clone) {

        //resize the image for both orig and clone
        Imgproc.resize(clone, clone, new Size(MahjongParameters.tileStdWidth * this.numberOfTiles, MahjongParameters.tileStdHeight));
        Imgproc.resize(orig, orig, new Size(clone.width(), clone.height()));

        //convert color to grey scale
        Imgproc.cvtColor(clone, clone, Imgproc.COLOR_RGB2GRAY);

        //ClAHE is used to adjust contrast to help with clear edging

        CLAHE clahe = Imgproc.createCLAHE(MahjongParameters.CLAHEClipLimit, new Size(MahjongParameters.CLAHEPixel, MahjongParameters.CLAHEPixel));
        clahe.apply(clone, clone);

        //blur the image  to smooth out background noise
        Imgproc.GaussianBlur(clone, clone, new Size(MahjongParameters.blurPixel, MahjongParameters.blurPixel), 0);

        //finally canny:
        Imgproc.Canny(clone, clone, MahjongParameters.cannyLowerThresh, MahjongParameters.cannyUpperThresh, MahjongParameters.cannyApertureSize, true);

        //now clone is ready for coutour/edge detection
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

}
