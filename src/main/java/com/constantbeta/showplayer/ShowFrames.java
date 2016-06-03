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
    private final int[]       transitionTimes;
    private final FrameMask[] frameMasks;

    public ShowFrames(int[] transitionTimes, FrameMask[] frameMasks)
    {
        assert transitionTimes.length == frameMasks.length;

        this.transitionTimes = transitionTimes;
        this.frameMasks = frameMasks;
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
            return frameMasks[frameMasks.length - 1];
        }

        int i = Arrays.binarySearch(transitionTimes, elapsedTime);

        if (i >= 0)
        {
            return frameMasks[i + 1];
        }
        else
        {
            return frameMasks[-(i + 1)];
        }
    }

    public int getShowDuration()
    {
        return transitionTimes[transitionTimes.length - 1];
    }

    public int getWidth()
    {
        return frameMasks[0].getWidth();
    }

    public int getHeight()
    {
        return frameMasks[0].getHeight();
    }

    public static class Builder
    {
        private final ArrayList<Integer>   transitionTimesList = new ArrayList<>();
        private final ArrayList<FrameMask> frameMasksList      = new ArrayList<>();

        public Builder addJson(JSONObject config)
        {
            int       width           = config.getInt("width");
            int       height          = config.getInt("height");
            JSONArray framesJsonArray = config.getJSONArray("frames");

            for (int i = 0; i < framesJsonArray.length(); i++)
            {
                JSONObject frameJson       = framesJsonArray.getJSONObject(i);
                int        duration        = frameJson.getInt("duration");
                JSONObject maskLayerConfig = frameJson.getJSONObject("maskLayer");
                MaskLayer  maskLayer       = MaskLayerFactory.create(width, height, maskLayerConfig);
                FrameMask  frameMask       = new FrameMask(width, height, maskLayer);

                addFrame(frameMask, duration);
            }

            return this;
        }

        public Builder addFrame(FrameMask frameMask, int duration)
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
            frameMasksList.add(frameMask);

            return this;
        }

        public ShowFrames build()
        {
            int[]       transitionTimes = new int[transitionTimesList.size()];
            FrameMask[] frameMasks      = new FrameMask[frameMasksList.size()];

            for (int i = 0; i < transitionTimes.length; i++)
            {
                transitionTimes[i] = transitionTimesList.get(i);
                frameMasks[i]      = frameMasksList.get(i);
            }

            return new ShowFrames(transitionTimes, frameMasks);
        }
    }
}
