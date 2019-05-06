package org.schic.modules.sys.utils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.schic.common.sms.SMSUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import org.schic.common.utils.CacheUtils;
import org.schic.common.utils.SpringContextHolder;
import org.schic.common.service.AbstractService;
import org.schic.modules.sys.dao.AreaDao;
import org.schic.modules.sys.dao.MenuDao;
import org.schic.modules.sys.dao.OfficeDao;
import org.schic.modules.sys.dao.RoleDao;
import org.schic.modules.sys.dao.UserDao;
import org.schic.modules.sys.entity.Area;
import org.schic.modules.sys.entity.Menu;
import org.schic.modules.sys.entity.Office;
import org.schic.modules.sys.entity.Role;
import org.schic.modules.sys.entity.User;
import org.schic.modules.sys.security.SystemAuthorizingRealm.Principal;

/**
 * 
 * @Description: 用户工具类
 * @author Caiwb
 * @date 2019年5月6日 上午11:23:34
 */
public class UserUtils {

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static OfficeDao officeDao = SpringContextHolder
			.getBean(OfficeDao.class);

	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID = "id_";
	public static final String USER_CACHE_LOGIN_NAME = "ln";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID = "oid_";

	public static final String CACHE_ROLE_LIST = "roleList";
	public static final String CACHE_MENU_LIST = "menuList";
	public static final String CACHE_AREA_LIST = "areaList";
	public static final String CACHE_OFFICE_LIST = "officeList";
	public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

