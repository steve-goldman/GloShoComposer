package com.constantbeta.frame.layer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class IntersectLayer extends MaskLayerBase
{
    private final MaskLayer[] maskLayers;

    public IntersectLayer(int width, int height, MaskLayer[] maskLayers)
    {
        super(width, height);

        this.maskLayers = maskLayers;
    }

    public IntersectLayer(int width, int height, JSONObject config)
    {
        this(width, height, getMaskLayers(width, height, config));
    }

    @Override
    public boolean isOn(int x, int y)
    {
        for (MaskLayer maskLayer : maskLayers)
        {
            if (!maskLayer.isOn(x, y))
            {
                return false;
            }
        }

        return true;
    }

    private static MaskLayer[] getMaskLayers(int width, int height, JSONObject config)
    {
        ArrayList<MaskLayer> maskLayers = new ArrayList<>();
        JSONArray            jsonArray  = config.getJSONArray("maskLayers");

        for (int i = 0; i < jsonArray.length(); i++)
        {
            maskLayers.add(MaskLayerFactory.create(width, height, jsonArray.getJSONObject(i)));
        }

        return maskLayers.toArray(new MaskLayer[maskLayers.size()]);
    }
}
