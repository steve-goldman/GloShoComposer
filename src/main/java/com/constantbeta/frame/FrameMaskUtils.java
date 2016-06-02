package com.constantbeta.frame;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class FrameMaskUtils
{
    private static final ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
    private static final int[]      nBits      = { 8 };
    private static final ColorModel colorModel = new ComponentColorModel(
            colorSpace,
            nBits,
            false,
            true,
            Transparency.OPAQUE,
            DataBuffer.TYPE_BYTE);

    public static BufferedImage toBufferedImage(FrameMask frameMask)
    {
        byte[]         buffer      = frameMask.getArray();
        SampleModel sampleModel = colorModel.createCompatibleSampleModel(frameMask.getWidth(), frameMask.getHeight());
        DataBufferByte dataBuffer  = new DataBufferByte(buffer, buffer.length, 0);
        WritableRaster raster      = Raster.createWritableRaster(sampleModel, dataBuffer, null);
        return new BufferedImage(colorModel, raster, false, null);
    }

    public static javafx.scene.image.Image toFxImage(FrameMask frameMask)
    {
        BufferedImage src  = toBufferedImage(frameMask);
        WritableImage dest = new WritableImage(frameMask.getWidth(), frameMask.getHeight());

        return SwingFXUtils.toFXImage(src, dest);
    }

    public static void persist(FrameMask frameMask, File file)
    {
        try
        {
            ImageIO.write(toBufferedImage(frameMask), "PNG", file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
