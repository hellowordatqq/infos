package com.github.colingan.infos.biz.services.slider.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.colingan.infos.biz.services.slider.SliderService;
import com.github.colingan.infos.dal.common.CommonOrderBy;
import com.github.colingan.infos.dal.common.CommonOrderBy.OrderByDirection;
import com.github.colingan.infos.dal.common.ComparisonCondition;
import com.github.colingan.infos.dal.common.ComparisonCondition.Operation;
import com.github.colingan.infos.dal.common.Condition;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.common.MultiOrderBy;
import com.github.colingan.infos.dal.slider.SliderDAO;
import com.github.colingan.infos.dal.slider.bo.Slider;

@Service
public class SliderServiceImpl implements SliderService {

  @Resource
  private SliderDAO sliderDao;

  @Value("#[slider.image.dir]")
  protected volatile String sliderImageDir;

  @Override
  public boolean updateSlider(long id, int idx, String originName, String destName) {
    Slider slider = new Slider();
    slider.setId(idx);
    slider.setIdx(idx);
    slider.setOriginName(originName);
    slider.setDestName(destName);
    slider.setUpdateTime(new Date());
    return this.sliderDao.update(id, slider) == 1;
  }

  @Override
  public Slider addSlider(int idx, String originName, String destName) {
    Validate.notEmpty(originName);
    Validate.notEmpty(destName);

    Slider slider = new Slider();
    slider.setAddTime(new Date());
    slider.setDestName(destName);
    slider.setIdx(idx);
    slider.setIsDel(0);
    slider.setOriginName(originName);
    slider.setUpdateTime(new Date());

    slider.setId(this.sliderDao.addSlider(slider));
    return slider;
  }

  @Override
  public String getImageFileDirectory() {
    return sliderImageDir;
  }

  @Override
  public List<Slider> queryAllValidateSliders() {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.IDX);
    fields.add(Field.ORIGIN_NAME);
    fields.add(Field.DEST_NAME);

    Condition condition = new ComparisonCondition(Field.IS_DEL, Operation.EQ, 0);
    MultiOrderBy orderBy = new MultiOrderBy();
    orderBy.addCommonOrderBy(new CommonOrderBy(Field.IDX, OrderByDirection.ASC));
    orderBy.addCommonOrderBy(new CommonOrderBy(Field.UPDATE_TIME, OrderByDirection.DESC));
    return this.sliderDao.getObjects(null, fields, condition, orderBy);
  }

  @Override
  public boolean deleteSlider(long id) {

    return this.sliderDao.delete(null, new ComparisonCondition(Field.ID, Operation.EQ, id)) == 1;
  }
}
