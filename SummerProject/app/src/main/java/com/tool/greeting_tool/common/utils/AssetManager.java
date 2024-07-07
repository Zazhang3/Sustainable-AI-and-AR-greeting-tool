package com.tool.greeting_tool.common.utils;

import com.tool.greeting_tool.R;

public class AssetManager {
    // Arrays to store the resource IDs for the assets
    private static final int[] DATA_LIST = {
            R.drawable.happynewyear, R.drawable.happyhoolidays, R.drawable.getwellsoon, R.drawable.haveaniceday,
            // Add remaining assets here...
    };

    private static final int[] EMOJI_LIST = {
            R.drawable.heart, R.drawable.loveeye, R.drawable.lovesmile, R.drawable.tonge, R.drawable.star,
            // Add remaining assets here...
    };

    private static final int[] ANIMATION_LIST = {
            R.drawable.staranmation, R.drawable.heartanimation,
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
                return "Get well soon";
            case R.drawable.haveaniceday:
                return "Have a nice day";
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
            // Add remaining mappings here...
            default:
                return "Unknown";
        }
    }

    public static String mapResourceIdToAnimation(int resourceId) {
        switch (resourceId) {
            case R.drawable.staranmation:
                return "Star animation";
            case R.drawable.heartanimation:
                return "Heart animation";
            // Add remaining mappings here...
            default:
                return "Unknown";
        }
    }
}
