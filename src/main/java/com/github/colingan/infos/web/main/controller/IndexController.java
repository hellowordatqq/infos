/**
 * Baidu.com Inc. Copyright (c) 2000-2014 All Rights Reserved.
 */
package com.github.colingan.infos.web.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.colingan.infos.biz.services.blog.BlogService;
import com.github.colingan.infos.biz.services.link.LinkService;
import com.github.colingan.infos.biz.services.slider.SliderService;
import com.github.colingan.infos.common.utils.DateTimeUtil;
import com.github.colingan.infos.dal.blogs.bo.Blog;
import com.github.colingan.infos.dal.category.bo.Category;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.slider.bo.Slider;
import com.github.colingan.infos.web.controller.BaseController;
import com.github.colingan.infos.web.main.model.MainModel;
import com.github.colingan.infos.web.main.model.SearchModel;

/**
 * 首页控制器
 * 
 * @title IndexController
 * @author Gan Jia (ganjia@baidu.com)
 * @date 2014年11月11日
 * @version 1.0
 */
@Controller
public class IndexController extends BaseController {

	private static final String INDEX_PAGE = "home/home2";
	private static final String SEARCH_PAGE = "home/search";

	protected volatile String logout;
	@Value("#[latest.count]")
	protected volatile String latestCount;
	@Value("#[news.delay]")
	protected volatile int newsDelay;

	@Resource
	private SliderService sliderService;

	@Resource
	private BlogService blogService;

	@Resource
	private LinkService linkService;

	@RequestMapping(value = "/error")
	public String error() {
		return "error";
	}

	@RequestMapping(value = "/search")
	public String search(HttpServletRequest request,
			HttpServletResponse response) {
		SearchModel model = new SearchModel();
		// basic数据模型
		model.setBasic(super.prepareBaseModel(request));
		request.setAttribute(MODEL_NAME, model);

		// blogs
		Long pageSize = super.DEFAULT_PAGE_SIZE;
		String pageSizeStr = request.getParameter(super.PARAM_PAGE_SIZE);
		if (StringUtils.isNotEmpty(pageSizeStr)) {
			try {
				pageSize = Long.parseLong(pageSizeStr);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("page size参数不正确");
			}
		}
		Long pageNum = super.DEFAULT_PAGE;
		String pageNumberStr = request.getParameter(super.PARAM_PAGE_NUMBER);
		if (StringUtils.isNotEmpty(pageNumberStr)) {
			try {
				pageNum = Long.parseLong(pageNumberStr);
			} catch (NumberFormatException nfe) {
				throw new IllegalArgumentException("page number参数不正确");
			}
		}
		long totalPage = 0;
		List<Map<String, Object>> blogs = new ArrayList<Map<String, Object>>();
		String s = request.getParameter("s");
		if (StringUtils.isEmpty(s)) {
			// ensure s is not null
			s = "";
			// 没有关键词，不检索
			totalPage = 0;
		} else {
			// do query
			List<Blog> blogList = this.blogService.searchByTitle(s, pageNum,
					pageSize);
			if (CollectionUtils.isNotEmpty(blogList)) {
				for (Blog blog : blogList) {
					Map<String, Object> innerMap = new HashMap<String, Object>();
					innerMap.put(Field.ID.getKeyName(), blog.getId());
					innerMap.put(Field.TITLE.getKeyName(), blog.getTitle());
					innerMap.put(Field.AUTHOR.getKeyName(), blog.getUserName());
					innerMap.put(Field.ADD_TIME.getKeyName(),
							DateTimeUtil.dateToSecond(blog.getAddTime()));

					blogs.add(innerMap);
				}
			}
			totalPage = this.blogService.getSearchByTitleSize(s);
		}

		model.setBlogs(blogs);
		model.setPageNum(pageNum);
		model.setS(s);
		model.setTotalPage(totalPage % pageSize == 0 ? totalPage / pageSize
				: totalPage / pageSize + 1);

		request.setAttribute(MODEL_NAME, model);

		return SEARCH_PAGE;
	}

	@RequestMapping(value = "/")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		MainModel model = new MainModel();
		// basic数据模型
		model.setBasic(super.prepareBaseModel(request));
		request.setAttribute(MODEL_NAME, model);
		// slider
		List<Slider> sliders = this.sliderService.queryAllValidateSliders();
		if (CollectionUtils.isNotEmpty(sliders)) {
			String[] banner = new String[sliders.size()];
			int idx = 0;
			for (Slider slider : sliders) {
				banner[idx++] = slider.getDestName();
			}
			model.setBanner(banner);
		}
		// new blogs
		Map<Category, List<Entry<Category, List<Blog>>>> tmpBlogs = new LinkedHashMap<Category, List<Entry<Category, List<Blog>>>>();
		Map<Category, Map<Category, List<Blog>>> newBlogs = new LinkedHashMap<Category, Map<Category, List<Blog>>>();
		Map<Category, List<Category>> categoryMap = this.categoryService
				.queryAllValidCategoryBriefs();
		if (categoryMap != null && categoryMap.size() > 0) {
			for (Entry<Category, List<Category>> entry : categoryMap.entrySet()) {
				newBlogs.put(entry.getKey(),
						new LinkedHashMap<Category, List<Blog>>());
				if (CollectionUtils.isNotEmpty(entry.getValue())) {
					for (Category category : entry.getValue()) {
						newBlogs.get(entry.getKey()).put(category,
								new ArrayList<Blog>());
					}
				}
			}
		}
		List<Blog> blogs = this.blogService.getLatestBlogs(Integer
				.valueOf(latestCount));
		Date now = new Date();
		if (CollectionUtils.isNotEmpty(blogs)) {
			for (Blog blog : blogs) {
				Category category1 = new Category(blog.getCategory1());
				Category category2 = new Category(blog.getCategory2());
				List<Blog> blogList = null;
				if (newBlogs.containsKey(category1)) {
					blogList = newBlogs.get(category1).get(category2);
				}
				if (blogList != null) {
					if (DateTimeUtil.daysBetween(now, blog.getAddTime()) <= newsDelay) {
						blog.setFresh(true);
					}
					blogList.add(blog);
				} else {
					LOGGER.warn("dirty blog data find." + blog);
				}
			}
		}
		for (Entry<Category, Map<Category, List<Blog>>> entry : newBlogs
				.entrySet()) {
			tmpBlogs.put(entry.getKey(),
					new ArrayList<Entry<Category, List<Blog>>>(entry.getValue()
							.entrySet()));
		}
		model.setNewBlogs(new ArrayList<Entry<Category, List<Entry<Category, List<Blog>>>>>(
				tmpBlogs.entrySet()));

		// links
		model.setLinks(this.linkService.queryAllLinks());

		request.setAttribute(MODEL_NAME, model);
		return INDEX_PAGE;
	}

	@RequestMapping(value = "/raw")
	@ResponseBody
	public MainModel indexRaw(HttpServletRequest request,
			HttpServletResponse response) {
		this.index(request, response);
		return (MainModel) request.getAttribute(MODEL_NAME);
	}

	/**
	 * 设置登出地址
	 * 
	 * @param logout
	 *            登出url
	 */
	@Value("#[logout.url]")
	public void setLogout(String logout) {
		this.logout = logout;
	}

	@Override
	protected String getLogout() {
		return this.logout;
	}

}
