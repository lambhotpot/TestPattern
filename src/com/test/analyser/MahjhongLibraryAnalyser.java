package com.test.analyser;

import com.test.domain.MahjongParameters;
import com.test.domain.MahjongTileAnalyseResult;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * In this class we want to go through each tile in library and save their stats in a config file
 * Later on we read it as a resource for comparison
 */
public enum MahjhongLibraryAnalyser {
    INSTANCE;

    private static HashMap<String, HashMap<String, MahjongTileAnalyseResult>> libStatsMap = new HashMap<>();

    MahjhongLibraryAnalyser() {

    }

    public void writeResult() {
        System.out.println(this.toString());

    }

    @Override
    public String toString() {
        return "MahjhongLibraryAnalyser{" +
                "libStatsMap=" + libStatsMap +
                '}';
    }

    static {
        MahjongTileProcessor processor = new MahjongTileProcessor(1);

        try {
            File folder = new File(MahjongParameters.libPath);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                libStatsMap.put(file.getCanonicalFile().toString(), new HashMap<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String libraryPath : libStatsMap.keySet()) {
            File tileFolder = new File(libraryPath);
            File[] listOfLibTiles = tileFolder.listFiles();
            for (File libraryImageFile : listOfLibTiles
                    ) {
                if (libraryImageFile.isFile()) {
                    Mat libImageMat;
                    try {
                        libImageMat = Imgcodecs.imread(libraryImageFile.getCanonicalFile().toString());
                        MahjongTileAnalyseResult libResult = processor.analyseLibraryTile(libImageMat);
                        HashMap tileResultMap = libStatsMap.get(libraryPath);
                        tileResultMap.put(libraryImageFile.getName(), libResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }

}
