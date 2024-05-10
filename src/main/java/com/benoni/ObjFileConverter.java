package com.benoni;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ObjFileConverter {

    public static void matrixToFile(Vertex[][] matrix) throws IOException {
        String contents = buildFileContents(matrix);
        File out = new File("terrain.obj");
        if (out.exists()) {
            out.delete();
        }
        out.createNewFile();
        FileWriter fw = new FileWriter(out);
        fw.write(contents);
        fw.close();
    }

    private static String buildFileContents(Vertex[][] matrix) {
        String comment = DiamondSquare.generateExtremaComment(matrix);
        StringBuilder sb_vertexV = new StringBuilder();
        StringBuilder sb_vertexVn = new StringBuilder();
        StringBuilder sb_vertexVt = new StringBuilder();
        StringBuilder sb_faces = new StringBuilder();
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                sb_vertexV.append(matrix[x][y].toWavefrontObjStrV());
                sb_vertexVn.append(matrix[x][y].toWavefrontObjStrVn());
                sb_vertexVt.append(matrix[x][y].toWavefrontObjStrVt());
            }
        }

        for (int y = 0; y < matrix.length - 1; y++) {
            for (int x = 0; x < matrix.length - 1; x++) {
                sb_faces.append("f " + (y * matrix.length + x + 1) + " " + (y * matrix.length + x + matrix.length + 1) + " " + (y * matrix.length + x + matrix.length + 2) + "\n");
                sb_faces.append("f " + (y * matrix.length + x + 1) + " " + (y * matrix.length + x + 2) + " " + (y * matrix.length + x + matrix.length + 2) + "\n");
            }
        }
        return comment + sb_vertexV + /*sb_vertexVt +*/ sb_vertexVn + sb_faces;
    }
}
