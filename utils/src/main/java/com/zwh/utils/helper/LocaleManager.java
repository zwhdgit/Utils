package com.zwh.utils.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

/**
 * 注：1、activity及时生效需配置
 * 2、涉及context不要使用application
 * 多语言工具类
 * https://github.com/Freydoonk/LanguageTest
 */
public class LocaleManager {
    public static final String LANGUAGE_CHINA = "zh";
    public static final String LANGUAGE_ENGLISH = "en";
    // 找不到对照表，用斯洛伐克
    public static final String LANGUAGE_SHONA = "sk";
    // 找不到对照表，用德国码
    public static final String LANGUAGE_NDEBELE = "de";
    /**
     * 保存SharedPreferences的文件名
     */
    private static final String LOCALE_FILE = "LOCALE_FILE";
    /**
     * 保存Locale的key
     */
    private static final String LOCALE_KEY = "LOCALE_KEY";

    private static LocaleManager sLocaleManager;

    private LocaleManager() {

    }

    public static LocaleManager getInstance() {
        if (sLocaleManager == null) {
            synchronized (LocaleManager.class) {
                if (sLocaleManager == null) {
                    sLocaleManager = new LocaleManager();
                }
            }
        }
        return sLocaleManager;
    }

    public Context setLocale(Context context) {
        return updateResources(context, getLanguage(context));
    }

    public Context setNewLocale(Context context, String language) {
        persistLanguage(context, language);
        return updateResources(context, language);
    }

    public String getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOCALE_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(LOCALE_KEY, LocaleManager.LANGUAGE_ENGLISH);
    }

    /**
     * 持久化语言
     *
     * @param context  上下文
     * @param language 语言
     */
    private void persistLanguage(Context context, String language) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOCALE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(LOCALE_KEY, language);
        edit.apply();
    }

    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    public Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? config.getLocales().get(0) : config.locale;
    }
}