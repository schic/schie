/**
 * * Copyright &copy; 2015-2020 <a href="https://gitee.com/JeeHuangBingGui/jeeSpringCloud">JeeSpringCloud</a> All rights reserved..
 */
package com.schic.schie.modules.exask.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 保存日志Entity
 * @author leodeyang
 * @version 2019-08-09
 */
public class ExBatchLog extends AbstractBaseEntity<ExBatchLog> {
	
	private static final long serialVersionUID = 1L;
	private String resAskid;		// 请求的id
	private Long srcRows;		// 源头 执行行数
	private Date srcExeBegintime;		// 源头 执行开始时间
	private Date srcExeEndtime;		// 源头 执行结束时间
	private Integer srcIsend;		// 源头 是否结束。0为false，1为true
	private Long srcExeCosttime;		// 源头 执行耗时
	private Date tarExeBegintime;		// 目标 执行开始时间
	private Date tarExeEndtime;		// 目标 执行结束时间
	private Long tarExeCosttime;		// 目标 执行耗时
	private Long tarRowsUpdate;		// 目标 更新行数
	private Long tarRowsInsert;		// 目标 插入行数
	private Long tarRowsIgnore;		// 目标 忽略行数
	private Date beginSrcExeBegintime;		// 开始 源头 执行开始时间
	private Date endSrcExeBegintime;		// 结束 源头 执行开始时间
	private Date beginSrcExeEndtime;		// 开始 源头 执行结束时间
	private Date endSrcExeEndtime;		// 结束 源头 执行结束时间
	private Date beginTarExeBegintime;		// 开始 目标 执行开始时间
	private Date endTarExeBegintime;		// 结束 目标 执行开始时间
	private Date beginTarExeEndtime;		// 开始 目标 执行结束时间
	private Date endTarExeEndtime;		// 结束 目标 执行结束时间
	private Date beginCreateDate;		// 开始 本条数据生成时间
	private Date endCreateDate;		// 结束 本条数据生成时间
	
	public ExBatchLog() {
		super();
	}

	public ExBatchLog(String id){
		super(id);
	}

	@Length(min=0, max=36, message="请求的id长度必须介于 0 和 36 之间")
				@ExcelField(title="请求的id", align=2, sort=1)
	public String getResAskid() {
		return resAskid;
	}

	public void setResAskid(String resAskid) {
		this.resAskid = resAskid;
	}


				@ExcelField(title="源头 执行行数", align=2, sort=2)
	public Long getSrcRows() {
		return srcRows;
	}

	public void setSrcRows(Long srcRows) {
		this.srcRows = srcRows;
	}


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
				@ExcelField(title="源头 执行开始时间", align=2, sort=3)
	public Date getSrcExeBegintime() {
		return srcExeBegintime;
	}

	public void setSrcExeBegintime(Date srcExeBegintime) {
		this.srcExeBegintime = srcExeBegintime;
	}


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
				@ExcelField(title="源头 执行结束时间", align=2, sort=4)
	public Date getSrcExeEndtime() {
		return srcExeEndtime;
	}

	public void setSrcExeEndtime(Date srcExeEndtime) {
		this.srcExeEndtime = srcExeEndtime;
	}


				@ExcelField(title="源头 是否结束。0为false，1为true", align=2, sort=5)
	public Integer getSrcIsend() {
		return srcIsend;
	}

	public void setSrcIsend(Integer srcIsend) {
		this.srcIsend = srcIsend;
	}


				@ExcelField(title="源头 执行耗时", align=2, sort=6)
	public Long getSrcExeCosttime() {
		return srcExeCosttime;
	}

	public void setSrcExeCosttime(Long srcExeCosttime) {
		this.srcExeCosttime = srcExeCosttime;
	}


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
				@ExcelField(title="目标 执行开始时间", align=2, sort=7)
	public Date getTarExeBegintime() {
		return tarExeBegintime;
	}

	public void setTarExeBegintime(Date tarExeBegintime) {
		this.tarExeBegintime = tarExeBegintime;
	}


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
				@ExcelField(title="目标 执行结束时间", align=2, sort=8)
	public Date getTarExeEndtime() {
		return tarExeEndtime;
	}

	public void setTarExeEndtime(Date tarExeEndtime) {
		this.tarExeEndtime = tarExeEndtime;
	}


				@ExcelField(title="目标 执行耗时", align=2, sort=9)
	public Long getTarExeCosttime() {
		return tarExeCosttime;
	}

	public void setTarExeCosttime(Long tarExeCosttime) {
		this.tarExeCosttime = tarExeCosttime;
	}


				@ExcelField(title="目标 更新行数", align=2, sort=10)
	public Long getTarRowsUpdate() {
		return tarRowsUpdate;
	}

	public void setTarRowsUpdate(Long tarRowsUpdate) {
		this.tarRowsUpdate = tarRowsUpdate;
	}


				@ExcelField(title="目标 插入行数", align=2, sort=11)
	public Long getTarRowsInsert() {
		return tarRowsInsert;
	}

	public void setTarRowsInsert(Long tarRowsInsert) {
		this.tarRowsInsert = tarRowsInsert;
	}


				@ExcelField(title="目标 忽略行数", align=2, sort=12)
	public Long getTarRowsIgnore() {
		return tarRowsIgnore;
	}

	public void setTarRowsIgnore(Long tarRowsIgnore) {
		this.tarRowsIgnore = tarRowsIgnore;
	}


	public Date getBeginSrcExeBegintime() {
		return beginSrcExeBegintime;
	}

	public void setBeginSrcExeBegintime(Date beginSrcExeBegintime) {
		this.beginSrcExeBegintime = beginSrcExeBegintime;
	}
	
	public Date getEndSrcExeBegintime() {
		return endSrcExeBegintime;
	}

	public void setEndSrcExeBegintime(Date endSrcExeBegintime) {
		this.endSrcExeBegintime = endSrcExeBegintime;
	}
		
	public Date getBeginSrcExeEndtime() {
		return beginSrcExeEndtime;
	}

	public void setBeginSrcExeEndtime(Date beginSrcExeEndtime) {
		this.beginSrcExeEndtime = beginSrcExeEndtime;
	}
	
	public Date getEndSrcExeEndtime() {
		return endSrcExeEndtime;
	}

	public void setEndSrcExeEndtime(Date endSrcExeEndtime) {
		this.endSrcExeEndtime = endSrcExeEndtime;
	}
		
	public Date getBeginTarExeBegintime() {
		return beginTarExeBegintime;
	}

	public void setBeginTarExeBegintime(Date beginTarExeBegintime) {
		this.beginTarExeBegintime = beginTarExeBegintime;
	}
	
	public Date getEndTarExeBegintime() {
		return endTarExeBegintime;
	}

	public void setEndTarExeBegintime(Date endTarExeBegintime) {
		this.endTarExeBegintime = endTarExeBegintime;
	}
		
	public Date getBeginTarExeEndtime() {
		return beginTarExeEndtime;
	}

	public void setBeginTarExeEndtime(Date beginTarExeEndtime) {
		this.beginTarExeEndtime = beginTarExeEndtime;
	}
	
	public Date getEndTarExeEndtime() {
		return endTarExeEndtime;
	}

	public void setEndTarExeEndtime(Date endTarExeEndtime) {
		this.endTarExeEndtime = endTarExeEndtime;
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