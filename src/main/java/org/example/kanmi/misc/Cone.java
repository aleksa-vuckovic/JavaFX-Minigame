package org.example.kanmi.misc;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.example.kanmi.Utils;

public class Cone extends MeshView {

    public Cone(float radius, float height, int divisions) {
        float[] points = new float[(divisions+4)*3]; //2+divisions circle + 2 top and bottom
        for (int i = 0; i < divisions + 2; i++) {
            Point2D p = Utils.getCirclePoint(divisions+2, i, radius, 0);
            points[3*i] = (float)p.getX();
            points[3*i+1] = 0;
            points[3*i+2] = (float)p.getY();
        }
        points[(divisions+2)*3] = 0;
        points[(divisions+2)*3+1] = -height;
        points[(divisions+2)*3+2] = 0;
        points[(divisions+3)*3] = 0;
        points[(divisions+3)*3+1] = 0;
        points[(divisions+3)*3+2] = 0;
        float[] tex = new float[(divisions+4)*2];
        for (int i = 0; i < divisions + 2; i++) {
            tex[2*i] = (float)i/(divisions + 1);
            tex[2*i+1] = 0.5f;
        }
        tex[2*(divisions+2)] = 0.5f;
        tex[2*(divisions+2)+1] = 0;
        tex[2*(divisions+3)] = 0.5f;
        tex[2*(divisions+3)+1] = 1;
        int[] faces = new int[(divisions+2)*2*6];
        for (int i = 0; i < divisions+2; i++) {
            int a = 6*i;
            faces[a] = i; faces[a+1] = faces[a];
            faces[a+2] = (i+1)%(divisions+2); faces[a+3] = faces[a+2];
            faces[a+4] = divisions+2; faces[a+5]=faces[a+4];

            a = (divisions+2)*6+6*i;
            faces[a+2] = i; faces[a+3] = faces[a+2];
            faces[a] = (i+1)%(divisions+2); faces[a+1] = faces[a];
            faces[a+4] = divisions+3; faces[a+5] = faces[a+4];
        }
        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(tex);
        mesh.getFaces().addAll(faces);
        setMesh(mesh);
    }
    public Cone(float radius, float height) {
        this(radius, height, 15);
    }
}
