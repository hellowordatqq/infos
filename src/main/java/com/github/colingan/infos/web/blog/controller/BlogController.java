package com.github.colingan.infos.web.blog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.colingan.infos.biz.BizConstants;
import com.github.colingan.infos.biz.services.blog.BlogService;
import com.github.colingan.infos.biz.services.category.CategoryService;
import com.github.colingan.infos.common.utils.DateTimeUtil;
import com.github.colingan.infos.dal.blogs.bo.Blog;
import com.github.colingan.infos.dal.category.bo.Category;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.constants.RoleGroup;
import com.github.colingan.infos.dal.members.bo.Member;
import com.github.colingan.infos.web.admin.model.CategoryConsoleModel;
import com.github.colingan.infos.web.admin.model.JTableModel;
import com.github.colingan.infos.web.blog.model.BlogListModel;
import com.github.colingan.infos.web.blog.model.BlogModel;
import com.github.colingan.infos.web.controller.BaseController;
import com.github.colingan.infos.web.exceptions.PermissionDeniedException;

@Controller
public class BlogController extends BaseController {

  private static final String BLOG_CREATE_PAGE = "blogs/create";
  private static final String BLOG_DETAIL_PAGE = "blogs/view";
  private static final String BLOG_EDIT_PAGE = "blogs/edit";
  private static final String BLOG_NOTCHILD_LIST = "blogs/empty";
  private static final String BLOG_LIST_PAGE = "blogs/list";

  @Value("#[logout.url]")
  protected volatile String logout;

  @Resource
  private CategoryService categoryService;

  @Resource
  private BlogService blogService;

  @RequestMapping(value = "/blogs/root")
  public String blogsRootEntry(HttpServletRequest request, HttpServletResponse response) {
    Long id = null;
    try {
      id = Long.parseLong(request.getParameter(Field.ID.getKeyName()));
      Validate.notNull(id);
    } catch (Exception e) {
      throw new IllegalArgumentException("一级分类id缺失 or 不正确.");
    }
    List<Category> childCategorys = this.categoryService.getValidSecondChildLevelContent(id);
    if (CollectionUtils.isEmpty(childCategorys)) {
      BlogListModel model = new BlogListModel();
      model.setBasic(super.prepareBaseModel(request));
      model.setRootCategory(id);
      request.setAttribute(MODEL_NAME, model);
      return BLOG_NOTCHILD_LIST;
    }

    Category firstChild = childCategorys.get(0);
    return "redirect:/blogList?rootCategory=" + id + "&childCategory=" + firstChild.getId();
  }

  @RequestMapping(value = "/blogList")
  public String listBlogs(HttpServletRequest request, HttpServletResponse response) {
    Long rootCategory = null;
    try {
      rootCategory = Long.parseLong(request.getParameter(Field.ROOT_CATEGORY.getKeyName()));
      Validate.notNull(rootCategory);
    } catch (Exception e) {
      throw new IllegalArgumentException("root category缺失 or 不正确！");
    }
    Long childCategory = null;
    try {
      childCategory = Long.parseLong(request.getParameter(Field.CHILD_CATEGORY.getKeyName()));
      Validate.notNull(childCategory);
    } catch (Exception e) {
      throw new IllegalArgumentException("child category缺失 or 不正确！");
    }
    List<Category> children = this.categoryService.getValidSecondChildLevelContent(rootCategory);
    if (CollectionUtils.isEmpty(children) || !children.contains(new Category(childCategory))) {
      throw new IllegalArgumentException("分类关系不正确！");
    }
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
    BlogListModel model = new BlogListModel();
    model.setRootCategory(rootCategory);
    model.setChildCategory(childCategory);
    model.setBasic(super.prepareBaseModel(request));
    model.setCategorys(children);
    // query blog brief for current child category
    List<Blog> blogList =
        this.blogService.queryBlogsBrief(rootCategory, childCategory, pageNum, pageSize);
    List<Map<String, Object>> blogs = new ArrayList<Map<String, Object>>();
    if (CollectionUtils.isNotEmpty(blogList)) {
      for (Blog blog : blogList) {
        Map<String, Object> innerMap = new HashMap<String, Object>();
        innerMap.put(Field.ID.getKeyName(), blog.getId());
        innerMap.put(Field.TITLE.getKeyName(), blog.getTitle());
        innerMap.put(Field.AUTHOR.getKeyName(), blog.getUserName());
        innerMap.put(Field.ADD_TIME.getKeyName(), DateTimeUtil.dateToSecond(blog.getAddTime()));

        blogs.add(innerMap);
      }
    }
    model.setBlogs(blogs);
    model.setPageNum(pageNum);
    long totalCount = this.blogService.getBlogSize(rootCategory, childCategory);
    model.setTotalPage(totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize
        + 1);
    request.setAttribute(MODEL_NAME, model);
    return BLOG_LIST_PAGE;
  }

