package org.example.kanmi.misc;

import javafx.geometry.Point2D;
import javafx.scene.effect.Light;
import javafx.scene.shape.Arc;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Arc3D extends MeshView {

    private static Point2D getArcPoint(double radius, double startAngle, double length, int i, int divisions) {
        double angle = startAngle + (double)i/divisions*length;
        double x = Math.cos(Math.toRadians(angle))*radius;
        double y = -Math.sin(Math.toRadians(angle))*radius;
        return new Point2D(x, y);
    }

    public Arc3D(double outerR, double innerR, double startAngle, double length, double thickness, int divisions) {
        /*
        divisions+1 arc points on the inner and outer radius, on both sides of the arc. = (divisons+1)*4
         */
        float[] points = new float[4*(divisions+1) * 3];
        float[] tex = new float[4*(divisions+1) * 2];
        final int OUTER_FRONT =     0*(divisions+1);
        final int OUTER_BACK =      1*(divisions+1);
        final int INNER_FRONT =     2*(divisions+1);
        final int INNER_BACK =      3*(divisions+1);
        for (int i = 0; i <= divisions; i++) {
            Point2D outer = getArcPoint(outerR, startAngle, length, i, divisions);
            Point2D inner = getArcPoint(innerR, startAngle, length, i, divisions);
            Point2D texouter = outer.multiply(0.5/outerR).add(0.5, 0.5);
            Point2D texinner = inner.multiply(0.5/outerR).add(0.5, 0.5);
            points[(OUTER_FRONT+i)*3 + 0] = (float)outer.getX();
            points[OUTER_FRONT*3 + i*3 + 1] = (float)outer.getY();
            points[OUTER_FRONT*3 + i*3 + 2] = (float)-thickness/2;
            points[OUTER_BACK*3 + i*3 + 0] = (float)outer.getX();
            points[OUTER_BACK*3 + i*3 + 1] = (float)outer.getY();
            points[OUTER_BACK*3 + i*3 + 2] = (float)+thickness/2;
            points[INNER_FRONT*3 + i*3 + 0] = (float)inner.getX();
            points[INNER_FRONT*3 + i*3 + 1] = (float)inner.getY();
            points[INNER_FRONT*3 + i*3 + 2] = (float)-thickness/2;
            points[INNER_BACK*3 + i*3 + 0] = (float)inner.getX();
            points[INNER_BACK*3 + i*3 + 1] = (float)inner.getY();
            points[INNER_BACK*3 + i*3 + 2] = (float)+thickness/2;
            tex[(OUTER_FRONT+i)*2 + 0] = (float)texouter.getX();
            tex[(OUTER_FRONT+i)*2 + 1] = (float)texouter.getY();
            tex[(OUTER_BACK+i)*2 + 0] = (float)texouter.getX();
            tex[(OUTER_BACK+i)*2 + 1] = (float)texouter.getY();
            tex[(INNER_FRONT+i)*2 + 0] = (float)texinner.getX();
            tex[(INNER_FRONT+i)*2 + 1] = (float)texinner.getY();
            tex[(INNER_BACK+i)*2 + 0] = (float)texinner.getX();
            tex[(INNER_BACK+i)*2 + 1] = (float)texinner.getY();
        }
        /*
        For each point on the outer arc except for the last - 2 faces, times 2 sides (front and back).
        For each point on the outer arc except for the last - 2 faces between the sides.
            The same applies for the inner arc.
        Finally 2 faces for each end of the arc.
         */
        int[] faces = new int[(divisions*2*2*2+4)*6];
        int cur = 0;
        //
        for (int i = 0; i < divisions; i++) {
            faces[cur++] = OUTER_FRONT+i;   faces[cur++] = OUTER_FRONT+i;
            faces[cur++] = INNER_FRONT+i+1; faces[cur++] = INNER_FRONT+i+1;
            faces[cur++] = INNER_FRONT+i;   faces[cur++] = INNER_FRONT+i;
            faces[cur++] = OUTER_FRONT+i;   faces[cur++] = OUTER_FRONT+i;
            faces[cur++] = OUTER_FRONT+i+1; faces[cur++] = OUTER_FRONT+i+1;
            faces[cur++] = INNER_FRONT+i+1; faces[cur++] = INNER_FRONT+i+1;

            faces[cur++] = OUTER_BACK+i;   faces[cur++] = OUTER_BACK+i;
            faces[cur++] = INNER_BACK+i;   faces[cur++] = INNER_BACK+i;
            faces[cur++] = INNER_BACK+i+1; faces[cur++] = INNER_BACK+i+1;
            faces[cur++] = OUTER_BACK+i;   faces[cur++] = OUTER_BACK+i;
            faces[cur++] = INNER_BACK+i+1; faces[cur++] = INNER_BACK+i+1;
            faces[cur++] = OUTER_BACK+i+1; faces[cur++] = OUTER_BACK+i+1;

            faces[cur++] = OUTER_FRONT+i;   faces[cur++] = OUTER_FRONT+i;
            faces[cur++] = OUTER_BACK+i;    faces[cur++] = OUTER_BACK+i;
            faces[cur++] = OUTER_BACK+i+1;  faces[cur++] = OUTER_BACK+i+1;
            faces[cur++] = OUTER_FRONT+i;   faces[cur++] = OUTER_FRONT+i;
            faces[cur++] = OUTER_BACK+i+1;  faces[cur++] = OUTER_BACK+i+1;
            faces[cur++] = OUTER_FRONT+i+1; faces[cur++] = OUTER_FRONT+i+1;

            faces[cur++] = INNER_FRONT+i;   faces[cur++] = INNER_FRONT+i;
            faces[cur++] = INNER_BACK+i+1;  faces[cur++] = INNER_BACK+i+1;
            faces[cur++] = INNER_BACK+i;    faces[cur++] = INNER_BACK+i;
            faces[cur++] = INNER_FRONT+i;   faces[cur++] = INNER_FRONT+i;
            faces[cur++] = INNER_FRONT+i+1; faces[cur++] = INNER_FRONT+i+1;
            faces[cur++] = INNER_BACK+i+1;  faces[cur++] = INNER_BACK+i+1;
        }

        faces[cur++] = OUTER_FRONT; faces[cur++] = OUTER_FRONT;
        faces[cur++] = INNER_FRONT; faces[cur++] = INNER_FRONT;
        faces[cur++] = INNER_BACK;  faces[cur++] = INNER_BACK;
        faces[cur++] = OUTER_FRONT; faces[cur++] = OUTER_FRONT;
        faces[cur++] = INNER_BACK;  faces[cur++] = INNER_BACK;
        faces[cur++] = OUTER_BACK;  faces[cur++] = OUTER_BACK;

        faces[cur++] = OUTER_FRONT+divisions;   faces[cur++] = OUTER_FRONT+divisions;
        faces[cur++] = INNER_BACK+divisions;    faces[cur++] = INNER_BACK+divisions;
        faces[cur++] = INNER_FRONT+divisions;   faces[cur++] = INNER_FRONT+divisions;
        faces[cur++] = OUTER_FRONT+divisions;   faces[cur++] = OUTER_FRONT+divisions;
        faces[cur++] = OUTER_BACK+divisions;    faces[cur++] = OUTER_BACK+divisions;
        faces[cur++] = INNER_BACK+divisions;    faces[cur++] = INNER_BACK+divisions;

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(tex);
        mesh.getFaces().addAll(faces);

        setMesh(mesh);
    }
}
