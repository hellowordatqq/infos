package com.github.colingan.infos.biz.services.slider;

import java.util.List;

import com.github.colingan.infos.dal.slider.bo.Slider;

public interface SliderService {

  List<Slider> queryAllValidateSliders();

  boolean deleteSlider(long id);

  String getImageFileDirectory();

  Slider addSlider(int idx, String originName, String destName);

  boolean updateSlider(long id, int idx, String originName, String destName);

}
