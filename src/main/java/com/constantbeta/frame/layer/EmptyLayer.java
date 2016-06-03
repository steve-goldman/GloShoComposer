package com.constantbeta.frame.layer;

import org.json.JSONObject;

public class EmptyLayer extends MaskLayerBase
{
    public EmptyLayer(int width, int height)
    {
        super(width, height);
    }

    public EmptyLayer(int width, int height, JSONObject config)
    {
        this(width, height);
    }

    @Override
    public boolean isOn(int x, int y)
    {
        return false;
    }
}
