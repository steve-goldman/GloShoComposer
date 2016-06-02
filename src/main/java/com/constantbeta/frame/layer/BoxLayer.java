package com.constantbeta.frame.layer;

import org.json.JSONObject;

public class BoxLayer extends MaskLayerBase
{
    private final int boxX;
    private final int boxY;
    private final int boxWidth;
    private final int boxHeight;

    public BoxLayer(int width, int height, int boxX, int boxY, int boxWidth, int boxHeight)
    {
        super(width, height);

        this.boxX      = boxX;
        this.boxY      = boxY;
        this.boxWidth  = boxWidth;
        this.boxHeight = boxHeight;
    }

    public BoxLayer(int width, int height, JSONObject config)
    {
        this(width, height,
                config.getInt("boxX"),
                config.getInt("boxY"),
                config.getInt("boxWidth"),
                config.getInt("boxHeight"));
    }

    @Override
    public boolean isOn(int x, int y)
    {
        return x >= boxX && x < boxX + boxWidth && y >= boxY && y < boxY + boxHeight;
    }
}
