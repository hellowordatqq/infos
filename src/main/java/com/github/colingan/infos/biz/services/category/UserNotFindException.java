package com.github.colingan.infos.biz.services.category;

public class UserNotFindException extends RuntimeException {

  private String userName;

  public UserNotFindException(String userName, Throwable t) {
    super(t);
    this.userName = userName;
  }

  public String getUserName() {
    return userName;
  }
}
