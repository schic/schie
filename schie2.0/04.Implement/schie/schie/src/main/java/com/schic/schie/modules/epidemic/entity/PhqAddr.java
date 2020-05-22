/**
 * *
 */
package com.schic.schie.modules.epidemic.entity;

import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 高频地址实体Entity
 * @author leo
 * @version 2020-03-11
 */
public class PhqAddr extends AbstractBaseEntity<PhqAddr> {
	
	private static final long serialVersionUID = 1L;
	private String pid;		// 病患id
	private String ptel;		// 病人电话
	private String hqAddr;		// 高频热点地址
	private Integer stayHours;		// 累计驻留时长
	private Date beginCreateDate;		// 开始 入库时间
	private Date endCreateDate;		// 结束 入库时间
	
	public PhqAddr() {
		super();
	}

	public PhqAddr(String id){
		super(id);
	}

	@Length(min=0, max=64, message="病患id长度必须介于 0 和 64 之间")
	@ExcelField(title="病患id", align=2, sort=1)
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}


	@Length(min=0, max=100, message="病人电话长度必须介于 0 和 100 之间")
	@ExcelField(title="病人电话", align=2, sort=2)
	public String getPtel() {
		return ptel;
	}

	public void setPtel(String ptel) {
		this.ptel = ptel;
	}


	@Length(min=0, max=255, message="高频热点地址长度必须介于 0 和 255 之间")
	@ExcelField(title="高频热点地址", align=2, sort=3)
	public String getHqAddr() {
		return hqAddr;
	}

	public void setHqAddr(String hqAddr) {
		this.hqAddr = hqAddr;
	}


	@ExcelField(title="累计驻留时长", align=2, sort=4)
	public Integer getStayHours() {
		return stayHours;
	}

	public void setStayHours(Integer stayHours) {
		this.stayHours = stayHours;
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
