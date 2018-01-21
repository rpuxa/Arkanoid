package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

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
        float scl = Intersector.intersectRayRay(lineSegment.first, new Vector2(lineSegment.second).sub(lineSegment.first),
                new Vector2((float) vector[0][0], (float) vector[0][1]), new Vector2((float) (vector[1][0]), (float) (vector[1][1])));
        if (scl > 0 && scl <= 1) {
            scl = Intersector.intersectRayRay(new Vector2((float) vector[0][0], (float) vector[0][1]), new Vector2((float) (vector[1][0]), (float) (vector[1][1])),
                    lineSegment.first, new Vector2(lineSegment.second).sub(lineSegment.first));
            if (scl < 0)
                return Float.POSITIVE_INFINITY;
            return scl;
        }
        return Float.POSITIVE_INFINITY;
    }

    public double collision(double[][] vector, double[] ballPos, double velocityAngle, double damage) {
        if (isNull)
            return 0;
        float scl = Intersector.intersectRayRay(new Vector2((float) vector[0][0], (float) vector[0][1]), new Vector2((float) (vector[1][0] - vector[0][0]), (float) (vector[1][1] - vector[0][1])),
                lineSegment.first, new Vector2(lineSegment.second).sub(lineSegment.first));
        if (scl > 1 || scl <= 0)
            return 0;
        if (let != null)
            let.removePoints(-damage, ballPos);
        double angle = Math.atan2(lineSegment.first.y - lineSegment.second.y, lineSegment.first.x - lineSegment.second.x);
        return 2 * angle - velocityAngle;
    }

    public boolean equals(Edge o) {
        return !isNull && o.lineSegment.equals(lineSegment);
    }
}
