/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.test.web.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeespring.common.config.Global;
import com.jeespring.common.utils.StringUtils;
import com.jeespring.common.web.AbstractBaseController;
import com.jeespring.modules.sys.dao.OfficeDao;
import com.jeespring.modules.sys.dao.UserDao;
import com.jeespring.modules.sys.entity.Office;
import com.jeespring.modules.sys.entity.User;
import com.jeespring.modules.sys.security.SystemAuthorizingRealm;
import com.schic.schie.modules.resource.entity.ExResources;
import com.schic.schie.modules.resource.service.IExResourcesService;
import com.schic.schie.modules.test.entity.tree.TestTree;
import com.schic.schie.modules.test.service.tree.TestTreeService;
import org.apache.shiro.SecurityUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.schic.schie.modules.common.ExChangeConst.ADMINORGID;


/**
 * 树Controller
 *
 * @author JeeSpring
 * @version 2018-12-13
 */
@Controller
@RequestMapping(value = "${adminPath}/test/tree/testTree")
public class TestTreeController extends AbstractBaseController {
    public static final String RESOURCETYPE = "1";
    @Autowired
    private TestTreeService testTreeService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OfficeDao officeDao;

    @Autowired
    private IExResourcesService resourcesService;

    @ModelAttribute
    public TestTree get(@RequestParam(required = false) String id) {
        TestTree entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = testTreeService.get(id);
        }
        if (entity == null) {
            entity = new TestTree();
        }
        return entity;
    }

    /**
     * 树列表页面
     */
    //@RequiresPermissions("test:tree:testTree:list")
    @RequestMapping(value = {"list", ""})
    public String list(TestTree testTree, HttpServletRequest request, HttpServletResponse response, Model model, String type) {
        User user = new User();
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject().getPrincipal();
        String loginName = principal.getLoginName();
        user.setLoginName(loginName);
        User returnuser = userDao.getByLoginName(user);
        @NotNull(message = "归属部门不能为空") Office office = returnuser.getOffice();
//         String officeId = office.getParentIds().substring(0, 1);
        String id = office.getId();
        testTree.setOrgid(id);
        if (type != null || type != "") {
            testTree.setType(type);
            model.addAttribute("type", type);
        }
//        testTree.setOrgid(returnuser.getCompany());
        List<TestTree> list = testTreeService.findList(testTree);
        model.addAttribute("list", list);
        return "modules/test/tree/testTreeList";
    }

    /**
     * 查看，增加，编辑树表单页面
     */
    //@RequiresPermissions(value={"test:tree:testTree:view","test:tree:testTree:add","test:tree:testTree:edit"},logical=Logical.OR)
    @RequestMapping(value = "form")
    public String form(TestTree testTree, Model model) {
        if (testTree.getParent() != null && StringUtils.isNotBlank(testTree.getParent().getId())) {
            testTree.setParent(testTreeService.get(testTree.getParent().getId()));
            // 获取排序号，最末节点排序号+30
            if (StringUtils.isBlank(testTree.getId())) {
                TestTree testTreeChild = new TestTree();
                testTreeChild.setParent(new TestTree(testTree.getParent().getId()));
                List<TestTree> list = testTreeService.findList(testTree);
                if (!list.isEmpty()) {
                    testTree.setSort(list.get(list.size() - 1).getSort());
                    if (testTree.getSort() != null) {
                        testTree.setSort(testTree.getSort() + 30);
                    }
                }
            }
        }
        if (testTree.getSort() == null) {
            testTree.setSort(30);
        }
        model.addAttribute("testTree", testTree);
        return "modules/test/tree/testTreeForm";
    }

    /**
     * 保存树
     */
    //@RequiresPermissions(value={"test:tree:testTree:add","test:tree:testTree:edit"},logical=Logical.OR)
    @RequestMapping(value = "save")
    public String save(TestTree testTree, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, testTree)) {
            return form(testTree, model);
        }
        if (!(" ").equals(testTree.getParentId())) {
            TestTree tree = new TestTree();
            tree.setId(testTree.getParentId());
            TestTree testTree1 = testTreeService.get(tree);
            testTree.setType(testTree1.getType());
        }
        User user = new User();
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject().getPrincipal();
        String loginName = principal.getLoginName();
        user.setLoginName(loginName);
        User returnuser = userDao.getByLoginName(user);
        @NotNull(message = "归属部门不能为空") Office office = returnuser.getOffice();
        String id = office.getId();
        testTree.setOrgid(id);
        testTreeService.save(testTree);
        addMessage(redirectAttributes, "保存树成功");
        return "redirect:" + Global.getAdminPath() + "/test/tree/testTree/?repage&type="+testTree.getType();
    }

    /**
     * 删除树
     */
    //@RequiresPermissions("test:tree:testTree:del")
    @RequestMapping(value = "delete")
    public String delete(TestTree testTree, RedirectAttributes redirectAttributes) {
        testTreeService.delete(testTree);
        addMessage(redirectAttributes, "删除树成功");
        return "redirect:" + Global.getAdminPath() + "/test/tree/testTree/?repage&type="+testTree.getType();
    }

    /**
     * 数据资源管理树数据
     *资源管理,节点管理的树走这里
     * @param extId
     * @param response
     * @return
     */
    //@RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, HttpServletResponse response, String type, Model model,TestTree trees) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        TestTree testTree = new TestTree();
        User user = new User();
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject().getPrincipal();
        String loginName = principal.getLoginName();
        user.setLoginName(loginName);
        User returnuser = userDao.getByLoginName(user);
        @NotNull(message = "归属部门不能为空") Office office = returnuser.getOffice();
