package com.github.colingan.infos.biz.services.category.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import com.github.colingan.infos.biz.services.AbstractBaseService;
import com.github.colingan.infos.biz.services.category.CategoryService;
import com.github.colingan.infos.common.utils.Constants;
import com.github.colingan.infos.dal.category.CategoryDAO;
import com.github.colingan.infos.dal.category.bo.Category;
import com.github.colingan.infos.dal.common.CommonOrderBy;
import com.github.colingan.infos.dal.common.CommonOrderBy.OrderByDirection;
import com.github.colingan.infos.dal.common.ComparisonCondition;
import com.github.colingan.infos.dal.common.ComparisonCondition.Operation;
import com.github.colingan.infos.dal.common.Condition;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.common.LogicGroupCondition;
import com.github.colingan.infos.dal.common.LogicGroupCondition.LogicOperation;
import com.github.colingan.infos.dal.common.MultiOrderBy;
import com.github.colingan.infos.dal.common.OrderBy;
import com.github.colingan.infos.dal.constants.CategoryLevel;

/**
 * service implements for ones123 category.
 * 
 * @title CategoryServiceImpl
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2015年1月19日
 * @version 1.0
 */
@Service
public class CategoryServiceImpl extends AbstractBaseService implements CategoryService {

  @Resource
  private CategoryDAO categoryDao;

  protected volatile String categoryIconDir;

