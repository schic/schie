/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.test.service.tree;

import com.jeespring.common.service.TreeService;
import com.jeespring.common.utils.StringUtils;
import com.schic.schie.modules.test.dao.tree.TestTreeDao;
import com.schic.schie.modules.test.entity.tree.TestTree;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 树Service
 * @author JeeSpring
 * @version 2018-12-13
 */
@Service
@Transactional(readOnly = true)
public class TestTreeService extends TreeService<TestTreeDao, TestTree> {
    @Override
    public TestTree get(String id) {
        return super.get(id);
    }
    
    public List<TestTree> findList(TestTree testTree) {
        if (StringUtils.isNotBlank(testTree.getParentIds())){
            testTree.setParentIds(","+testTree.getParentIds()+",");
        }
        return super.findList(testTree);
    }
    @Override
    @Transactional(readOnly = false)
    public void save(TestTree testTree) {
        super.save(testTree);
    }
    
    @Transactional(readOnly = false)
    public void delete(TestTree testTree) {
        super.delete(testTree);
    }
    
}
