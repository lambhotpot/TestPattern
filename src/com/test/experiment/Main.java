package com.test.experiment;

import com.test.analyser.MahjhongLibraryAnalyser;

import com.test.analyser.MahjongTileClassifier;
import com.test.analyser.MahjongTileProcessor;
import com.test.domain.MahjongTileAnalyseResult;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.util.ArrayList;


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
        Mat test5 = Imgcodecs.imread("src/resources/tileLibrary/demo_photo_std1_chopped/zi_zhong.png");



        MahjongTileProcessor processor = new MahjongTileProcessor(1);
        ArrayList<MahjongTileAnalyseResult> processResult = processor.process(test5);
        for (MahjongTileAnalyseResult res: processResult
             ) {
            System.out.println("isWan:-----"+MahjongTileClassifier.isWan(res));
            System.out.println("isHongZhong:-----"+MahjongTileClassifier.isZi_Zhong(res));

        }

        //analyser.process(test2);
        //analyser.process(test3);

        //SimpleBlob.process();
        //MahjhongLibraryAnalyser.INSTANCE.writeResult();

    }


}


