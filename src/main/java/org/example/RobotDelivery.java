package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RobotDelivery {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        int numThreads = 1000;

        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countR = countLetter(route, 'R');
                updateSizeToFreq(countR);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        printResults();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countLetter(String route, char letter) {
        int count = 0;
        for (int i = 0; i < route.length(); i++) {
            if (route.charAt(i) == letter) {
                count++;
            }
        }
        return count;
    }

    public static void updateSizeToFreq(int countR) {
        synchronized (sizeToFreq) {
            sizeToFreq.merge(countR, 1, Integer::sum);
        }
    }

    public static void printResults() {
        int mostFrequent = 0;
        int mostFrequentCount = 0;
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int count = entry.getKey();
            int frequency = entry.getValue();

            if (frequency > mostFrequentCount) {
                mostFrequentCount = frequency;
                mostFrequent = count;
            }

        }
        System.out.println("\nСамое частое количество повторений: " + mostFrequent + " (встретилось " + mostFrequentCount + " раз)");
        System.out.println("Другие размеры : ");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            int count = entry.getKey();
            int frequency = entry.getValue();

            System.out.println("- " + count + " (встретилось " + frequency + " раз)");


        }
    }
}