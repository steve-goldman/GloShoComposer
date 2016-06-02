package com.constantbeta.frame.layer;

import org.json.JSONObject;

import java.util.ArrayList;

public class CheckerboardLayer extends MaskLayerBase
{
    private final UnionLayer delegateLayer;

    public CheckerboardLayer(int width, int height, int numRows, int numColumns, boolean topLeftOn)
    {
        super(width, height);

        ArrayList<GridBoxLayer> gridBoxLayers = new ArrayList<>();

        for (int row = 0; row < numRows; row++)
        {
            for (int column = firstOnColumn(row, topLeftOn); column < numColumns; column += 2)
            {
                gridBoxLayers.add(new GridBoxLayer(width, height, numRows, numColumns, row, column));
            }
        }

        this.delegateLayer = new UnionLayer(width, height, gridBoxLayers.toArray(new GridBoxLayer[gridBoxLayers.size()]));
    }

    public CheckerboardLayer(int width, int height, JSONObject config)
    {
        this(width, height,
                config.getInt("numRows"),
                config.getInt("numColumns"),
                config.getBoolean("topLeftOn"));
    }

    @Override
    public boolean isOn(int x, int y)
    {
        return delegateLayer.isOn(x, y);
    }

    private int firstOnColumn(int row, boolean topLeftOn)
    {
        if ((isEven(row) && topLeftOn) || (!isEven(row) && !topLeftOn))
        {
            return 0;
        }
        return 1;
    }

    private boolean isEven(int row)
    {
        return 0 == row % 2;
    }
}
