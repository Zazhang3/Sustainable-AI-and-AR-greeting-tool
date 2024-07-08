package com.tool.greeting_tool.common.utils;

import com.tool.greeting_tool.R;

public class AssetManager {
    // Arrays to store the resource IDs for the assets
    private static final int[] DATA_LIST = {
            R.drawable.happynewyear, R.drawable.happyhoolidays, R.drawable.getwellsoon, R.drawable.haveaniceday,
            R.drawable.allwell, R.drawable.enjoyyourday, R.drawable.goodafternoon, R.drawable.goodday,R.drawable.goodevening,
            R.drawable.goodmorning, R.drawable.goodnight, R.drawable.hello, R.drawable.heyfolks,R.drawable.heythere,
            R.drawable.keepwell, R.drawable.nicetomeetyou, R.drawable.seeyousoon, R.drawable.thankyou
            // Add remaining assets here...
    };

    private static final int[] EMOJI_LIST = {
            R.drawable.heart, R.drawable.loveeye, R.drawable.lovesmile, R.drawable.tonge, R.drawable.star,
            R.drawable.biting, R.drawable.coldsweat, R.drawable.newloveeye, R.drawable.newsmile, R.drawable.partypopper,
            R.drawable.sad, R.drawable.shit, R.drawable.shock, R.drawable.skull, R.drawable.sunglasses, R.drawable.wink
            // Add remaining assets here...
    };

    private static final int[] ANIMATION_LIST = {
            R.drawable.staranmation, R.drawable.heartanimation, R.drawable.butterfly, R.drawable.pinwheel, R.drawable.snow
            // Add remaining assets here...
    };

    // Methods to retrieve the asset arrays
    public static int[] getDataList() {
        return DATA_LIST;
    }

    public static int[] getEmojiList() {
        return EMOJI_LIST;
    }

    public static int[] getAnimationList() {
        return ANIMATION_LIST;
    }

    // Methods to map resource IDs to their corresponding string values
    public static String mapResourceIdToText(int resourceId) {
        switch (resourceId) {
            case R.drawable.happynewyear:
                return "HappyNewYear";
            case R.drawable.happyhoolidays:
                return "HappyHolidays";
            case R.drawable.getwellsoon:
                return "GetWellSoon";
            case R.drawable.haveaniceday:
                return "HaveaNiceDay";
            case R.drawable.allwell:
                return "Allwell";
            case R.drawable.enjoyyourday:
                return "Enjoyyourday";
            case R.drawable.goodafternoon:
                return "Goodfternoon";
            case R.drawable.goodday:
                return "Goodday";
            case R.drawable.goodevening:
                return "Goodevening";
            case R.drawable.goodmorning:
                return "Goodmorning";
            case R.drawable.goodnight:
                return "Goodnignt";
            case R.drawable.hello:
                return "Hello";
            case R.drawable.heyfolks:
                return "Heyfolks";
            case R.drawable.heythere:
                return "Heythere";
            case R.drawable.keepwell:
                return "Keepwell";
            case R.drawable.nicetomeetyou:
                return "Nicetomeetyou";
            case R.drawable.seeyousoon:
                return "Seeyousoon";
            case R.drawable.thankyou:
                return "Thankyou";
            // Add remaining mappings here...
            default:
                return "Unknown";
        }
    }

    public static String mapResourceIdToEmoji(int resourceId) {
        switch (resourceId) {
            case R.drawable.heart:
                return "Heart";
            case R.drawable.loveeye:
                return "LoveEye";
            case R.drawable.lovesmile:
                return "LoveSmile";
            case R.drawable.tonge:
                return "Tongue";
            case R.drawable.star:
                return "Star";
            case R.drawable.biting:
                return "Biting";
            case R.drawable.coldsweat:
                return "ColdSweat";
            case R.drawable.newloveeye:
                return "Newloveeye";
            case R.drawable.newsmile:
                return "NewSmile";
            case R.drawable.partypopper:
                return "PartyPopper";
            case R.drawable.sad:
                return "Sad";
            case R.drawable.shit:
                return "Shit";
            case R.drawable.shock:
                return "Shock";
            case R.drawable.skull:
                return "Skull";
            case R.drawable.sunglasses:
                return "Sunglasses";
            case R.drawable.wink:
                return "Wink";
            // Add remaining mappings here...
            default:
                return "Unknown";
        }
    }

    public static String mapResourceIdToAnimation(int resourceId) {
        switch (resourceId) {
            case R.drawable.staranmation:
                return "StarAnimation";
            case R.drawable.heartanimation:
                return "HeartAnimation";
            case R.drawable.butterfly:
                return "Butterfly";
            case R.drawable.pinwheel:
                return "Pinwheel";
            case R.drawable.snow:
                return "Snow";
            // Add remaining mappings here...
            default:
                return "Unknown";
        }
    }
}
