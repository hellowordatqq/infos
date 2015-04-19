package com.github.colingan.infos.dal.link;

import com.github.colingan.infos.dal.common.IGenericDAO;
import com.github.colingan.infos.dal.link.bo.Link;

public interface LinkDAO extends IGenericDAO<Link> {

  Long addLink(Link link);

  boolean update(long id, String name, String link);
}
