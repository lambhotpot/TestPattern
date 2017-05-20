package com.test.analyser;

import com.test.domain.MahjongTileAnalyseResult;

/**
 * This class take result object as input and apply smart logic to determine the final result
 */
public class MahjongTileClassifier {

    public static void analyseResult(MahjongTileAnalyseResult result){
    }

    public static boolean isZi_Zhong(MahjongTileAnalyseResult result) {
        return result.getFullTileColor().getRedRatio() >= 0.99;
    }

    public static boolean isWan(MahjongTileAnalyseResult result){
        //assume each tile only have 3 colors

        //exclude the case for hongzhong
        if(isZi_Zhong(result))
            return false;

        //Full tile r + b percentage
        double rNbPctFull = result.getFullTileColor().getRedBlackRatio();

        //Top half red
        double rPctTop = result.getTopTileColor().getRedRatio();
        double bPctTop = result.getTopTileColor().getBlackRatio();
        System.out.println(rPctTop);

        //Bottom half
        double rPctBot = result.getBottomTileColor().getRedRatio();
        double bPctBot = result.getBottomTileColor().getBlackRatio();
        System.out.println(rPctBot);

        //if r+b dominant and there is half tile only has red
        if(rNbPctFull >= 0.99 && ((rPctBot>0.99)||rPctTop>0.99)){
            return true;
        }else{
            return false;
        }
    }


}