	private UserUtils() {

	}

	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id) {
		User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_ID + id);
		if (user == null) {
			user = userDao.get(id);
			if (user == null) {
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID + user.getId(), user);
			CacheUtils.put(USER_CACHE,
					USER_CACHE_LOGIN_NAME + user.getLoginName(), user);
		}
		return user;
	}

	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginName) {
		User user = (User) CacheUtils.get(USER_CACHE,
				USER_CACHE_LOGIN_NAME + loginName);
		if (user == null) {
			user = userDao.getByLoginName(new User(null, loginName));
			if (user == null) {
				return null;
			}
			user.setRoleList(roleDao.findList(new Role(user)));
			CacheUtils.put(USER_CACHE, USER_CACHE_ID + user.getId(), user);
			CacheUtils.put(USER_CACHE,
					USER_CACHE_LOGIN_NAME + user.getLoginName(), user);
		}
		return user;
	}

	/**
	 * 清除当前用户缓存
	 */
	public static void clearCache() {
		removeCache(CACHE_ROLE_LIST);
		removeCache(CACHE_MENU_LIST);
		removeCache(CACHE_AREA_LIST);
		removeCache(CACHE_OFFICE_LIST);
		removeCache(CACHE_OFFICE_ALL_LIST);
		UserUtils.clearCache(getUser());
	}

	/**
	 * 清除指定用户缓存
	 * @param user
	 */
	public static void clearCache(User user) {
		CacheUtils.remove(USER_CACHE, USER_CACHE_ID + user.getId());
		CacheUtils.remove(USER_CACHE,
				USER_CACHE_LOGIN_NAME + user.getLoginName());
		CacheUtils.remove(USER_CACHE,
				USER_CACHE_LOGIN_NAME + user.getOldLoginName());
		if (user.getOffice().getId() != null) {
			CacheUtils.remove(USER_CACHE,
					USER_CACHE_LIST_BY_OFFICE_ID + user.getOffice().getId());
		}
	}

	/**
	 * 获取当前用户
	 * @return 取不到返回 new User()
	 */
	public static User getUser() {
		Principal principal = getPrincipal();
		if (principal != null) {
			User user = get(principal.getId());
			if (user != null) {
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}

	/**
	 * 获取当前用户角色列表
	 * @return
	 */
	public static List<Role> getRoleList() {
		@SuppressWarnings("unchecked")
		List<Role> roleList = (List<Role>) getCache(CACHE_ROLE_LIST);
		if (roleList == null) {
			User user = getUser();
			if (user.isAdmin()) {
				roleList = roleDao.findAllList(new Role());
			} else {
				Role role = new Role();
				role.getSqlMap().put("dsf", AbstractService
						.dataScopeFilter(user.getCurrentUser(), "o", "u"));
				roleList = roleDao.findList(role);
			}
			putCache(CACHE_ROLE_LIST, roleList);
		}
		return roleList;
	}

	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static List<Menu> getMenuList() {
		@SuppressWarnings("unchecked")
		List<Menu> menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
		if (menuList == null) {
			User user = getUser();
			if (user.isAdmin()) {
				menuList = menuDao.findAllList(new Menu());
			} else {
				Menu m = new Menu();
				m.setUserId(user.getId());
				menuList = menuDao.findByUserId(m);
			}
			putCache(CACHE_MENU_LIST, menuList);
		}
		return menuList;
	}

	/**
	 * 获取当前用户授权菜单
	 * @return
	 */
	public static Menu getTopMenu() {
		Menu.setAllMenu(UserUtils.getMenuList());
		return menuDao.findUniqueByProperty("parent_id", "'0'");
	}
	/**
	 * 获取当前用户授权的区域
	 * @return
	 */
	public static List<Area> getAreaList() {
		@SuppressWarnings("unchecked")
		List<Area> areaList = (List<Area>) getCache(CACHE_AREA_LIST);
		if (areaList == null) {
			areaList = areaDao.findAllList(new Area());
			putCache(CACHE_AREA_LIST, areaList);
		}
		return areaList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeList() {
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_LIST);
		if (officeList == null) {
			User user = getUser();
			if (user.isAdmin()) {
				officeList = officeDao.findAllList(new Office());
			} else {
				Office office = new Office();
				office.getSqlMap().put("dsf",
						AbstractService.dataScopeFilter(user, "a", ""));
				officeList = officeDao.findList(office);
			}
			putCache(CACHE_OFFICE_LIST, officeList);
		}
		return officeList;
	}

	/**
	 * 获取当前用户有权限访问的部门
	 * @return
	 */
	public static List<Office> getOfficeAllList() {
		@SuppressWarnings("unchecked")
		List<Office> officeList = (List<Office>) getCache(
				CACHE_OFFICE_ALL_LIST);
		if (officeList == null) {
			officeList = officeDao.findAllList(new Office());
		}
		return officeList;
	}

	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal() {
		try {
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null) {
				return principal;
			}
		} catch (UnavailableSecurityManagerException e) {

		} catch (InvalidSessionException e) {

		}
		return null;
	}

	public static Session getSession() {
		try {
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null) {
				session = subject.getSession();
			}
			if (session != null) {
				return session;
			}
		} catch (InvalidSessionException e) {

		}
		return null;
	}

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		Object obj = getSession().getAttribute(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}

	public static String getTime(Date date) {
		StringBuilder time = new StringBuilder();
		Date date2 = new Date();
		long temp = date2.getTime() - date.getTime();
		long days = temp / 1000 / 3600 / 24; //相差小时数
		if (days > 0) {
			time.append(days + "天");
		}
		long temp1 = temp % (1000 * 3600 * 24);
		long hours = temp1 / 1000 / 3600; //相差小时数
		if (days > 0 || hours > 0) {
			time.append(hours + "小时");
		}
		long temp2 = temp1 % (1000 * 3600);
		long mins = temp2 / 1000 / 60; //相差分钟数
		time.append(mins + "分钟");
		return time.toString();
	}

	//发送注册码
	public static String sendRandomCode(String uid, String pwd, String tel,
			String randomCode) throws IOException {
		//发送内容
		String content = "您的验证码是：" + randomCode + "，有效期30分钟，请在有效期内使用。";
		try {
			return SMSUtils.send(uid, pwd, tel, content);
		} catch (Exception e) {
			return "";
		}
	}

	//注册用户重置密码
	public static String sendPass(String uid, String pwd, String tel,
			String password) throws IOException {
		//发送内容
		String content = "您的新密码是：" + password + "，请登录系统，重新设置密码。";
		try {
			return SMSUtils.send(uid, pwd, tel, content);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 导出Excel调用,根据姓名转换为ID
	 */
	public static User getByUserName(String name) {
		User u = new User();
		u.setName(name);
		List<User> list = userDao.findList(u);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return new User();
		}
	}
	/**
	 * 导出Excel使用，根据名字转换为id
	 */
	public static Office getByOfficeName(String name) {
		Office o = new Office();
		o.setName(name);
		List<Office> list = officeDao.findList(o);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return new Office();
		}
	}
	/**
	 * 导出Excel使用，根据名字转换为id
	 */
	public static Area getByAreaName(String name) {
		Area a = new Area();
		a.setName(name);
		List<Area> list = areaDao.findList(a);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return new Area();
		}
	}
}
