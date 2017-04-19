package com.xlz.gray.model;

import com.xlz.commons.base.model.BaseDomain;
import com.xlz.commons.config.Level;
import com.xlz.commons.config.Type;
import com.xlz.commons.config.Way;

public class Strategy extends BaseDomain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 策略名称 */
	private String name;  
	/** 1为白名单，2小流量，3为ip段策略，4权重，5业务进行灰度 */
	private Type type;  
	/** 0整，1反，比如在不在白名单中 */
	private Integer forwardReverse;  
	/** 小流量比例 */
	private Integer flowTatio;  
	/** 正则表达式 */
	private String regular;  
	/** 可以按照用户特征等将其分组 */
	private String remark;  
  	
	/** 策略使用级别 */
	private Level level;  
	
	/** 策略方式：1为白名单，2小流量，3为ip段策略，4权重，5业务进行灰度，6正则表达式*/
	private Way way;  
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	public Integer getForwardReverse() {
		return forwardReverse;
	}

	public void setForwardReverse(Integer forwardReverse) {
		this.forwardReverse = forwardReverse;
	}
	public Integer getFlowTatio() {
		return flowTatio;
	}

	public void setFlowTatio(Integer flowTatio) {
		this.flowTatio = flowTatio;
	}
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getRegular() {
		return regular;
	}

	public void setRegular(String regular) {
		this.regular = regular;
	}

	public Way getWay() {
		return way;
	}

	public void setWay(Way way) {
		this.way = way;
	}

}
