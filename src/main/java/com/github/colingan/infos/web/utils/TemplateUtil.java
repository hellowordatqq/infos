package com.github.colingan.infos.web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateUtil {
  public static String getOutputFromStr(String raw, Map<String, Object> params) throws IOException,
      TemplateException {
    Configuration cfg = new Configuration();
    cfg.setTemplateLoader(new StringTemplateLoader(raw));
    cfg.setDefaultEncoding("UTF-8");

    Template template = cfg.getTemplate("");

    StringWriter writer = new StringWriter();
    template.process(params, writer);
    return writer.toString();
  }

  public static String getOutputFromTemplateFile(String fileName, Map<String, Object> params)
      throws IOException, TemplateException {
    Configuration cfg = new Configuration();
    InputStream is = TemplateUtil.class.getClassLoader().getResourceAsStream(fileName);
    cfg.setTemplateLoader(new StringTemplateLoader(IOUtils.toString(is, "UTF-8")));
    cfg.setDefaultEncoding("UTF-8");

    Template template = cfg.getTemplate("");

    StringWriter writer = new StringWriter();
    template.process(params, writer);
    String result = writer.toString();

    return result;
  }
}
