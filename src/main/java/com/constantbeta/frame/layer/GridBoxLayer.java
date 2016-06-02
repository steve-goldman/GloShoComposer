package com.constantbeta.frame.layer;

import org.json.JSONObject;

public class GridBoxLayer extends MaskLayerBase
{
    private final BoxLayer delegateLayer;

    public GridBoxLayer(int width, int height, int numRows, int numColumns, int row, int column)
    {
        super(width, height);

        this.delegateLayer = new BoxLayer(
                width, height,
                column * width  / numColumns,
                row    * height / numRows,
                width           / numColumns,
                height          / numRows);
    }

    public GridBoxLayer(int width, int height, JSONObject config)
    {
        this(width, height,
                config.getInt("numRows"),
                config.getInt("numColumns"),
                config.getInt("row"),
                config.getInt("column"));
    }

    @Override
    public boolean isOn(int x, int y)
    {
        return delegateLayer.isOn(x, y);
    }
}
