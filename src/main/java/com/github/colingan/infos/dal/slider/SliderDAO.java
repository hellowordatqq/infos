package com.github.colingan.infos.dal.slider;

import com.github.colingan.infos.dal.common.IGenericDAO;
import com.github.colingan.infos.dal.slider.bo.Slider;

public interface SliderDAO extends IGenericDAO<Slider> {

  long addSlider(Slider slider);

  int update(long id, Slider slider);

}
