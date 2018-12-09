package com.tkbs.chem.press.catalog.bean;


import com.tkbs.chem.press.catalog.tree.bean.TreeNodeCode;
import com.tkbs.chem.press.catalog.tree.bean.TreeNodeId;
import com.tkbs.chem.press.catalog.tree.bean.TreeNodeLabel;
import com.tkbs.chem.press.catalog.tree.bean.TreeNodePid;

public class FileBean
{
	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	private long length;
	private String desc;
	@TreeNodeCode
	private String code;

	public FileBean(int _id, int parentId, String name,String code)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
		this.code = code;
	}
	public FileBean(int _id, int parentId, String name)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
		
	}

}
