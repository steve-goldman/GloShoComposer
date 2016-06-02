package com.constantbeta.frame;

import com.constantbeta.frame.layer.*;

import java.util.Arrays;

public class FrameMask
{
    private final int    width;
    private final int    height;
    private final byte[] array;

    public static final byte OFF = 0;
    public static final byte ON  = (byte)0xFF;

    public FrameMask(int width, int height, MaskLayer maskLayer)
    {
        this.width  = width;
        this.height = height;
        this.array  = new byte[width * height];

        Arrays.fill(array, ON);
        apply(maskLayer);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    byte[] getArray()
    {
        return array;
    }

    private void apply(MaskLayer maskLayer)
    {
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (getAt(x, y) == ON && !maskLayer.isOn(x, y))
                {
                    setAt(x, y, OFF);
                }
            }
        }
    }

    private byte getAt(int x, int y)
    {
        return array[index(x, y)];
    }

    private void setAt(int x, int y, byte value)
    {
        array[index(x, y)] = value;
    }

    private int index(int x, int y)
    {
        return width * y + x;
    }
}
