/**
 *
 */
package com.schic.schie.modules.onlinenode.service;

import com.jeespring.common.persistence.Page;
import com.jeespring.common.service.AbstractBaseService;
import com.schic.schie.modules.onlinenode.dao.OnlineNodeDao;
import com.schic.schie.modules.onlinenode.entity.OnlineNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlineNodeService extends AbstractBaseService<OnlineNodeDao, OnlineNode> implements IOnlineNodeService {

    @Override
    public OnlineNode getCache(String id) {
        return null;
    }

    @Override
    public List<OnlineNode> totalCache(OnlineNode entity) {
        return null;
    }

    @Override
    public List<OnlineNode> findListCache(OnlineNode entity) {
        return null;
    }


    @Override
    public Page<OnlineNode> findPageCache(Page<OnlineNode> page, OnlineNode entity) {
        return null;
    }

}
