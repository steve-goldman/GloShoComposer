package com.constantbeta.frame.layer;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MaskLayerFactory
{
    public static MaskLayer create(int width, int height, JSONObject config)
    {
        String type = config.getString("type");

        try
        {
            Class<?> clazz = Class.forName("com.constantbeta.frame.layer." + type);

            try
            {
                Constructor<?> constructor = clazz.getConstructor(Integer.TYPE, Integer.TYPE, JSONObject.class);
                return (MaskLayer)constructor.newInstance(width, height, config.getJSONObject("config"));
            }
            catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e)
            {
                throw new Error(e);
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException("unexpected type: " + type);
        }
    }
}
