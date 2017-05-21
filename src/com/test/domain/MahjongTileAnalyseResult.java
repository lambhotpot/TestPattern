package com.test.domain;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class MahjongTileAnalyseResult {


    private HashMap<String, HashMap<String, Double>> orbResult = new HashMap<>();
    private MahjongMainColor fullTileColor = new MahjongMainColor();
    private MahjongMainColor topTileColor = new MahjongMainColor();
    private MahjongMainColor bottomTileColor = new MahjongMainColor();
    private MahjongMainColor leftTileColor = new MahjongMainColor();
    private MahjongMainColor rightTileColor = new MahjongMainColor();

    private int numberOfObjects = 0;
    private int numberOfCircleObjects = 0;

    public int getNumberOfObject (){
        return numberOfObjects;
    }

    public int getNumberOfCircleObjects(){
        return numberOfCircleObjects;
    }


    public void setNumberOfCircleObjects(int numberOfCircleObjects ){
        this.numberOfCircleObjects = numberOfCircleObjects;
    }

    public MahjongTileAnalyseResult() {
    }

    public void intializeOrbResult() {
        //initialize the orb result data structure
        try {
            File folder = new File(MahjongParameters.libPath);
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                orbResult.put(file.getCanonicalFile().toString(), new HashMap<>());
            }
        }catch (IOException e){
            e.printStackTrace();
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

    public MahjongMainColor getFullTileColor() {
        return fullTileColor;
    }

    public void setFullTileColor(MahjongMainColor fullTileColor) {
        this.fullTileColor = fullTileColor;
    }

    public MahjongMainColor getTopTileColor() {
        return topTileColor;
    }

    public void setTopTileColor(MahjongMainColor topTileColor) {
        this.topTileColor = topTileColor;
    }

    public MahjongMainColor getBottomTileColor() {
        return bottomTileColor;
    }

    public void setBottomTileColor(MahjongMainColor bottomTileColor) {
        this.bottomTileColor = bottomTileColor;
    }

    public MahjongMainColor getLeftTileColor() {
        return leftTileColor;
    }

    public void setLeftTileColor(MahjongMainColor leftTileColor) {
        this.leftTileColor = leftTileColor;
    }

    public MahjongMainColor getRightTileColor() {
        return rightTileColor;
    }

    public void setRightTileColor(MahjongMainColor rightTileColor) {
        this.rightTileColor = rightTileColor;
    }

    public void setNumberOfObjects(int numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
    }

    @Override
    public String toString() {
        String resultFirstHalf = "MahjongTileAnalyseResult{" +
                "fullTileColor=" + fullTileColor +
                ", topTileColor=" + topTileColor +
                ", bottomTileColor=" + bottomTileColor +
                ", leftTileColor=" + leftTileColor +
                ", rightTileColor=" + rightTileColor +
                ", numberOfObjects=" + numberOfObjects +
                '}';
        String result2ndHalf = "";
        for (String key : this.orbResult.keySet()) {
            result2ndHalf += "Library: " ;
            result2ndHalf += this.getTopHighScoreForLibrary(key, 3).toString();
            result2ndHalf += "\n";
        }

        return resultFirstHalf + "\n" + result2ndHalf + "\n" ;

    }
}