  @RequestMapping(value = "/publish")
  public String newBlogEntry(HttpServletRequest request, HttpServletResponse response) {
    this.basePublishMemberCheck(request);
    // basic数据
    CategoryConsoleModel model = new CategoryConsoleModel();
    model.setBasic(super.prepareBaseModel(request));

    request.setAttribute(MODEL_NAME, model);
    return BLOG_CREATE_PAGE;
  }

  @RequestMapping(value = "/publish/edit")
  public String editBlogEntry(HttpServletRequest request, HttpServletResponse response) {
    BlogModel model = this.innerQueryBlogModel(request);
    this.blogOwnerCheck(model, request);
    request.setAttribute(MODEL_NAME, model);

    return BLOG_EDIT_PAGE;
  }

  @RequestMapping(value = "/publish/delete")
  public String deleteBlog(HttpServletRequest request, HttpServletResponse response) {
    Long id = null;
    try {
      id = Long.parseLong(request.getParameter(Field.ID.getKeyName()));
    } catch (NumberFormatException nfe) {
      throw new IllegalArgumentException("id为空！");
    }
    Blog blog = this.blogService.queryBlogBrief(id);
    if (blog == null) {
      throw new IllegalArgumentException("未知id！");
    }
    try {
      this.blogOwnerCheck(request, super.getUserName(request), blog.getUserName());
    } catch (Exception e) {
      throw new PermissionDeniedException("您没有该文章的编辑权限！");
    }
    if (this.blogService.deleteBlog(id)) {
      return "redirect:/blogList?rootCategory=" + blog.getCategory1() + "&childCategory="
          + blog.getCategory2();
    } else {
      throw new RuntimeException("操作失败！");
    }
  }

