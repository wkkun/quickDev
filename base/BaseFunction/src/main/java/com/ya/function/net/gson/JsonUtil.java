package com.ya.function.net.gson;


import androidx.annotation.Nullable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lll on 2019/7/4
 * Email : lllemail@foxmail.com
 * Describe : json 工具类
 */
public class JsonUtil {

    private static final String TAG = "JsonUtil";

    public static Gson getGson() {
        return GsonHolder.gson;
    }

    private static Gson normalGson = new Gson();

    private static class GsonHolder {
        private static Gson gson = initGson();
    }

    private static Gson initGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(TypeSafeAdapterFactory.newInstance());
        try {
            Gson gson = gsonBuilder.create();

            Class gsonClass = gson.getClass();

            Field gF = gsonClass.getDeclaredField("factories");
            gF.setAccessible(true);
            List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) gF.get(gson);
            List<TypeAdapterFactory> newFactories = new ArrayList<>(factories.size());
            for (TypeAdapterFactory factory : factories) {
                if (factory instanceof JsonAdapterAnnotationTypeAdapterFactory) {
                    continue;
                }
                if (factory instanceof ReflectiveTypeAdapterFactory) {
                    continue;
                }
                newFactories.add(factory);
            }

            Field cF = gsonClass.getDeclaredField("constructorConstructor");
            cF.setAccessible(true);
            ConstructorConstructor cons = (ConstructorConstructor) cF.get(gson);

            JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory =
                    new JsonAdapterAnnotationTypeAdapterFactory(cons);
            newFactories.add(jsonAdapterAnnotationTypeAdapterFactory);
            newFactories.add(new ReflectiveTypeAdapterFactory(cons, FieldNamingPolicy.IDENTITY, Excluder.DEFAULT, jsonAdapterAnnotationTypeAdapterFactory));
            List<TypeAdapterFactory> unModifiable = Collections.unmodifiableList(newFactories);
            gF.set(gson, unModifiable);
            return gson;
        } catch (Exception e) {
//            IKLog.e("gson " + e.getMessage());
        }

        return gsonBuilder.create();
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonIOException, JsonSyntaxException {
        return getGson().fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return getGson().fromJson(json, type);
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return getGson().fromJson(reader, typeOfT);
    }

    public static <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return getGson().fromJson(json, classOfT);
    }

    public static <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return getGson().fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return normalGson.toJson(src);
    }

    public static String toJson(Object src, Type typeOfSrc) {
        return normalGson.toJson(src, typeOfSrc);
    }

    @Nullable
    public static String toJsonSafe(Object src) {
        try {
            return toJson(src);
        } catch (Exception e) {
//            IKLog.e(Log.getStackTraceString(e));
//            CrashReporter.report(e);
        }
        return null;
    }

    @Nullable
    public static <T> T fromJsonSafe(String json, Class<T> classOfT) {
        try {
            return fromJson(json, (Type) classOfT);
        } catch (Exception e) {
//            IKLog.e(TAG + " " + Log.getStackTraceString(e), json, classOfT);
//            Map<String,String> map = new HashMap<>();
//            map.put("json",json);
//            map.put("type",classOfT.toString());
//            CrashReporter.report(e,map);
        }
        return null;
    }

    @Nullable
    public static <T> T fromJsonSafe(String json, Type typeOfT) {
        try {
            return fromJson(json, typeOfT);
        } catch (Exception e) {
//            IKLog.e(TAG + " " + Log.getStackTraceString(e), json, typeOfT);
//            Map<String,String> map = new HashMap<>();
//            map.put("json",json);
//            map.put("type",typeOfT.toString());
//            CrashReporter.report(e,map);
        }
        return null;
    }
}
