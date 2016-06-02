package com.constantbeta.frame.layer;

import org.json.JSONObject;

public class CircleLayer extends MaskLayerBase
{
    private final ArcLayer delegate;

    public CircleLayer(int width, int height, int centerX, int centerY, double innerRadius, double outerRadius)
    {
        super(width, height);

        this.delegate = new ArcLayer(width, height, centerX, centerY, innerRadius, outerRadius, 0, 2 * Math.PI);
    }

    public CircleLayer(int width, int height, JSONObject config)
    {
        this(width, height,
                config.getInt("centerX"),
                config.getInt("centerY"),
                config.getDouble("innerRadius"),
                config.getDouble("outerRadius"));
    }

    @Override
    public boolean isOn(int x, int y)
    {
        return delegate.isOn(x, y);
    }
}