  @Override
  public Map<Category, List<Category>> queryAllValidCategoryBriefs() {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.CATEGORY_NAME);
    return this.innerQueryCategory(null, null, Boolean.TRUE, fields);
  }

  @Override
  public boolean iconFileExist(String fileName) {
    Validate.notEmpty(fileName);
    File path = new File(categoryIconDir);
    if (!path.exists()) {
      path.mkdirs();
    }
    File file = new File(path, fileName);
    return file.exists();
  }

  @Override
  public boolean updateCategory(String userName, long id, String name) {
    Validate.notEmpty(userName);
    Validate.notEmpty(name);

    Category category = new Category();
    category.setId(id);
    category.setUpdateuser(userName);
    category.setName(name);

    return this.categoryDao.updateCategory(category) == 1;
  }

  @Override
  public List<Category> queryCategorys(Long... ids) {
    if (ids == null || ids.length == 0) {
      throw new IllegalArgumentException("category ids should not be empty.");
    }
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.CATEGORY_NAME);
    fields.add(Field.LEVEL);

    Condition condition = new ComparisonCondition(Field.ID, Operation.IN, Arrays.asList(ids));
    MultiOrderBy orderBy = new MultiOrderBy();
    orderBy.addCommonOrderBy(new CommonOrderBy(Field.LEVEL, OrderByDirection.ASC));
    orderBy.addCommonOrderBy(new CommonOrderBy(Field.ID, OrderByDirection.ASC));

    return this.categoryDao.getObjects(null, fields, condition, orderBy);
  }

  @Override
  public boolean deleteCategoryCascade(long id) {

    return this.categoryDao.deleteCascade(id) > 0;
  }

  @Override
  public Category getValidateCategoryByIds(long level1, long level2) {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.CATEGORY_NAME);
    Condition condition =
        new LogicGroupCondition(new ComparisonCondition(Field.ID, Operation.EQ, level2),
            LogicOperation.AND,
            new ComparisonCondition(Field.PARENT_CATEGORY, Operation.EQ, level1));
    condition =
        new LogicGroupCondition(condition, LogicOperation.AND, new ComparisonCondition(
            Field.IS_DEL, Operation.EQ, 0));

    List<Category> datas = this.categoryDao.getObjects(null, fields, condition);
    if (CollectionUtils.isEmpty(datas)) {
      return null;
    }
    return datas.get(0);
  }

  @Override
  public List<Category> getValidFirstLevelBriefs() {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.CATEGORY_NAME);
    fields.add(Field.ID);
    Map<Category, List<Category>> datas =
        this.innerQueryCategory(CategoryLevel.LEVEL1, null, Boolean.TRUE, fields);
    return new ArrayList<Category>(datas.keySet());
  }

  @Override
  public List<Category> getValidFirstLevelContent() {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.CATEGORY_NAME);
    fields.add(Field.LEVEL);
    Map<Category, List<Category>> datas =
        this.innerQueryCategory(CategoryLevel.LEVEL1, null, Boolean.TRUE, fields);
    return new ArrayList<Category>(datas.keySet());
  }

  @Override
  public List<Category> getValidSecondChildLevelContent(long parentCategory) {
    Validate.isTrue(parentCategory > 0, "parent category id should greater than 0");
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.CATEGORY_NAME);
    fields.add(Field.LEVEL);
    Map<Category, List<Category>> datas =
        this.innerQueryCategory(CategoryLevel.LEVEL2, parentCategory, Boolean.TRUE, fields);
    List<Category> rel = new ArrayList<Category>(datas.keySet());
    // if (CollectionUtils.isNotEmpty(rel)) {
    // for (Category category : rel) {
    // category.setIcon(getStaticResourceUri(category.getIcon()));
    // }
    // }
    return rel;
  }

  @Override
  public Category addCategory(String userName, String name, int level,
      long parentCategory) {
    Validate.notEmpty(userName);
    Validate.notEmpty(name);
    Category category = new Category();
    category.setAddTime(new Date());
    category.setIsDel(0);
    category.setLevel(level);
    category.setName(name);
    category.setParentCategory(parentCategory);
    category.setUpdateTime(new Date());
    category.setUpdateuser(userName);
    Validate.isTrue(category.validate(), "category is not complete.");

    category.setId(this.categoryDao.addCategory(category));
    return category;
  }

  private Map<Category, List<Category>> innerQueryCategory(CategoryLevel level, Long parentId,
      Boolean validate, List<Field> fields) {
    Condition condition = null;
    Condition firstCondition = null;
    if (level != null) {
      firstCondition = new ComparisonCondition(Field.LEVEL, Operation.EQ, level.getValue());
    }
    condition = compoundCondition(condition, firstCondition, LogicOperation.AND);
    if (parentId != null) {
      firstCondition =
          new ComparisonCondition(Field.PARENT_CATEGORY, Operation.EQ, parentId.longValue());
    }
    condition = compoundCondition(condition, firstCondition, LogicOperation.AND);
    if (validate != null) {
      firstCondition = new ComparisonCondition(Field.IS_DEL, Operation.EQ, validate ? 0 : 1);
    }
    condition = compoundCondition(condition, firstCondition, LogicOperation.AND);
    // 默认必查字段
    Set<Field> fieldSet = new HashSet<Field>();
    fieldSet.add(Field.ID);
    fieldSet.add(Field.PARENT_CATEGORY);
    if (CollectionUtils.isNotEmpty(fields)) {
      fieldSet.addAll(fields);
    }

    OrderBy orderBy = new MultiOrderBy();
    ((MultiOrderBy) orderBy).addCommonOrderBy(new CommonOrderBy(Field.LEVEL, OrderByDirection.ASC));
    ((MultiOrderBy) orderBy).addCommonOrderBy(new CommonOrderBy(Field.ID, OrderByDirection.ASC));

    Map<Category, List<Category>> rel = new LinkedHashMap<Category, List<Category>>();
    List<Category> datas =
        categoryDao.getObjects(null, new ArrayList<Field>(fieldSet), condition, orderBy);
    if (CollectionUtils.isNotEmpty(datas)) {
      Category parent = null;
      for (Category data : datas) {
        parent = new Category();
        parent.setId(data.getParentCategory());
        List<Category> subList = rel.get(parent);
        if (subList != null) {
          subList.add(data);
        } else {
          rel.put(data, new ArrayList<Category>());
        }
      }
    }

    return rel;
  }

  public void setCategoryDao(CategoryDAO categoryDao) {
    this.categoryDao = categoryDao;
  }

  @Override
  public String getIconFileDirectory() {
    return this.categoryIconDir;
  }

}
