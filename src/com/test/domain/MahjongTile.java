package com.test.domain;

/**
 * Created by Lotti on 16/04/2017.
 */
public enum MahjongTile {

    /**
     * Prevailing wind tiles
     */
    EAST_WIND("东"),
    WEST_WIND("西"),
    SOUTH_WIND("南"),
    NORTH_WIND("北"),

    /**
     * Dragon tiles
     */

    RED_DRAGON("红中"),
    GREEN_DRAGON("青发"),
    WHITE_DRAGON("白板"),

    /**
     * Character suit tiles
     */


    ONE_OF_CHARACTERS("一万"),
    TWO_OF_CHARACTERS("二万"),
    THREE_OF_CHARACTERS("三万"),
    FOUR_OF_CHARACTERS("四万"),
    FIVE_OF_CHARACTERS("五万"),
    SIX_OF_CHARACTERS("六万"),
    SEVEN_OF_CHARACTERS("七万"),
    EIGHT_OF_CHARACTERS("八万"),
    NINE_OF_CHARACTERS("九万"),


    /**
     * Bamboo suit tiles
     */
    ONE_OF_BAMBOOS("一条"),
    TWO_OF_BAMBOOS("二条"),
    THREE_OF_BAMBOOS("三条"),
    FOUR_OF_BAMBOOS("四条"),
    FIVE_OF_BAMBOOS("五条"),
    SIX_OF_BAMBOOS("六条"),
    SEVEN_OF_BAMBOOS("七条"),
    EIGHT_OF_BAMBOOS("八条"),
    NINE_OF_BAMBOOS("九条"),


    /**
     * Circle suit tiles
     */
    ONE_OF_CIRCLES("一饼"),
    TWO_OF_CIRCLES("二饼"),
    THREE_OF_CIRCLES("三饼"),
    FOUR_OF_CIRCLES("四饼"),
    FIVE_OF_CIRCLES("五饼"),
    SIX_OF_CIRCLES("六饼"),
    SEVEN_OF_CIRCLES("七饼"),
    EIGHT_OF_CIRCLES("八饼"),
    NINE_OF_CIRCLES("九饼"),


    /**
     * Flower tiles
     */

    PLUM("梅"),
    ORCHID("兰"),
    BAMBOO("竹"),
    CHRYSANTHEMUM("菊"),

    /**
     * Season tiles
     */

    SPRING("春"),
    SUMMER("夏"),
    AUTUMN("秋"),
    WINTER("冬"),

    /**
     * Miscellaneous tiles
     */
    JOKER("百搭"),
    BACK("牌背"),

    /**
     * Save one to represent unknown
     */
    UNKNOWN("");

    private String chineseChar;

    MahjongTile(String chineseChar) {
        this.chineseChar = chineseChar;
    }

    public String chineseChar() {
        return chineseChar;
    }


}
