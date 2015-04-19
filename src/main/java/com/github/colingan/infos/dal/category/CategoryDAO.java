package com.github.colingan.infos.dal.category;

import com.github.colingan.infos.dal.category.bo.Category;
import com.github.colingan.infos.dal.common.IGenericDAO;

/**
 * DAO interface for ones123 category
 * 
 * @title CategoryDAO
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2015年1月19日
 * @version 1.0
 */
public interface CategoryDAO extends IGenericDAO<Category> {

  long addCategory(Category category);

  int deleteCascade(long id);

  int updateCategory(Category category);
}
