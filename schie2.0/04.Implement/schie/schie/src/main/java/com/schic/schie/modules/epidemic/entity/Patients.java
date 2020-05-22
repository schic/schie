/**
 * *
 */
package com.schic.schie.modules.epidemic.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 病患实体类Entity
 * @author Leo
 * @version 2020-03-11
 */
public class Patients extends AbstractBaseEntity<Patients> {
	
	private static final long serialVersionUID = 1L;
	private String pname;		// 病人姓名
	private String ptel;		// 病人电话
	private String ofShizhou;		// 所属市周
	private String sfId;		// 病人身份证
	private String ptype;		// 病人类型；0，疑似；1，确诊
	private Date createDate;		// 入库时间
	private Date beginCreateDate;		// 开始 入库时间
	private Date endCreateDate;		// 结束 入库时间
	
	public Patients() {
		super();
	}

	public Patients(String id){
		super(id);
	}

	@Length(min=0, max=100, message="病人姓名长度必须介于 0 和 100 之间")
    @ExcelField(title="病人姓名", align=2, sort=1)
	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}


	@Length(min=0, max=100, message="病人电话长度必须介于 0 和 100 之间")
    @ExcelField(title="病人电话", align=2, sort=2)
	public String getPtel() {
		return ptel;
	}

	public void setPtel(String ptel) {
		this.ptel = ptel;
	}


	@Length(min=0, max=255, message="所属市周长度必须介于 0 和 255 之间")
    @ExcelField(title="所属市周", align=2, sort=3)
	public String getOfShizhou() {
		return ofShizhou;
	}

	public void setOfShizhou(String ofShizhou) {
		this.ofShizhou = ofShizhou;
	}


	@Length(min=0, max=50, message="病人身份证长度必须介于 0 和 50 之间")
    @ExcelField(title="病人身份证", align=2, sort=4)
	public String getSfId() {
		return sfId;
	}

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}


	@Length(min=0, max=1, message="病人类型；0，疑似；1，确诊长度必须介于 0 和 1 之间")
    @ExcelField(title="病人类型；0，疑似；1，确诊", align=2, sort=5)
	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Override
	public Date getCreateDate() {
		return createDate;
	}

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Override
	public void setCreateDate(Date createDate) {
        this.createDate = createDate;
	}
}
