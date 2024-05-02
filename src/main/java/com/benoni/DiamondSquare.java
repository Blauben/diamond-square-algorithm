package com.benoni;

import java.util.Random;

public class DiamondSquare {

    private static final Random rand = new Random();
    private static final double roughness = 1.3;
    private static double sigma = 15;

    public static double[][] createLandscape(int size_exp) {
        int size = (int) Math.pow(2, size_exp) + 1;
        double[][] landscape = new double[size][size];
        landscape[0][0] = 0;
        landscape[0][size - 1] = 5;
        landscape[size - 1][0] = 10;
        landscape[size - 1][size - 1] = 5;
        double sigmaTmp = sigma;
        stepEngine(landscape);
        sigma = sigmaTmp;
        return landscape;
    }

    private static void stepEngine(double[][] landscape) {
        for (int stepSize = landscape.length - 1; stepSize > 1; stepSize = stepSize / 2) {
            diamondStep(landscape, stepSize);
            squareStep(landscape, stepSize);
            sigma = sigma / Math.pow(2, roughness);
        }
    }

    private static void diamondStep(double[][] landscape, int stepSize) {
        for (int y = 0; y < landscape[0].length - 1; y = y + stepSize) {
            for (int x = 0; x < landscape.length - 1; x = x + stepSize) {
                double sum = landscape[x][y] + landscape[x + stepSize][y] + landscape[x][y + stepSize] + landscape[x + stepSize][y + stepSize];
                landscape[x + stepSize / 2][y + stepSize / 2] = sum / 4.0 + rand.nextGaussian() * sigma;
            }
        }
    }

    private static void squareStep(double[][] landscape, int stepSize) {
        for (int y = 0; y < landscape[0].length; y = y + stepSize) {
            for (int x = 0; x < landscape.length; x = x + stepSize) {
                squareStepRight(landscape, x, y, stepSize);
                squareStepDown(landscape, x, y, stepSize);
            }
        }
    }

    private static void squareStepRight(double[][] landscape, int x, int y, int stepSize) {
        if (x + stepSize / 2 >= landscape.length) {
            return;
        }

        int delimiter = 4;
        double sum = landscape[x][y];
        if (x + stepSize < landscape.length) {
            sum += landscape[x + stepSize][y];
        } else {
            delimiter--;
        }

        if (x + stepSize / 2 < landscape.length && y - stepSize / 2 >= 0) {
            sum += landscape[x + stepSize / 2][y - stepSize / 2];
        } else {
            delimiter--;
        }

        if (x + stepSize / 2 < landscape.length && y + stepSize / 2 < landscape[0].length) {
            sum += landscape[x + stepSize / 2][y + stepSize / 2];
        } else {
            delimiter--;
        }
        landscape[x + stepSize / 2][y] = sum / delimiter + rand.nextGaussian() * sigma;
    }

    private static void squareStepDown(double[][] landscape, int x, int y, int stepSize) {
        if (y + stepSize / 2 >= landscape[0].length) {
            return;
        }

        int delimiter = 4;
        double sum = landscape[x][y];
        if (y + stepSize < landscape[0].length) {
            sum += landscape[x][y + stepSize];
        } else {
            delimiter--;
        }

        if (x - stepSize / 2 >= 0) {
            sum += landscape[x - stepSize / 2][y + stepSize / 2];
        } else {
            delimiter--;
        }

        if (x + stepSize / 2 < landscape.length) {
            sum += landscape[x + stepSize / 2][y + stepSize / 2];
        } else {
            delimiter--;
        }
        landscape[x][y + stepSize / 2] = sum / delimiter + rand.nextGaussian() * sigma;
    }
}