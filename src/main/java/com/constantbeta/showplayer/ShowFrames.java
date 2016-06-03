package com.constantbeta.showplayer;

import com.constantbeta.frame.FrameMask;
import com.constantbeta.frame.layer.MaskLayer;
import com.constantbeta.frame.layer.MaskLayerFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ShowFrames
{
    private final int         width;
    private final int         height;
    private final int[]       transitionTimes;
    private final MaskLayer[] maskLayers;
    private final FrameMask[] frameMasks;

    public ShowFrames(int width, int height, int[] transitionTimes, MaskLayer[] maskLayers)
    {
        assert transitionTimes.length == maskLayers.length;

        this.width           = width;
        this.height          = height;
        this.transitionTimes = transitionTimes;
        this.maskLayers      = maskLayers;
        this.frameMasks      = new FrameMask[maskLayers.length];
    }

    public static ShowFrames fromJson(JSONObject config)
    {
        Builder builder = new Builder();
        builder.addJson(config);
        return builder.build();
    }

    public FrameMask getFrameFor(int elapsedTime)
    {
        if (elapsedTime >= getShowDuration())
        {
            return getFrame(frameMasks.length - 1);
        }

        int i = Arrays.binarySearch(transitionTimes, elapsedTime);

        if (i >= 0)
        {
            return getFrame(i + 1);
        }
        else
        {
            return getFrame(-(i + 1));
        }
    }

    public int getShowDuration()
    {
        return transitionTimes[transitionTimes.length - 1];
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    private FrameMask getFrame(int i)
    {
        if (null == frameMasks[i])
        {
            frameMasks[i] = new FrameMask(width, height, maskLayers[i]);
        }

        return frameMasks[i];
    }

    public static class Builder
    {
        private int                        width;
        private int                        height;
        private final ArrayList<Integer>   transitionTimesList = new ArrayList<>();
        private final ArrayList<MaskLayer> maskLayersList      = new ArrayList<>();

        public Builder addJson(JSONObject config)
        {
            width                     = config.getInt("width");
            height                    = config.getInt("height");
            JSONArray framesJsonArray = config.getJSONArray("frames");

            for (int i = 0; i < framesJsonArray.length(); i++)
            {
                JSONObject frameJson       = framesJsonArray.getJSONObject(i);
                int        duration        = frameJson.getInt("duration");
                JSONObject maskLayerConfig = frameJson.getJSONObject("maskLayer");
                MaskLayer  maskLayer       = MaskLayerFactory.create(width, height, maskLayerConfig);

                addFrame(maskLayer, duration);
            }

            return this;
        }

        public Builder addFrame(MaskLayer maskLayer, int duration)
        {
            final int lastTransitionTime;
            if (transitionTimesList.isEmpty())
            {
                lastTransitionTime = 0;
            }
            else
            {
                lastTransitionTime = transitionTimesList.get(transitionTimesList.size() - 1);
            }

            transitionTimesList.add(lastTransitionTime + duration);
            maskLayersList.add(maskLayer);

            return this;
        }

        public Builder setWidth(int width)
        {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height)
        {
            this.height = height;
            return this;
        }

        public ShowFrames build()
        {
            if (width == 0 || height == 0 || transitionTimesList.isEmpty())
            {
                throw new IllegalStateException();
            }

            int[]       transitionTimes = new int[transitionTimesList.size()];
            MaskLayer[] maskLayers      = new MaskLayer[maskLayersList.size()];

            for (int i = 0; i < transitionTimes.length; i++)
            {
                transitionTimes[i] = transitionTimesList.get(i);
                maskLayers[i]      = maskLayersList.get(i);
            }

            return new ShowFrames(width, height, transitionTimes, maskLayers);
        }
    }
}
