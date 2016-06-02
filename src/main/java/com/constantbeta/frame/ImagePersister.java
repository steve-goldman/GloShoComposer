package com.constantbeta.frame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class ImagePersister
{
    private final ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
    private final int[] nBits = { 8 };
    private final ColorModel colorModel
            = new ComponentColorModel(colorSpace, nBits, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
    private final SampleModel sampleModel;

    public ImagePersister(int width, int height)
    {
        this.sampleModel = colorModel.createCompatibleSampleModel(width, height);
    }

    public void persist(File file, byte[] buffer)
    {
        DataBufferByte dataBuffer = new DataBufferByte(buffer, buffer.length, 0);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, null);
        BufferedImage result = new BufferedImage(colorModel, raster, false, null);

        try
        {
            ImageIO.write(result, "PNG", file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
