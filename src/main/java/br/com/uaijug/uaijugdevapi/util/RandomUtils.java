package br.com.uaijug.uaijugdevapi.util;

import java.util.Random;

public class RandomUtils {
    public static void main(String[] args) {
        String[] myArray = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape"};

        // Picking a random element from the array
        Random random = new Random();
        String randomElement = myArray[random.nextInt(myArray.length)];

        System.out.println(randomElement);

    }
}
