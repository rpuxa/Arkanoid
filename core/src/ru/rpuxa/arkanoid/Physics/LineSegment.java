package ru.rpuxa.arkanoid.Physics;

import com.badlogic.gdx.math.Vector2;

public class LineSegment {
    public Vector2 first, second;

    public LineSegment(Vector2 first, Vector2 second) {
        this.first = first;
        this.second = second;
    }

    public LineSegment(double[] point0, double[] point1) {
        this(new Vector2((float) point0[0], (float) point0[1]), new Vector2((float) point1[0], (float) point1[1]));
    }

    public LineSegment(double[][] points) {
        this(points[0], points[1]);
    }

    public boolean equals(LineSegment lineSegment) {
        return first.equals(lineSegment.first) && second.equals(lineSegment.second);
    }
}
