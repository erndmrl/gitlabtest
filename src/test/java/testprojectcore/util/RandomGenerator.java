package testprojectcore.util;

import org.apache.commons.lang3.RandomStringUtils;


import java.util.Random;


/**
 * @author Eren Demirel
 */
public class RandomGenerator {

    static Random random = new Random();

    private static final String[] strings = {
            RandomStringUtils.randomAlphanumeric(1),
            RandomStringUtils.randomAlphanumeric(2),
            RandomStringUtils.randomAlphanumeric(3),
            RandomStringUtils.randomAlphanumeric(4),
            RandomStringUtils.randomAlphanumeric(5),
            RandomStringUtils.randomAlphanumeric(6),
            RandomStringUtils.randomAlphanumeric(7),
            RandomStringUtils.randomAlphanumeric(8),
            RandomStringUtils.randomAlphanumeric(9),
            RandomStringUtils.randomAlphanumeric(10),
            RandomStringUtils.randomAlphanumeric(11),
            RandomStringUtils.randomAlphanumeric(12),
            RandomStringUtils.randomAlphanumeric(13),
            RandomStringUtils.randomAlphanumeric(14),
            RandomStringUtils.randomAlphanumeric(15),
            RandomStringUtils.randomAlphanumeric(16)};

    private static final int[] ints = {
            random.nextInt((9 - 0) + 1) + 0,
            random.nextInt((99 - 10) + 1) + 10,
            random.nextInt((999 - 100) + 1) + 100,
            random.nextInt((9999 - 1000) + 1) + 1000,
            random.nextInt((99999 - 10000) + 1) + 10000,
            random.nextInt((999999 - 100000) + 1) + 100000,
            random.nextInt((9999999 - 1000000) + 1) + 1000000,
            random.nextInt((99999999 - 10000000) + 1) + 10000000,
            random.nextInt((999999999 - 100000000) + 1) + 100000000,
            random.nextInt(Integer.MAX_VALUE - 1000000000) + 1000000000,
    };

    public static int getADifferentNumberEachTime(int numberOfDigits) {
        int min = (int) Math.pow(10, numberOfDigits - 1);
        int max = (int) Math.pow(10, numberOfDigits);
        int randomInt = random.nextInt(max - min) + min;
        return randomInt;
    }

    public static int getAlwaysTheSameNumberEachTime(int numberOfDigits) {
        return ints[numberOfDigits - 1];
    }

    public static long getADifferentNumberEachTime() {
        long min = Integer.MAX_VALUE - 1;
        long max = Long.MAX_VALUE;
        long randomInt = (Math.abs(random.nextLong()) % (max - min)) + min;
        return randomInt;
    }

    public static String getADifferentStringEachTime(int stringLength) {
        String generatedString = RandomStringUtils.randomAlphanumeric(stringLength);
        return generatedString;
    }

    public static String getAlwaysTheSameStringEachTime(int stringLength) {
        return strings[stringLength - 1];
    }
}
