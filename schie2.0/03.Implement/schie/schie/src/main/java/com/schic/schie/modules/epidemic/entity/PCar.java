/**
 * * .
 */
package com.schic.schie.modules.epidemic.entity;

import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

/**
 * 病患同汽车实体类Entity
 * @author Leo
 * @version 2020-03-17
 */
public class PCar extends AbstractBaseEntity<PCar> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// 类型
	private String beginDate;		// 汽车开车时间
	private String name;		// 同车人姓名
	private String idNumber;		// 同车人身份证号
	private String carPosition;		// 车站
	private String province;		// 省份
	private String code;		// code
	private String lxTel;		// 联系电话
	private String ofShizhou;		// 所属市周
	private String ofQuxian;		// 所属区县
	private String respArea;		// 责任区
	private String phone;		// 电话号码
	private String habitation;		// 参考居住地
	
	public PCar() {
		super();
	}

	public PCar(String id){
		super(id);
	}

	@Length(min=0, max=50, message="类型长度必须介于 0 和 50 之间")
	@ExcelField(title="类型", align=2, sort=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	@Length(min=0, max=50, message="汽车开车时间长度必须介于 0 和 50 之间")
	@ExcelField(title="汽车开车时间", align=2, sort=2)
	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}


	@Length(min=0, max=50, message="同车人姓名长度必须介于 0 和 50 之间")
	@ExcelField(title="同车人姓名", align=2, sort=3)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Length(min=0, max=50, message="同车人身份证号长度必须介于 0 和 50 之间")
	@ExcelField(title="同车人身份证号", align=2, sort=4)
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}


	@Length(min=0, max=50, message="车站长度必须介于 0 和 50 之间")
	@ExcelField(title="车站", align=2, sort=5)
	public String getCarPosition() {
		return carPosition;
	}

	public void setCarPosition(String carPosition) {
		this.carPosition = carPosition;
	}


	@Length(min=0, max=50, message="省份长度必须介于 0 和 50 之间")
	@ExcelField(title="省份", align=2, sort=6)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}


	@Length(min=0, max=50, message="code长度必须介于 0 和 50 之间")
	@ExcelField(title="code", align=2, sort=7)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	@Length(min=0, max=255, message="联系电话长度必须介于 0 和 255 之间")
	@ExcelField(title="联系电话", align=2, sort=8)
	public String getLxTel() {
		return lxTel;
	}

	public void setLxTel(String lxTel) {
		this.lxTel = lxTel;
	}


	@Length(min=0, max=100, message="所属市周长度必须介于 0 和 100 之间")
	@ExcelField(title="所属市周", align=2, sort=9)
	public String getOfShizhou() {
		return ofShizhou;
	}

	public void setOfShizhou(String ofShizhou) {
		this.ofShizhou = ofShizhou;
	}


	@Length(min=0, max=100, message="所属区县长度必须介于 0 和 100 之间")
	@ExcelField(title="所属区县", align=2, sort=10)
	public String getOfQuxian() {
		return ofQuxian;
	}

	public void setOfQuxian(String ofQuxian) {
		this.ofQuxian = ofQuxian;
	}


	@Length(min=0, max=100, message="责任区长度必须介于 0 和 100 之间")
	@ExcelField(title="责任区", align=2, sort=11)
	public String getRespArea() {
		return respArea;
	}

	public void setRespArea(String respArea) {
		this.respArea = respArea;
	}


	@Length(min=0, max=100, message="电话号码长度必须介于 0 和 100 之间")
	@ExcelField(title="电话号码", align=2, sort=12)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	@Length(min=0, max=100, message="参考居住地长度必须介于 0 和 100 之间")
	@ExcelField(title="参考居住地", align=2, sort=13)
	public String getHabitation() {
		return habitation;
	}

	public void setHabitation(String habitation) {
		this.habitation = habitation;
	}
}
