package com.emm.util.generate;

public class RandomTools {
    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static String randomIntString(int min, int max, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(randomInt(min, max));
        }
        return new String(sb);
    }

    public static double randomDouble(double min, double max) {
        return (Math.random() * (max - min + 1) + min);
    }

}
