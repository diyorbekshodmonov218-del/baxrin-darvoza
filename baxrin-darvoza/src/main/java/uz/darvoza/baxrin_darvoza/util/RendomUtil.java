package uz.darvoza.baxrin_darvoza.util;

import java.util.Random;

public class RendomUtil {
    public static String getRandomSmsCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000); // 100000 dan 999999 gacha
        return String.valueOf(code);
    }
}
