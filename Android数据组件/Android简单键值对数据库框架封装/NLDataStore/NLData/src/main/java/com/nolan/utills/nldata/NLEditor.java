package com.nolan.utills.nldata;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import java.util.Set;

/**
 * 作者 zf
 * 日期 2024/1/2
 */
public class NLEditor implements SharedPreferences.Editor {
    @Override
    public SharedPreferences.Editor putString(String s, @Nullable String s1) {
        return null;
    }

    @Override
    public SharedPreferences.Editor putStringSet(String s, @Nullable Set<String> set) {
        return null;
    }

    @Override
    public SharedPreferences.Editor putInt(String s, int i) {
        return null;
    }

    @Override
    public SharedPreferences.Editor putLong(String s, long l) {
        return null;
    }

    @Override
    public SharedPreferences.Editor putFloat(String s, float v) {
        return null;
    }

    @Override
    public SharedPreferences.Editor putBoolean(String s, boolean b) {
        return null;
    }

    @Override
    public SharedPreferences.Editor remove(String s) {
        return null;
    }

    @Override
    public SharedPreferences.Editor clear() {
        return null;
    }

    @Override
    public boolean commit() {
        return false;
    }

    @Override
    public void apply() {

    }
}
