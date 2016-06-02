package com.constantbeta.frame.layer;

import org.json.JSONObject;

public class InvertLayer extends MaskLayerBase
{
    private final MaskLayer maskLayer;

    public InvertLayer(int width, int height, MaskLayer maskLayer)
    {
        super(width, height);

        this.maskLayer = maskLayer;
    }

    public InvertLayer(int width, int height, JSONObject config)
    {
        this(width, height, MaskLayerFactory.create(width, height, config.getJSONObject("maskLayer")));
    }

    @Override
    public boolean isOn(int x, int y)
    {
        return !maskLayer.isOn(x, y);
    }
}
