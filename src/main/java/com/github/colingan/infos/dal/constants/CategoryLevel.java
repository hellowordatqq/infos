package com.github.colingan.infos.dal.constants;

import java.util.HashMap;
import java.util.Map;

public enum CategoryLevel {

  LEVEL1(1), // 1级分类
  LEVEL2(2) // 2级分类
  ;

  static final Map<Integer, CategoryLevel> VALUE_MAP = new HashMap<Integer, CategoryLevel>() {
    {
      for (CategoryLevel level : CategoryLevel.values()) {
        put(level.value, level);
      }
    }
  };

  private CategoryLevel(int value) {
    this.value = value;
  }

  private int value;

  public int getValue() {
    return value;
  }

  public static boolean validateCategoryLevel(int value) {
    return VALUE_MAP.containsKey(Integer.valueOf(value));
  }
}
