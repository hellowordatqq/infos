package com.github.colingan.infos.biz.services.category;

import java.util.List;
import java.util.Map;

import com.github.colingan.infos.dal.category.bo.Category;

/**
 * service interface for ones123 category.
 * 
 * @title CategoryService
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2015年1月19日
 * @version 1.0
 */
public interface CategoryService {

  /**
   * <p>
   * 获取有效的一级分类的简介，简介仅包含id和字面 结果按id排序
   * </p>
   * 
   * @return 分类列表
   */
  List<Category> getValidFirstLevelBriefs();

  /**
   * <p>
   * 获取有效的一级分类详情
   * </p>
   * 
   * @return
   */
  List<Category> getValidFirstLevelContent();

  /**
   * <p>
   * 获取指定一级分类id的二级分类详情
   * </p>
   * 
   * @param parentCategory
   * @return
   */
  List<Category> getValidSecondChildLevelContent(long parentCategory);

  Category addCategory(String userName, String name, int level, long parentCategory);

  Category getValidateCategoryByIds(long level1, long level2);

  boolean deleteCategoryCascade(long id);

  List<Category> queryCategorys(Long... ids);

  boolean updateCategory(String userName, long id, String name);

  boolean iconFileExist(String fileName);

  String getIconFileDirectory();

  Map<Category, List<Category>> queryAllValidCategoryBriefs();
}
