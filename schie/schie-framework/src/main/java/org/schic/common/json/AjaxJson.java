/**
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
package org.schic.common.json;

import java.util.LinkedHashMap;

import org.schic.common.mapper.JsonMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @Description: $.ajax后需要接受的JSON 
 * @author Caiwb
 * @date 2019年4月29日 上午11:23:52
 */
public class AjaxJson {

	private boolean success = true;// 是否成功
	private String errorCode = "-1";//错误代码
	private String msg = "操作成功";// 提示信息
	private LinkedHashMap<String, Object> body = new LinkedHashMap<String, Object>();//封装json的map

	public LinkedHashMap<String, Object> getBody() {
		return body;
	}

	public void setBody(LinkedHashMap<String, Object> body) {
		this.body = body;
	}

	public void put(String key, Object value) {//向json中添加属性，在js中访问，请调用data.map.key
		body.put(key, value);
	}

	public void remove(String key) {
		body.remove(key);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {//向json中添加属性，在js中访问，请调用data.msg
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	@JsonIgnore //返回对象时忽略此属性
	public String getJsonStr() {//返回json字符串数组，将访问msg和key的方式统一化，都使用data.key的方式直接访问。

		String json = JsonMapper.getInstance().toJson(this);
		return json;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
