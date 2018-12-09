package com.tkbs.chem.press.bean;


import com.tkbs.chem.press.catalog.tree.bean.TreeNodeCode;
import com.tkbs.chem.press.catalog.tree.bean.TreeNodeLabel;
import com.tkbs.chem.press.catalog.tree.bean.TreeNodePid;

/**
 *  目录的业务的benn的类
 * 
 * @author lbs
 *
 */
public class DirectoryBean {
	private int id;
	//子视图的ID
	@TreeNodePid
	private int parentId;
	//父视图的的id
	@TreeNodeCode
	private String  param3;
	//当前的页数
	private String  param1;
	//当前的页数
	@TreeNodeLabel
	private String name;
	//章节内容

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public DirectoryBean(int id, int parentId, String name, String param3, String param1) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.param3 = param3;
		this.param1 = param1;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getParam3() {
		return param3;
	}
	public void setParam3(String param3) {
		this.param3 = param3;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
