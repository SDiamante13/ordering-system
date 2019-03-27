package com.diamante.orderingsystem.utils;

public class QueryParamUtils {

    public static boolean isSingleParam(String parameterValue, String regex) {
        return parameterValue.split(regex).length == 1;
    }

    public static String splitParamByRegexAndCombine(String queryParamWithDelimiter, String regex) {
        String[] paramStringArray = queryParamWithDelimiter.split(regex);
        StringBuilder paramWithSpaces = new StringBuilder();

        for(String singleWord : paramStringArray) {
            paramWithSpaces.append(singleWord).append(" ");
        }
        return paramWithSpaces.toString().trim();
    }
}
