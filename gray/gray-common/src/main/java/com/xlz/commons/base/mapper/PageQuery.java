package com.xlz.commons.base.mapper;

import com.xlz.commons.utils.StringUtils;

/**
 * 
 * @author zhangll
 *
 */
public class PageQuery {

	public PageQuery() {

	}

	public PageQuery(int pageNo, int pageSize) {
		this(pageNo,pageSize,null,"asc");
	}
	
	public PageQuery(int pageNo, int pageSize,String sort,String order) {
		this.pageNo = pageNo;
		this.pageSize= pageSize;
		this.setSort(sort);
		this.order = order;
		
		if (pageNo == 1) {
			this.start = 0;
		} else {
			this.start = (pageNo - 1) * pageSize;
		}

	}

	
	private int pageNo;

	private int pageSize;

	private int start;

	/**
	 * total count
	 */
	private int totalCount = -1;

    private String sort;

    private String order;

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		//分页查询最大行数为5000
		if(pageSize > 5000){
			pageSize = 5000;
		}
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStart() {
		return start;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

    public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		if (StringUtils.isNotBlank(sort)) {
			this.sort = camelToUnderline(sort);
		}
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setStart(int start) {
		this.start = start;
	}


    @Override
	public String toString() {
		return "PageQuery [pageNo=" + getPageNo() + ", pageSize=" + getPageSize() + ", start=" + getStart() + ", total=" + totalCount + "]";
	}
	
    public static final char UNDERLINE='_';  
    
    public static String camelToUnderline(String param){  
        if (param==null||"".equals(param.trim())){  
            return "";  
        }  
        int len=param.length();  
        StringBuilder sb=new StringBuilder(len);  
        for (int i = 0; i < len; i++) {  
            char c=param.charAt(i);  
            if (Character.isUpperCase(c)){  
                sb.append(UNDERLINE);  
                sb.append(Character.toLowerCase(c));  
            }else{  
                sb.append(c);  
            }  
        }  
        return sb.toString();  
    }  
}
