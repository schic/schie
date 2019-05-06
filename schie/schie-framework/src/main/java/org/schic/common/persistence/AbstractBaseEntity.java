/* 
 * 创建日期 2019年4月29日
 *
 * 四川健康久远科技有限公司
 * 电话： 
 * 传真： 
 * 邮编： 
 * 地址：成都市武侯区
 * 版权所有
 */
package org.schic.common.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.schic.common.utils.IdGen;
import org.schic.modules.sys.entity.User;
import org.schic.modules.sys.utils.UserUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @Description: 数据Entity类
 * @author Caiwb
 * @date 2019年4月29日 下午2:32:58 
 * @param <T>
 */
public abstract class AbstractBaseEntity<T> extends AbstractEntity<T> {

	private static final long serialVersionUID = 1L;

	protected String remarks; // 备注
	protected User createBy; // 创建者
	protected Date createDate; // 创建日期
	protected User updateBy; // 更新者
	protected Date updateDate; // 更新日期
	protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	protected HashMap<String, Object> extendMap;//HashMap对象的拓展属性
	protected JSONObject jsonObject;//JSONObject对象的拓展属性
	protected Integer totalCount;
	protected String totalDate;
	protected String totalType;

	public void setExtendMap(String item, Object object) {
		if (extendMap == null) {
			extendMap = new HashMap<>();
		}
		extendMap.put(item, object);
	}

	public Map<String, Object> getExtendMap() {
		return extendMap;
	}

	public JSONObject setExtendObject(String item, Object object) {
		if (jsonObject == null) {
			jsonObject = JSON.parseObject(JSON.toJSONString(this));
		}
		jsonObject.put(item, object);
		return jsonObject;
	}

	public AbstractBaseEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}

	public AbstractBaseEntity(String id) {
		super(id);
	}

	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert() {
		// 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
		if (!this.isNewRecord) {
			setId(IdGen.uuid());
		}
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}

	/**
	 * 更新之前执行方法，需要手动调用
	 */
	@Override
	public void preUpdate() {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())) {
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}

	@Length(min = 0, max = 255)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@JsonIgnore
	@JSONField(serialize = false)
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	@JSONField(serialize = false)
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@JsonIgnore
	@JSONField(serialize = false)
	@Length(min = 1, max = 1)
	public String getDelFlag() {
		if (delFlag == null) {
			delFlag = "0";
		}
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getTotalDate() {
		return totalDate;
	}

	public void setTotalDate(String totalDate) {
		this.totalDate = totalDate;
	}

	public String getTotalType() {
		return totalType;
	}

	public void setTotalType(String totalType) {
		this.totalType = totalType;
	}
}
