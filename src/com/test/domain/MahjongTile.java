package com.test.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lotti on 16/04/2017.
 */
public enum MahjongTile {

    /**
     * Prevailing wind tiles
     */
    EAST_WIND("东" ,"zi_dong"),
    WEST_WIND("西","zi_xi"),
    SOUTH_WIND("南","zi_nan"),
    NORTH_WIND("北","zi_bei"),

    /**
     * Dragon tiles
     */

    RED_DRAGON("红中","zi_zhong"),
    GREEN_DRAGON("青发","zi_fa"),
    WHITE_DRAGON("白板","zi_bai"),

    /**
     * Character suit tiles
     */


    ONE_OF_CHARACTERS("一万","wan1"),
    TWO_OF_CHARACTERS("二万","wan2"),
    THREE_OF_CHARACTERS("三万","wan3"),
    FOUR_OF_CHARACTERS("四万","wan4"),
    FIVE_OF_CHARACTERS("五万","wan5"),
    SIX_OF_CHARACTERS("六万","wan6"),
    SEVEN_OF_CHARACTERS("七万","wan7"),
    EIGHT_OF_CHARACTERS("八万","wan8"),
    NINE_OF_CHARACTERS("九万","wan9"),


    /**
     * Bamboo suit tiles
     */
    ONE_OF_BAMBOOS("一条","tiao1"),
    TWO_OF_BAMBOOS("二条","tiao2"),
    THREE_OF_BAMBOOS("三条","tiao3"),
    FOUR_OF_BAMBOOS("四条","tiao4"),
    FIVE_OF_BAMBOOS("五条","tiao5"),
    SIX_OF_BAMBOOS("六条","tiao6"),
    SEVEN_OF_BAMBOOS("七条","tiao7"),
    EIGHT_OF_BAMBOOS("八条","tiao8"),
    NINE_OF_BAMBOOS("九条","tiao9"),


    /**
     * Circle suit tiles
     */
    ONE_OF_CIRCLES("一饼","tong1"),
    TWO_OF_CIRCLES("二饼","tong2"),
    THREE_OF_CIRCLES("三饼","tong3"),
    FOUR_OF_CIRCLES("四饼","tong4"),
    FIVE_OF_CIRCLES("五饼","tong5"),
    SIX_OF_CIRCLES("六饼","tong6"),
    SEVEN_OF_CIRCLES("七饼","tong7"),
    EIGHT_OF_CIRCLES("八饼","tong8"),
    NINE_OF_CIRCLES("九饼","tong9"),


    /**
     * Flower tiles
     */

    PLUM("梅",""),
    ORCHID("兰",""),
    BAMBOO("竹",""),
    CHRYSANTHEMUM("菊",""),

    /**
     * Season tiles
     */

    SPRING("春",""),
    SUMMER("夏",""),
    AUTUMN("秋",""),
    WINTER("冬",""),

    /**
     * Miscellaneous tiles
     */
    JOKER("百搭",""),
    BACK("牌背",""),

    /**
     * Save one to represent unknown
     */
    UNKNOWN("","");

    private String chineseChar;
    private String libFileIdentifier;

    MahjongTile(String chineseChar, String libFileIdentifier) {
        this.libFileIdentifier = libFileIdentifier;
        this.chineseChar = chineseChar;
    }

    public String chineseChar() {
        return chineseChar;
    }

    public String fileIdentifier() {
        return libFileIdentifier;
    }

    /**
     *
     * @param fileName: including extensions, must in png format
     * @return
     */
    public MahjongTile findByFileName(String fileName){
        //TODO: check and handle the extension type
        return lookup.get(fileName);
    }

    // Reverse-lookup map from file name to tile identity
    private static final Map<String, MahjongTile> lookup = new HashMap<String, MahjongTile>();

    static {
        for (MahjongTile tile : MahjongTile.values()) {
            lookup.put(tile.libFileIdentifier + ".png", tile);
        }
    }

}
