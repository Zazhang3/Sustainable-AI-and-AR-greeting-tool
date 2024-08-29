package com.tool.utils;

import com.tool.constant.VerificationCodeConstant;

import java.security.SecureRandom;
import java.util.Random;

public class VerificationCodeGenerator {


    private static final Random RANDOM = new SecureRandom();// to keep safe

    public static String generateVerificationCode() {
        StringBuilder code = new StringBuilder(VerificationCodeConstant.CODE_LENGTH);
        for (int i = 0; i < VerificationCodeConstant.CODE_LENGTH; i++) {
            code.append(VerificationCodeConstant.CHARACTERS.charAt(RANDOM.nextInt(VerificationCodeConstant.CHARACTERS.length())));
        }
        return code.toString();
    }

}
