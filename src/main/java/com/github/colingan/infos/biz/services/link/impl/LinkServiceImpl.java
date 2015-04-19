package com.github.colingan.infos.biz.services.link.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import com.github.colingan.infos.biz.services.link.LinkService;
import com.github.colingan.infos.dal.common.CommonOrderBy;
import com.github.colingan.infos.dal.common.CommonOrderBy.OrderByDirection;
import com.github.colingan.infos.dal.common.ComparisonCondition;
import com.github.colingan.infos.dal.common.ComparisonCondition.Operation;
import com.github.colingan.infos.dal.common.Field;
import com.github.colingan.infos.dal.common.OrderBy;
import com.github.colingan.infos.dal.link.LinkDAO;
import com.github.colingan.infos.dal.link.bo.Link;

@Service
public class LinkServiceImpl implements LinkService {

  @Resource
  private LinkDAO linkDao;

  @Override
  public List<Link> queryAllLinks() {
    List<Field> fields = new ArrayList<Field>();
    fields.add(Field.ID);
    fields.add(Field.LINK_NAME);
    fields.add(Field.LINK);

    OrderBy orderBy = new CommonOrderBy(Field.ID, OrderByDirection.ASC);
    return this.linkDao.getObjects(null, fields, orderBy);
  }

  @Override
  public boolean delete(long id) {

    return this.linkDao.delete(null, new ComparisonCondition(Field.ID, Operation.EQ, id)) == 1;
  }

  @Override
  public Link addLink(String name, String link) {
    Validate.notEmpty(name, "name should not be empty.");
    Validate.notEmpty(link, "link should not be empty.");
    Link linkObj = new Link();
    linkObj.setAddTime(new Date());
    linkObj.setLink(link);
    linkObj.setName(name);
    linkObj.setUpdateTime(new Date());
    linkObj.setId(this.linkDao.addLink(linkObj));
    return linkObj;
  }

  @Override
  public boolean updateLink(Long id, String name, String link) {
    Validate.notNull(id, "id should not be null");
    Validate.notEmpty(name, "name should not be empty.");
    Validate.notEmpty(link, "link should not be empty.");

    return this.linkDao.update(id, name, link);
  }

}
