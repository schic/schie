/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.database.entity;

import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import com.jeespring.modules.sys.utils.DictUtils;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * DBEntity
 * @author DHP
 * @version 2019-08-07
 */
public class ExDb extends AbstractBaseEntity<ExDb> {
	
	private static final long serialVersionUID = 1L;
	private String companyId;		// 机构id
	private String dbName;		// 数据库名称
	private String dbType;		// 数据库类型
	private String dbTypeLabel;		// 数据库类型Label
	private String dbTypePicture;		// 数据库类型Picture
	private String dbUrl;		// 数据库url
	private String dbUser;		// 数据库用户
	private String dbPwd;		// 数据库密码
	private Double sort;		// 排序
	private Date cdate;		// 创建时间
	private String cuser;		// 创建人
	private Date mdate;		// 修改时间
	private String muser;		// 修改人
	private Date beginCdate;		// 开始 创建时间
	private Date endCdate;		// 结束 创建时间
	private Date beginMdate;		// 开始 修改时间
	private Date endMdate;		// 结束 修改时间
	
	public ExDb() {
		super();
	}

	public ExDb(String id){
		super(id);
	}

	@Length(min=0, max=36, message="机构id长度必须介于 0 和 36 之间")
				@ExcelField(title="机构id", align=2, sort=1)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


	@Length(min=0, max=100, message="数据库名称长度必须介于 0 和 100 之间")
				@ExcelField(title="数据库名称", align=2, sort=2)
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}


	@Length(min=0, max=20, message="数据库类型长度必须介于 0 和 20 之间")
				@ExcelField(title="数据库类型", dictType="", align=2, sort=3)
	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}


	public String getDbTypeLabel() {
		return DictUtils.getDictLabel(dbType,"","");
	}
	public String getDbTypePicture() {
		return DictUtils.getDictPicture(dbType,"","");
	}
	@Length(min=0, max=200, message="数据库url长度必须介于 0 和 200 之间")
				@ExcelField(title="数据库url", align=2, sort=4)
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}


	@Length(min=0, max=100, message="数据库用户长度必须介于 0 和 100 之间")
				@ExcelField(title="数据库用户", align=2, sort=5)
	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}


	@Length(min=0, max=100, message="数据库密码长度必须介于 0 和 100 之间")
				@ExcelField(title="数据库密码", align=2, sort=6)
	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}


				@ExcelField(title="排序", align=2, sort=7)
	public Double getSort() {
		return sort;
	}

	public void setSort(Double sort) {
		this.sort = sort;
	}


	public Date getCdate() {
		return cdate;
	}

	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}


	@Length(min=1, max=36, message="创建人长度必须介于 1 和 36 之间")
				@ExcelField(title="创建人", align=2, sort=10)
	public String getCuser() {
		return cuser;
	}

	public void setCuser(String cuser) {
		this.cuser = cuser;
	}


	public Date getMdate() {
		return mdate;
	}

	public void setMdate(Date mdate) {
		this.mdate = mdate;
	}


	public String getMuser() {
		return muser;
	}

	public void setMuser(String muser) {
		this.muser = muser;
	}


	public Date getBeginCdate() {
		return beginCdate;
	}

	public void setBeginCdate(Date beginCdate) {
		this.beginCdate = beginCdate;
	}
	
	public Date getEndCdate() {
		return endCdate;
	}

	public void setEndCdate(Date endCdate) {
		this.endCdate = endCdate;
	}
		
	public Date getBeginMdate() {
		return beginMdate;
	}

	public void setBeginMdate(Date beginMdate) {
		this.beginMdate = beginMdate;
	}
	
	public Date getEndMdate() {
		return endMdate;
	}

	public void setEndMdate(Date endMdate) {
		this.endMdate = endMdate;
	}
		
}