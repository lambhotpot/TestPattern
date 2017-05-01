package com.test.experiment;

import com.test.analyser.MahjongTileAnalyser;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
       //Experiment.process();


        //WanDetector.process();
        Recognition.process(null);
        // DominateColor.process();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat test = Imgcodecs.imread("src/resources/data/demo_photo_std1_chains/tiao_1-9.png");

        MahjongTileAnalyser analyser = new MahjongTileAnalyser(9);

        //analyser.process(test);
        //SimpleBlob.process();


    }


}