  @RequestMapping(value = "/publish/save")
  @ResponseBody
  public Map<String, Object> updateBlog(HttpServletRequest request, HttpServletResponse response) {
    JTableModel model = new JTableModel();
    Long blogId = null;
    try {
      blogId = Long.parseLong(request.getParameter(Field.ID.getKeyName()));
      Validate.notNull(blogId);
    } catch (Exception e) {
      model.fail("id不能为空！");
      return model.getDatas();
    }
    String title = request.getParameter(Field.TITLE.getKeyName());
    if (StringUtils.isEmpty(title)) {
      model.fail("操作失败，标题不能为空！");
      return model.getDatas();
    }
    String content = request.getParameter(Field.CONTENT.getKeyName());
    if (StringUtils.isEmpty(content)) {
      model.fail("操作失败，亲，多少写点内容吧！");
      return model.getDatas();
    }

    try {
      if (this.blogService.updateBlog(blogId, title, content)) {
        model.success().setRedirectPage("/blogs/view?id=" + blogId);
      } else {
        model.fail("文件存储失败！");
      }
    } catch (Exception e) {
      model.fail("系统异常！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/publish/submit")
  @ResponseBody
  public Map<String, Object> submitNewBlog(HttpServletRequest request, HttpServletResponse response) {
    JTableModel model = new JTableModel();
    String title = request.getParameter(Field.TITLE.getKeyName());
    if (StringUtils.isEmpty(title)) {
      model.fail("操作失败，标题不能为空！");
      return model.getDatas();
    }
    Long category1 = null;
    try {
      category1 = Long.parseLong(request.getParameter(Field.CATEGORY1.getKeyName()));
      Validate.notNull(category1);
    } catch (Exception e) {
      model.fail("操作失败，未知一级分类！");
      return model.getDatas();
    }
    Long category2 = null;
    try {
      category2 = Long.parseLong(request.getParameter(Field.CATEGORY2.getKeyName()));
      Validate.notNull(category2);
    } catch (Exception e) {
      model.fail("操作失败，未知二级分类！");
      return model.getDatas();
    }

    String content = request.getParameter(Field.CONTENT.getKeyName());
    if (StringUtils.isEmpty(content)) {
      model.fail("操作失败，亲，多少写点内容吧！");
      return model.getDatas();
    }

    if (!this.validateCategory(category1, category2)) {
      model.fail("操作失败，未知分类！");
      return model.getDatas();
    }

    try {
      String userName = super.getUserName(request);
      long blogId = this.blogService.addNewBlog(userName, title, category1, category2, content);
      if (blogId == 0) {
        model.fail("文件存储失败！");
      } else {
        model.success().setRedirectPage("/blogs/view?id=" + blogId);
      }
    } catch (Exception e) {
      model.fail("系统异常！");
    }
    return model.getDatas();
  }

  @RequestMapping(value = "/blogs/view")
  public String viewBlog(HttpServletRequest request, HttpServletResponse response) {

    request.setAttribute(MODEL_NAME, this.innerQueryBlogModel(request));

    return BLOG_DETAIL_PAGE;
  }

  @RequestMapping(value = "/blogs/viewraw")
  @ResponseBody
  public BlogModel viewBlogRaw(HttpServletRequest request, HttpServletResponse response) {
    this.viewBlog(request, response);
    return (BlogModel) request.getAttribute(MODEL_NAME);
  }

  @Override
  protected String getLogout() {
    return this.logout;
  }

  protected void basePublishMemberCheck(HttpServletRequest request) {
    Member member = (Member) request.getAttribute(BizConstants.MEMBER_ROLE_GROUP);
    Validate.notNull(member, "member is required.");
    Validate.isTrue(member.getRoleGroup() >= RoleGroup.READ_WRITE.getValue(),
        "member role should greater than administrator");
  }

  protected void blogOwnerCheck(BlogModel model, HttpServletRequest request) {
    this.blogOwnerCheck(request, model.getBasic().getUserName(), model.getAuthor());
  }

  private void blogOwnerCheck(HttpServletRequest request, String userName, String blogAuthor) {
    Validate.notEmpty(userName);
    Validate.notEmpty(blogAuthor);
    Validate.notNull(request);
    Member member = (Member) request.getAttribute(BizConstants.MEMBER_ROLE_GROUP);
    Validate.notNull(member, "member is required.");
    Validate.isTrue(member.getRoleGroup() >= RoleGroup.READ_WRITE.getValue(),
        "member role should greater than administrator");
    Validate.isTrue(blogAuthor.equals(userName)
        || member.getRoleGroup() >= RoleGroup.ADMIN.getValue());
  }

  private boolean validateCategory(long level1, long level2) {
    try {
      Category category = this.categoryService.getValidateCategoryByIds(level1, level2);
      Validate.notNull(category);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private BlogModel innerQueryBlogModel(HttpServletRequest request) {
    BlogModel model = new BlogModel();
    model.setBasic(super.prepareBaseModel(request));

    Long id = Long.parseLong(request.getParameter(Field.ID.getKeyName()));
    Validate.notNull(id);
    // blog detail
    final Blog blog = this.blogService.queryBlogDetail(id);
    // breadcrumb
    List<Category> categorys =
        this.categoryService.queryCategorys(Long.valueOf(blog.getCategory1()),
            Long.valueOf(blog.getCategory2()));
    Validate.isTrue(categorys != null && 2 == categorys.size());
    model.setTitle(blog.getTitle());
    model.setAuthor(blog.getUserName());
    model.setAddTime(DateTimeUtil.dateToSecond(blog.getAddTime()));
    model.setContent(blog.getContent());

    List<Map<String, Object>> breadcrumb = new ArrayList<Map<String, Object>>();
    for (final Category category : categorys) {
      breadcrumb.add(new HashMap<String, Object>() {
        {
          put(Field.NAME.getKeyName(), category.getName());
          if (category.getLevel() == 1) {
            put(Field.LINK.getKeyName(), "/blogs/root?id=" + blog.getCategory1());
          } else {
            put(Field.LINK.getKeyName(), "/blogList?rootCategory=" + blog.getCategory1()
                + "&childCategory=" + blog.getCategory2());
          }
        }
      });
    }
    model.setBreadcrumb(breadcrumb);
    model.setRootCategory(blog.getCategory1());
    return model;
  }

  private boolean validateEmail(String mail) {
    Matcher matcher = BizConstants.EMIAL_PATTERN.matcher(mail);
    return matcher.matches();
  }
}
