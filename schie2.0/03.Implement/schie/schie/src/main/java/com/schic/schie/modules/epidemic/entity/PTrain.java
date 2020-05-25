/**
 * *
 */
package com.schic.schie.modules.epidemic.entity;

import com.jeespring.common.persistence.AbstractBaseEntity;
import com.jeespring.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

/**
 * 病患同火车实体类Entity
 * @author Leo
 * @version 2020-03-17
 */
public class PTrain extends AbstractBaseEntity<PTrain> {
	
	private static final long serialVersionUID = 1L;
	private String beginDate;		// 火车出发日期
	private String trainNum;		// 车次
	private String startSta;		// 始发站
	private String arrivalSta;		// 到达站
	private String arrivalDate;		// 到达日期
	private String carriageNum;		// 车厢
	private String seatNum;		// 座位
	private String name;		// 同火车人姓名
	private String idNumber;		// 同火车人身份证号
	private String bingliIdNumber;		// 同火车人病历号
	private String province;		// 同火车人省份
	private String lxTel;		// 同火车人联系电话
	private String telStation;		// 手机位置
	private String ptype;		// 同火车人病历分类；0，疑似；1，确诊
	private String ofShizhou;		// 所属市周
	private String ofQuxian;		// 所属区县
	private String respArea;		// 责任区
	private String phone;		// 电话号码
	private String habitation;		// 参考居住地
	
	public PTrain() {
		super();
	}

	public PTrain(String id){
		super(id);
	}

	@Length(min=0, max=50, message="火车出发日期长度必须介于 0 和 50 之间")
	@ExcelField(title="火车出发日期", align=2, sort=1)
	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}


	@Length(min=0, max=50, message="车次长度必须介于 0 和 50 之间")
	@ExcelField(title="车次", align=2, sort=2)
	public String getTrainNum() {
		return trainNum;
	}

	public void setTrainNum(String trainNum) {
		this.trainNum = trainNum;
	}


	@Length(min=0, max=50, message="始发站长度必须介于 0 和 50 之间")
	@ExcelField(title="始发站", align=2, sort=3)
	public String getStartSta() {
		return startSta;
	}

	public void setStartSta(String startSta) {
		this.startSta = startSta;
	}


	@Length(min=0, max=50, message="到达站长度必须介于 0 和 50 之间")
	@ExcelField(title="到达站", align=2, sort=4)
	public String getArrivalSta() {
		return arrivalSta;
	}

	public void setArrivalSta(String arrivalSta) {
		this.arrivalSta = arrivalSta;
	}


	@Length(min=0, max=50, message="到达日期长度必须介于 0 和 50 之间")
	@ExcelField(title="到达日期", align=2, sort=5)
	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}


	@Length(min=0, max=10, message="车厢长度必须介于 0 和 10 之间")
	@ExcelField(title="车厢", align=2, sort=6)
	public String getCarriageNum() {
		return carriageNum;
	}

	public void setCarriageNum(String carriageNum) {
		this.carriageNum = carriageNum;
	}


	@Length(min=0, max=50, message="座位长度必须介于 0 和 50 之间")
	@ExcelField(title="座位", align=2, sort=7)
	public String getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}


	@Length(min=0, max=100, message="同火车人姓名长度必须介于 0 和 100 之间")
	@ExcelField(title="同火车人姓名", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Length(min=0, max=50, message="同火车人身份证号长度必须介于 0 和 50 之间")
	@ExcelField(title="同火车人身份证号", align=2, sort=9)
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}


	@Length(min=0, max=50, message="同火车人病历号长度必须介于 0 和 50 之间")
	@ExcelField(title="同火车人病历号", align=2, sort=10)
	public String getBingliIdNumber() {
		return bingliIdNumber;
	}

	public void setBingliIdNumber(String bingliIdNumber) {
		this.bingliIdNumber = bingliIdNumber;
	}


	@Length(min=0, max=50, message="同火车人省份长度必须介于 0 和 50 之间")
	@ExcelField(title="同火车人省份", align=2, sort=11)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}


	@Length(min=0, max=255, message="同火车人联系电话长度必须介于 0 和 255 之间")
	@ExcelField(title="同火车人联系电话", align=2, sort=12)
	public String getLxTel() {
		return lxTel;
	}

	public void setLxTel(String lxTel) {
		this.lxTel = lxTel;
	}


	@Length(min=0, max=100, message="手机位置长度必须介于 0 和 100 之间")
	@ExcelField(title="手机位置", align=2, sort=13)
	public String getTelStation() {
		return telStation;
	}

	public void setTelStation(String telStation) {
		this.telStation = telStation;
	}


	@Length(min=0, max=1, message="同火车人病历分类；0，疑似；1，确诊长度必须介于 0 和 1 之间")
	@ExcelField(title="同火车人病历分类；0，疑似；1，确诊", align=2, sort=14)
	public String getPtype() {
		return ptype;
	}

	public void setPtype(String ptype) {
		this.ptype = ptype;
	}


	@Length(min=0, max=100, message="所属市周长度必须介于 0 和 100 之间")
	@ExcelField(title="所属市周", align=2, sort=15)
	public String getOfShizhou() {
		return ofShizhou;
	}

	public void setOfShizhou(String ofShizhou) {
		this.ofShizhou = ofShizhou;
	}


	@Length(min=0, max=100, message="所属区县长度必须介于 0 和 100 之间")
	@ExcelField(title="所属区县", align=2, sort=16)
	public String getOfQuxian() {
		return ofQuxian;
	}

	public void setOfQuxian(String ofQuxian) {
		this.ofQuxian = ofQuxian;
	}


	@Length(min=0, max=100, message="责任区长度必须介于 0 和 100 之间")
	@ExcelField(title="责任区", align=2, sort=17)
	public String getRespArea() {
		return respArea;
	}

	public void setRespArea(String respArea) {
		this.respArea = respArea;
	}


	@Length(min=0, max=100, message="电话号码长度必须介于 0 和 100 之间")
	@ExcelField(title="电话号码", align=2, sort=18)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	@Length(min=0, max=100, message="参考居住地长度必须介于 0 和 100 之间")
	@ExcelField(title="参考居住地", align=2, sort=19)
	public String getHabitation() {
		return habitation;
	}

	public void setHabitation(String habitation) {
		this.habitation = habitation;
	}

}
