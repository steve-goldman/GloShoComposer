package com.constantbeta.frame.layer;

import org.json.JSONObject;

public class ArcLayer extends MaskLayerBase
{
    private final int    centerX;
    private final int    centerY;
    private final double innerRadius;
    private final double outerRadius;
    private final double startAngle;
    private final double endAngle;

    public ArcLayer(int    width,       int    height,
                    int    centerX,     int    centerY,
                    double innerRadius, double outerRadius,
                    double startAngle,  double endAngle)
    {
        super(width, height);

        this.centerX     = centerX;
        this.centerY     = centerY;
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.startAngle  = startAngle;
        this.endAngle    = endAngle;
    }

    public ArcLayer(int width, int height, JSONObject config)
    {
        this(width, height,
                config.getInt("centerX"),
                config.getInt("centerY"),
                config.getDouble("innerRadius"),
                config.getDouble("outerRadius"),
                config.getDouble("startAngle"),
                config.getDouble("endAngle"));
    }

    @Override
    public boolean isOn(int x, int y)
    {
        return isWithinDistance(x, y) && isWithinAngle(x, y);
    }

    private boolean isWithinDistance(int x, int y)
    {
        double distance = getDistance(x, y);
        return distance >= innerRadius && distance < outerRadius;
    }

    private boolean isWithinAngle(int x, int y)
    {
        double pointAngle = getAngle(x, y);

        if (endAngle > startAngle)
        {
            return pointAngle >= startAngle && pointAngle <= endAngle;
        }
        else
        {
            return pointAngle >= startAngle || pointAngle <= endAngle;
        }
    }

    private double getDistance(int x, int y)
    {
        int deltaX = x - centerX;
        int deltaY = centerY - y;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    private double getAngle(int x, int y)
    {
        int    deltaX = x - centerX;
        int    deltaY = centerY - y;
        double angle  = Math.atan2(deltaY, deltaX) * 180 / Math.PI;

        if (angle > 0)
        {
            return angle;
        }
        else
        {
            return angle + 360;
        }
    }
}
