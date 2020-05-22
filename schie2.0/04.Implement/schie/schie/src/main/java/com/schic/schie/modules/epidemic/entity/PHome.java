/**
 * *
 */
package com.schic.schie.modules.epidemic.entity;

import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

/**
 * 病患同户实体类Entity
 * @author Leo
 * @version 2020-03-17
 */
public class PHome extends AbstractBaseEntity<PHome> {
	
	private static final long serialVersionUID = 1L;
	private String sfId;		// 同户人身份证号
	private String thName;		// 同户人姓名
	private String huhao;		// 户号
	private String hRelation;		// 与患者关系
	private String province;		// 省份
	private String jcDate;		// 接触日期
	private String lxTel;		// 联系电话
	private String telStation;		// 手机位置
	private String ptype;		// 病人分类；0，疑似；1，确诊
	private String ofShizhou;		// 所属市周
	private String ofQuxian;		// 所属区县
	private String respArea;		// 责任区
	private String phone;		// 电话号码
	private String habitation;		// 参考居住地
	
	public PHome() {
		super();
	}

	public PHome(String id){
		super(id);
	}

	@Length(min=0, max=50, message="同户人身份证号长度必须介于 0 和 50 之间")
	@ExcelField(title="同户人身份证号", align=2, sort=1)
	public String getSfId() {
		return sfId;
	}

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}


	@Length(min=0, max=50, message="同户人姓名长度必须介于 0 和 50 之间")
	@ExcelField(title="同户人姓名", align=2, sort=2)
	public String getThName() {
		return thName;
	}

	public void setThName(String thName) {
		this.thName = thName;
	}


	@Length(min=0, max=50, message="户号长度必须介于 0 和 50 之间")
	@ExcelField(title="户号", align=2, sort=3)
	public String getHuhao() {
		return huhao;
	}

	public void setHuhao(String huhao) {
		this.huhao = huhao;
	}


	@Length(min=0, max=50, message="与患者关系长度必须介于 0 和 50 之间")
	@ExcelField(title="与患者关系", align=2, sort=4)
	public String getHRelation() {
		return hRelation;
	}

	public void setHRelation(String hRelation) {
		this.hRelation = hRelation;
	}


	@Length(min=0, max=50, message="省份长度必须介于 0 和 50 之间")
	@ExcelField(title="省份", align=2, sort=5)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}


	@Length(min=0, max=50, message="接触日期长度必须介于 0 和 50 之间")
	@ExcelField(title="接触日期", align=2, sort=6)
	public String getJcDate() {
		return jcDate;
	}

	public void setJcDate(String jcDate) {
		this.jcDate = jcDate;
	}


	@Length(min=0, max=255, message="联系电话长度必须介于 0 和 255 之间")
	@ExcelField(title="联系电话", align=2, sort=7)
	public String getLxTel() {
		return lxTel;
	}

	public void setLxTel(String lxTel) {
		this.lxTel = lxTel;
	}


	@Length(min=0, max=100, message="手机位置长度必须介于 0 和 100 之间")
	@ExcelField(title="手机位置", align=2, sort=8)
	public String getTelStation() {
		return telStation;
	}

	public void setTelStation(String telStation) {
		this.telStation = telStation;
	}


	@Length(min=0, max=1, message="病人分类；0，疑似；1，确诊长度必须介于 0 和 1 之间")
	@ExcelField(title="病人分类；0，疑似；1，确诊", align=2, sort=9)
	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}


	@Length(min=0, max=100, message="所属市周长度必须介于 0 和 100 之间")
	@ExcelField(title="所属市周", align=2, sort=10)
	public String getOfShizhou() {
		return ofShizhou;
	}

	public void setOfShizhou(String ofShizhou) {
		this.ofShizhou = ofShizhou;
	}


	@Length(min=0, max=100, message="所属区县长度必须介于 0 和 100 之间")
	@ExcelField(title="所属区县", align=2, sort=11)
	public String getOfQuxian() {
		return ofQuxian;
	}

	public void setOfQuxian(String ofQuxian) {
		this.ofQuxian = ofQuxian;
	}


	@Length(min=0, max=100, message="责任区长度必须介于 0 和 100 之间")
	@ExcelField(title="责任区", align=2, sort=12)
	public String getRespArea() {
		return respArea;
	}

	public void setRespArea(String respArea) {
		this.respArea = respArea;
	}


	@Length(min=0, max=100, message="电话号码长度必须介于 0 和 100 之间")
	@ExcelField(title="电话号码", align=2, sort=13)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	@Length(min=0, max=100, message="参考居住地长度必须介于 0 和 100 之间")
	@ExcelField(title="参考居住地", align=2, sort=14)
	public String getHabitation() {
		return habitation;
	}

	public void setHabitation(String habitation) {
		this.habitation = habitation;
	}


}
