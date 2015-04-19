package com.github.colingan.infos.dal.blogs.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Blog implements Serializable {

	private static final long serialVersionUID = 2064717122400298350L;

	private long id;
	private String userName;
	private long category1;
	private long category2;
	private String title;
	private String content;
	private Date addTime;
	private Date updateTime;

	// not in db
	private boolean fresh = false;
	private String formatedAddTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getCategory1() {
		return category1;
	}

	public void setCategory1(long category1) {
		this.category1 = category1;
	}

	public long getCategory2() {
		return category2;
	}

	public void setCategory2(long category2) {
		this.category2 = category2;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isFresh() {

		return fresh;
	}

	public void setFresh(boolean fresh) {

		this.fresh = fresh;
	}

	public String getFormatedAddTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		formatedAddTime = format.format(addTime);
		return formatedAddTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
