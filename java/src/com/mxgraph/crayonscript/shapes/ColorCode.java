package com.mxgraph.crayonscript.shapes;

import java.awt.*;

public enum ColorCode {

    // https://pin.it/3ThtK6U
    DEFAULT_COLOR("#E4DB8C"),
    FUCHSIA("#C364C5"),
    MAROON("#C8385A"),
    NEON_CARROT("#FFA343"),
    JUNGLE_GREEN("#3BB08F"),
    SHOCKING_PINK("#FB7EFD"),
    RADICAL_RED("#FF496C"),
    SUN_GLOW("#FFCF48"),
    TROPICAL_RAIN_FOREST("#17806D"),
    PURPLE_PIZAZZ("#FF1DCE"),
    WILD_WATERMELON("#FC6C85"),
    LASER_LEMON("#FDFC74"),
    PINE_GREEN("#158078"),
    HOT_MAGENTA("#FF1DCE"),
    VIVID_TANGERINE("#FFA089"),
    UN_MELLOW_YELLOW("#FDFC74"),
    NAVY_BLUE("#1974D2"),
    RAZZLE_DAZZLE_ROSE("#FF48D0"),
    COPPER("#DD9475"),
    INCH_WORM("#B2EC5D"),
    DENIM("#2B6CC4"),
    CERISE("#FF43A4"),
    ATOMIC_TANGERINE("#FFA474"),
    SCREAMIN_GREEN("#76FF7A"),
    MIDNIGHT_BLUE("#1A4876"),
    JAZZ_BERRY_JAM("#CA3767"),
    OUTRAGEOUS_ORANGE("#FF6E4A"),
    ELECTRIC_LIME("#1DF914"),
    WILD_BLUE_WONDER("#A2ADD0"),
    RAZZMATAZZ("#E3256B"),
    MANGO_TANGO("#FF8243"),
    SHAMROCK("#45CEA2"),
    ROYAL_PURPLE("#7851A9");

    public String hexCode;

    public Color color;

    ColorCode(String hexCode) {
        this.hexCode = hexCode;
        this.color = Color.decode("0x" + hexCode.replace("#", ""));
    }
}
