package com.constantbeta.frame.layer;

abstract class MaskLayerBase implements MaskLayer
{
    private final int width;
    private final int height;

    protected MaskLayerBase(int width, int height)
    {
        this.width  = width;
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    @Override
    public abstract boolean isOn(int x, int y);
}
