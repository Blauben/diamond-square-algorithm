package com.benoni;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Vertex[][] matrix = DiamondSquare.createLandscape(5);
        try {
            ObjFileConverter.matrixToFile(matrix);
        } catch (IOException e) {
            System.out.println("File Error!\n" + e.getMessage());
        }
    }
}