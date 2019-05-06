package org.schic.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import org.schic.common.persistence.TreeEntity;

/**
 * 
 * @Description: 区域Entity
 * @author Caiwb
 * @date 2019年5月6日 上午10:39:48
 */
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
	private String code; // 区域编码
	private String type; // 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）

	public Area() {
		super();
		this.sort = 30;
	}

	public Area(String id) {
		super(id);
	}

	//	@JsonBackReference
	//	@NotNull
	@Override
	public Area getParent() {
		return parent;
	}

	@Override
	public void setParent(Area parent) {
		this.parent = parent;
	}

	@Length(min = 1, max = 1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min = 0, max = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return name;
	}
}