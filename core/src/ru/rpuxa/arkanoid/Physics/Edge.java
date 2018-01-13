package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.math.*;

import java.io.File;

public class Edge {

    public LineSegment lineSegment;
    public Let let;
    boolean isNull;

    public Edge(LineSegment lineSegment, Let let) {
        this.lineSegment = lineSegment;
        this.let = let;
    }

    Edge() {
        isNull = true;
    }

    float distance(double[][] vector) {
        if (isNull)
            return Float.POSITIVE_INFINITY;
        float scl = Intersector.intersectRayRay(new Vector2((float) vector[0][0], (float) vector[0][1]), new Vector2((float) vector[1][0], (float) vector[1][1]), lineSegment.first, lineSegment.second);
        if (scl > 0 && scl <= 1) {
            scl = Intersector.intersectRayRay(new Vector2((float) vector[0][0], (float) vector[0][1]), new Vector2((float) vector[1][0], (float) vector[1][1]), lineSegment.second, lineSegment.first);
            if (scl < 0)
                return Float.POSITIVE_INFINITY;
            return scl * (sqr(vector[1][0]) + sqr(vector[1][1]));
        }
        return Float.POSITIVE_INFINITY;
    }

    private float sqr(double x) {
        return (float) (x * x);
    }

    public double collusion(double[][] vector, double[] ballPos) {
        if (isNull)
            return 0;
        float scl = Intersector.intersectRayRay(new Vector2((float) vector[0][0], (float) vector[0][1]), new Vector2((float) vector[1][0], (float) vector[1][1]), lineSegment.second, lineSegment.first);
        if (scl > 1 || scl <= 0)
            return 0;
        if (let != null)
            let.removePoints(-1, ballPos);
        return Math.atan2(vector[1][1] - vector[0][1], vector[1][0] - vector[0][0]) - lineSegment.second.angleRad(lineSegment.first);
    }

    public boolean equals(Edge o) {
        return !isNull && o.lineSegment.equals(lineSegment);
    }
}
