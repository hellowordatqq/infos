package com.github.colingan.infos.dal.heroes.bo;

import java.io.Serializable;
import java.util.Date;

public class Hero implements Serializable {

  private static final long serialVersionUID = -1114709451298738467L;

  private long id;
  private String name;
  private String profile;
  private String reason;
  private Date addTime;
  private Date isDel;
}
