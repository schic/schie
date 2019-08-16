/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exchange.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 数据表管理Entity
 * @author XC
 * @version 2019-07-31
 */
public class ExDbStandard extends AbstractBaseEntity<ExDbStandard> {
	
	private static final long serialVersionUID = 1L;

	private ExDbStandard parent;		// 父级id
	private String parentIds;		// 所有父级id
	private String fType;		// 类型
	private String fTypeLabel;		// 类型Label
	private String fTypePicture;		// 类型Picture
	private String name;		// 名称
	private String inId;		// 标识符
	private String nameCn;		// 中文名
	private String fieldName;		// 字段名
	private String datatype;		// 数据类型
	private String datalength;		// 数据长度
	private String fRequire;		// 填报要求
	private Double sort;		// 排序
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private String orgid;          //组织机构ID
	private String exTabId;        //数据表ID

	public String getExTabId() {
		return exTabId;
	}

	public void setExTabId(String exTabId) {
		this.exTabId = exTabId;
	}

	public ExDbStandard() {
		super();
	}

	public ExDbStandard(String id){
		super(id);
	}

	@JsonBackReference
				@ExcelField(title="父级id", align=2, sort=1)
	public ExDbStandard getParent() {
		return parent;
	}

	public void setParent(ExDbStandard parent) {
		this.parent = parent;
	}
				@ExcelField(title="所有父级id", align=2, sort=2)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getfType() {
		return fType;
	}

	public void setfType(String fType) {
		this.fType = fType;
	}

	public String getfTypeLabel() {
		return fTypeLabel;
	}

	public void setfTypeLabel(String fTypeLabel) {
		this.fTypeLabel = fTypeLabel;
	}

	public String getfTypePicture() {
		return fTypePicture;
	}

	public void setfTypePicture(String fTypePicture) {
		this.fTypePicture = fTypePicture;
	}
	@Length(min=0, max=20, message="填报要求长度必须介于 0 和 20 之间")
	@ExcelField(title="填报要求", align=2, sort=10)
	public String getfRequire() {
		return fRequire;
	}

	public void setfRequire(String fRequire) {
		this.fRequire = fRequire;
	}


	@Length(min=0, max=100, message="名称长度必须介于 0 和 100 之间")
				@ExcelField(title="表名称", align=2, sort=4)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Length(min=0, max=100, message="标识符长度必须介于 0 和 100 之间")
				@ExcelField(title="标识符", align=2, sort=5)
	public String getInId() {
		return inId;
	}

	public void setInId(String inId) {
		this.inId = inId;
	}


	@Length(min=0, max=100, message="中文名长度必须介于 0 和 100 之间")
				@ExcelField(title="中文名", align=2, sort=6)
	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}


	@Length(min=0, max=100, message="字段名长度必须介于 0 和 100 之间")
				@ExcelField(title="字段名", align=2, sort=7)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}


	@Length(min=0, max=20, message="数据类型长度必须介于 0 和 20 之间")
				@ExcelField(title="数据类型", align=2, sort=8)
	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}


	@Length(min=0, max=20, message="数据长度长度必须介于 0 和 20 之间")
				@ExcelField(title="数据长度", align=2, sort=9)
	public String getDatalength() {
		return datalength;
	}

	public void setDatalength(String datalength) {
		this.datalength = datalength;
	}




				@ExcelField(title="排序", align=2, sort=11)
	public Double getSort() {
		return sort;
	}

	public void setSort(Double sort) {
		this.sort = sort;
	}


	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
}