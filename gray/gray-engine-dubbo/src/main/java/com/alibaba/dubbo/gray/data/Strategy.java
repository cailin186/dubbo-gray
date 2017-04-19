package com.alibaba.dubbo.gray.data;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.xlz.commons.config.Level;
import com.xlz.commons.config.Way;

public class Strategy  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;  
	private Integer forwardReverse;  
	private Integer flowTatio;  
	private Pattern regular;  
	private Way way;  
	
	public Strategy() {
		super();
	}

	public Strategy(String name, Integer forwardReverse, Integer flowTatio, Pattern regular, Level level,
			Way way) {
		super();
		this.name = name;
		this.forwardReverse = forwardReverse;
		this.flowTatio = flowTatio;
		this.regular = regular;
		this.way = way;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Pattern getRegular() {
		return regular;
	}

	public void setRegular(Pattern regular) {
		this.regular = regular;
	}

	public Way getWay() {
		return way;
	}

	public void setWay(Way way) {
		this.way = way;
	}

	@Override
	public String toString() {
		return "Strategy [name=" + name + ", forwardReverse=" + forwardReverse + ", flowTatio=" + flowTatio
				+ ", regular=" + regular + ", way=" + way + "]";
	}

}
