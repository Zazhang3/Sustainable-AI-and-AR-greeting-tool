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
            R.drawable.empty,R.drawable.heart, R.drawable.loveeye, R.drawable.lovesmile, R.drawable.tonge, R.drawable.star,
            R.drawable.biting, R.drawable.coldsweat, R.drawable.newloveeye, R.drawable.newsmile, R.drawable.partypopper,
            R.drawable.sad, R.drawable.shit, R.drawable.shock, R.drawable.skull, R.drawable.sunglasses, R.drawable.wink
            // Add remaining assets here...
    };

    private static final int[] ANIMATION_LIST = {
            R.drawable.empty,R.drawable.staranmation, R.drawable.heartanimation, R.drawable.butterfly, R.drawable.pinwheel, R.drawable.snow,
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
                return "Happy New Year";
            case R.drawable.happyhoolidays:
                return "Happy Holidays";
            case R.drawable.getwellsoon:
                return "Get Well Soon";
            case R.drawable.haveaniceday:
                return "Have a Nice Day";
            case R.drawable.allwell:
                return "All well";
            case R.drawable.enjoyyourday:
                return "Enjoy your day";
            case R.drawable.goodafternoon:
                return "Good afternoon";
            case R.drawable.goodday:
                return "Good day";
            case R.drawable.goodevening:
                return "Good evening";
            case R.drawable.goodmorning:
                return "Good morning";
            case R.drawable.goodnight:
                return "Good nignt";
            case R.drawable.hello:
                return "Hello";
            case R.drawable.heyfolks:
                return "Hey folks";
            case R.drawable.heythere:
                return "Hey there";
            case R.drawable.keepwell:
                return "Keep well";
            case R.drawable.nicetomeetyou:
                return "Nice to meet you";
            case R.drawable.seeyousoon:
                return "See you soon";
            case R.drawable.thankyou:
                return "Thank you";
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
                return "Love eye";
            case R.drawable.lovesmile:
                return "Love Smile";
            case R.drawable.tonge:
                return "Tongue";
            case R.drawable.star:
                return "Star";
            case R.drawable.biting:
                return "Biting";
            case R.drawable.coldsweat:
                return "Cold Sweat";
            case R.drawable.newloveeye:
                return "New love eye";
            case R.drawable.newsmile:
                return "New Smile";
            case R.drawable.partypopper:
                return "Party Popper";
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
            case R.drawable.empty:
                return "Empty";
            // Add remaining mappings here...
            default:
                return "Unknown";
        }
    }

    public static String mapResourceIdToAnimation(int resourceId) {
        switch (resourceId) {
            case R.drawable.staranmation:
                return "Star Animation";
            case R.drawable.heartanimation:
                return "Heart Animation";
            case R.drawable.butterfly:
                return "Butterfly";
            case R.drawable.pinwheel:
                return "Pinwheel";
            case R.drawable.snow:
                return "Snow";
            case R.drawable.empty:
                return "Empty";
            // Add remaining mappings here...
            default:
                return "Unknown";
        }
    }
}
