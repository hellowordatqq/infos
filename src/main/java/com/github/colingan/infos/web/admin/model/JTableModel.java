package com.github.colingan.infos.web.admin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class JTableModel implements Serializable {

  private static com.google.gson.Gson GSON = new Gson();

  public static final String RESULT_OK = "OK";
  public static final String RESULT_ERROR = "ERROR";
  public static final String DATA_KEY_RECORD = "Record";
  public static final String DATA_KEY_RECORDS = "Records";
  private static final String KEY_STATUS = "Result";
  private static final String KEY_MESSAGE = "Message";
  private static final String REDIRECT_PAGE = "Redirect";
  private static final String KEY_JSONRPC = "jsonrpc";
  public static final String RPC_2_0 = "2.0";
  private static final String JSONRPC_ERROR_KEY = "error";
  private static final String JSONRPC_RESULT_KEY = "result";
  private static final String JSONRPC_ID = "id";

  private Map<String, Object> datas = new HashMap<String, Object>();

  public JTableModel jsonRpcId(String id) {
    datas.put(JSONRPC_ID, id);
    return this;
  }

  public JTableModel jsonRpcResult(String result) {
    datas.put(JSONRPC_RESULT_KEY, result);
    return this;
  }

  public JTableModel jsonRpcVersion(String version) {
    datas.put(KEY_JSONRPC, version);
    return this;
  }

  public JTableModel jsonRpcFail(ErrorCode code) {
    datas.put(JSONRPC_ERROR_KEY, code);
    return this;
  }

  public JTableModel success() {
    datas.put(KEY_STATUS, RESULT_OK);
    return this;
  }

  public JTableModel fail(String message) {
    datas.put(KEY_STATUS, RESULT_ERROR);
    datas.put(KEY_MESSAGE, message);
    return this;
  }

  public JTableModel setRecord(Object bo) {
    datas.put(DATA_KEY_RECORD, bo);
    return this;
  }

  public JTableModel addRecord(Object bo) {
    Object list = datas.get(DATA_KEY_RECORDS);
    if (list == null) {
      list = new ArrayList();
      datas.put(DATA_KEY_RECORDS, list);
    }
    ((List) list).add(bo);
    return this;
  }

  public JTableModel addAllRecord(List bos) {
    Object list = datas.get(DATA_KEY_RECORDS);
    if (list == null) {
      list = new ArrayList();
      datas.put(DATA_KEY_RECORDS, list);
    }
    ((List) list).addAll(bos);
    return this;
  }

  public JTableModel setRedirectPage(String page) {
    datas.put(REDIRECT_PAGE, page);
    return this;
  }

  public Map<String, Object> getDatas() {
    return Collections.unmodifiableMap(datas);
  }

  public static final class ErrorCode implements Serializable {
    int code;
    String message;

    public ErrorCode(int code, String message) {
      super();
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

  }
}
