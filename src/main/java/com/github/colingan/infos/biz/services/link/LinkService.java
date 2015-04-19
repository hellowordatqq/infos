package com.github.colingan.infos.biz.services.link;

import java.util.List;

import com.github.colingan.infos.dal.link.bo.Link;

public interface LinkService {

  List<Link> queryAllLinks();

  boolean delete(long id);

  Link addLink(String name, String link);

  boolean updateLink(Long id, String name, String link);
}
