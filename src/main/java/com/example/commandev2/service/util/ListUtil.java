package com.example.commandev2.service.util;

import java.util.List;

public class ListUtil {

    public static boolean isEmpty(List list) {
        return (list == null || list.isEmpty());
    }

    public static boolean isNotEmpty(List list) {
        return !(isEmpty(list));
    }
}
