package com.test.domain;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class MahjongTileAnalyseResult {

    private HashMap<String, HashMap<String, Double>> orbResult = new HashMap<>();

    private int numberOfObjects = 0;
    private double redPercentage = 0;
    private double blueGreenPercentage = 0;
    private double blackPercentage = 0;


    public MahjongTileAnalyseResult() {

        try {
            intializeOrbResult();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void intializeOrbResult() throws IOException {
        //read directory names:
        File folder = new File(MahjongParameters.libPath);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            orbResult.put(file.getCanonicalFile().toString(), new HashMap<>());
        }
    }

    public Set<String> getLibraryPaths() {
        return this.orbResult.keySet();
    }

    public void setScoreForLibrary(String libName, String tileName, Double score) {
        this.orbResult.get(libName).put(tileName, score);
    }


    public HashMap<String, Double> getTopHighScoreForLibrary(String libraryName, int count) {
        LinkedHashMap<String, Double> result = new LinkedHashMap<>();

        Set<Map.Entry<String, Double>> entrySet = orbResult.get(libraryName).entrySet();
        List<Map.Entry<String, Double>> list =
                new LinkedList<>(entrySet);
        //sort descending order
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
               return (o2.getValue().compareTo(o1.getValue()));
            }
        });
        int index = 0;
        for (Map.Entry<String, Double> entry : list) {
            if (index < count) {
                result.put(entry.getKey(), entry.getValue());
            }
            index++;
        }
        return result;
    }


    public double getBlueGreenPercentage() {
        return blueGreenPercentage;
    }

    public void setBlueGreenPercentage(double blueGreenPercentage) {
        this.blueGreenPercentage = blueGreenPercentage;
    }

    public double getBlackPercentage() {
        return blackPercentage;
    }

    public void setBlackPercentage(double blackPercentage) {
        this.blackPercentage = blackPercentage;
    }


    public double getRedPercentage() {
        return redPercentage;
    }

    public void setRedPercentage(double redPercentage) {
        this.redPercentage = redPercentage;
    }

    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    public void setNumberOfObjects(int numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
    }

    public int getDominantColor() {
        if (redPercentage > blackPercentage && redPercentage > blueGreenPercentage) {
            return MahjongParameters.RED;
        }
        if (blackPercentage > redPercentage && blackPercentage > blueGreenPercentage) {
            return MahjongParameters.BLACK;
        }
        if (blueGreenPercentage > redPercentage && blueGreenPercentage > blackPercentage) {
            return MahjongParameters.GREEN;
        }
        //return invalid
        return MahjongParameters.INVALID_COLOR;
    }


    @Override
    public String toString() {
        String resultFirstHalf = "MahjongTileAnalyseResult{" +
                ", numberOfObjects=" + numberOfObjects +
                ", redPercentage=" + redPercentage +
                ", blueGreenPercentage=" + blueGreenPercentage +
                ", blackPercentage=" + blackPercentage +
                '}';
        String result2ndHalf = "";
        for (String key : this.orbResult.keySet()) {
            result2ndHalf += "Library: " ;
            result2ndHalf += this.getTopHighScoreForLibrary(key, 3).toString();
            result2ndHalf += "\n";
        }

        return resultFirstHalf + "\n" + result2ndHalf;

    }
}
