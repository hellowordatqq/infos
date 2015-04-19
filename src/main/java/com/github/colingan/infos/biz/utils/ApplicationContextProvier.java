package com.github.colingan.infos.biz.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvier implements ApplicationContextAware {

  private static ApplicationContext context;

  public static ApplicationContext getApplicationContext() {
    System.out.println("context = " + context);
    return context;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

}