//         String officeId = office.getParentIds().substring(0, 1);
        if (extId == null || ("").equals(extId)) {
            if ("".equals(trees.getOrgid())||trees.getOrgid()==null){
                String id = office.getId();
                testTree.setOrgid(id);
            }
//            else{
//                testTree.setOrgid(trees.getOrgid());
//            }
        }
        else if (!"".equals(trees.getOrgid())||trees.getOrgid()!=null){
            testTree.setOrgid(trees.getOrgid());
        }
        if (type != null || type != "") {
            testTree.setType(type);
        }
        List<TestTree> list = testTreeService.findList(testTree);
        if (!office.getId().equals(ADMINORGID)) {
            TestTree tree = new TestTree();
            tree.setParent(tree);
            if (type != null || type != "") {
                tree.setType(type);
            }
            tree.setExTabId("0");
            List<TestTree> list1 = testTreeService.findList(tree);
            list.addAll(list1);
        }
        for (int i = 0; i < list.size(); i++) {
            TestTree e = list.get(i);
            if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                map.put("orgid", e.getOrgid());
                mapList.add(map);
            }

        }
        return mapList;
    }

    /**
     * 数据表管理树数据
     *资源申请的树走这里
     * @param extId
     * @param response
     * @return
     */
    //@RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "tabTreeData")
    public List<Map<String, Object>> treeDatac(@RequestParam(required = false) String extId, String comId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        TestTree testTree = new TestTree();
        User user = new User();
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject().getPrincipal();
        String loginName = principal.getLoginName();
        user.setLoginName(loginName);
        User returnuser = userDao.getByLoginName(user);
        @NotNull(message = "归属部门不能为空") Office office = returnuser.getOffice();
        String id = office.getId();
        if (!id.equals(ADMINORGID)) {
            testTree.setOrgid(id);
        }
        if (comId != null && comId.length() != 0){
            testTree.setOrgid(comId);
        }
        testTree.setType("1");
        List<TestTree> list = testTreeService.findList(testTree);
        for (int i = 0; i < list.size(); i++) {
            TestTree e = list.get(i);
            if (StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                Office office1 = officeDao.get(e.getOrgid());
                @Length(min = 1, max = 100) String name = office1.getName();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName() + "(" + name + ")");
                map.put("orgid", e.getOrgid());
                mapList.add(map);
                ExResources resources = new ExResources();
                resources.setResdirId(e.getId());
                List<ExResources> list1 = resourcesService.findList(resources);
                for (int j = 0; j < list1.size(); j++) {
                    Map<String, Object> map1 = Maps.newHashMap();
                    map1.put("id", list1.get(j).getId());
                    map1.put("pId", e.getId());
                    map1.put("name", list1.get(j).getName());
                    map1.put("type",RESOURCETYPE);
                    map1.put("orgid", e.getOrgid());
                    mapList.add(map1);
                }
            }

        }
        return mapList;
    }

    @ResponseBody
    @RequestMapping(value = "judgeOrg")
    public Map<String, String> Admin() {
        Map<String, String> map = new HashMap<>();
        User user = new User();
        SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal) SecurityUtils.getSubject().getPrincipal();
        String loginName = principal.getLoginName();
        user.setLoginName(loginName);
        User returnuser = userDao.getByLoginName(user);
        @NotNull(message = "归属部门不能为空") Office office = returnuser.getOffice();
        String id = office.getId();
        if (id.equals(ADMINORGID)) {
            map.put("orgid", "true");
        } else {
            map.put("orgid", "false");
        }

        return map;
    }
}
