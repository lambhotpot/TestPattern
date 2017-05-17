package com.test.experiment;

import com.test.analyser.MahjongTileProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        //Experiment.process();
        //WanDetector.process();
        //Recognition.process();
        //DominateColor.process();

        Mat test = Imgcodecs.imread("src/resources/data/demo_photo_std1_chains/tong_1-9.png");
        Mat test2 = Imgcodecs.imread("src/resources/data/demo_photo_std1_chains/tiao_1-9.png");
        Mat test3 = Imgcodecs.imread("src/resources/data/demo_photo_std1_chains/wan_1-9.png");

        MahjongTileProcessor analyser = new MahjongTileProcessor(9);
        //analyser.process(test);
        //analyser.process(test2);
        analyser.process(test3);



        //SimpleBlob.process();


    }


}


