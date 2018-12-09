package com.tkbs.chem.press.catalog.tree.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * http://blog.csdn.net/lmj623565791/article/details/40212367
 * @author zhy
 *
 */
public class Node
{

	private int id;
	/**
	 * Root node pId = 0
	 */
	private int pId = 0;

	private String name;

	/**
	 * Current Level
	 */
	private int level;

	/**
	 * Expanded or Not
	 */
	private boolean isExpand = false;

	private int icon;
	private int headicon;

	/**
	 * Children Node
	 */
	private List<Node> children = new ArrayList<Node>();

	/**
	 * Parent Node
	 */
	private Node parent;
	
	private String category_code;

	public Node()
	{
	}

	public Node(int id, int pId, String name,String categoryCode)
	{
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.category_code = categoryCode;
	}

	public int getIcon()
	{
		return icon;
	}

	public void setIcon(int icon)
	{
		this.icon = icon;
	}
	
	public int getHeadIcon()
	{
		return headicon;
	}

	public void setHeadIcon(int headicon)
	{
		this.headicon = headicon;
	}	

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getpId()
	{
		return pId;
	}

	public void setpId(int pId)
	{
		this.pId = pId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCategoryCode()
	{
		return category_code;
	}

	public void setCategoryCode(String code)
	{
		this.category_code = code;
	}	
	public void setLevel(int level)
	{
		this.level = level;
	}

	public boolean isExpand()
	{
		return isExpand;
	}

	public List<Node> getChildren()
	{
		return children;
	}

	public void setChildren(List<Node> children)
	{
		this.children = children;
	}

	public Node getParent()
	{
		return parent;
	}

	public void setParent(Node parent)
	{
		this.parent = parent;
	}

	/**
	 *是否为跟节点
	 * 
	 * @return
	 */
	public boolean isRoot()
	{
		return parent == null;
	}

	/**
	 *判断父节点是否展开 
	 * 
	 * @return
	 */
	public boolean isParentExpand()
	{
		if (parent == null)
			return false;
		return parent.isExpand();
	}

	/**
	 * 是否是叶子界点
	 * 
	 * @return
	 */
	public boolean isLeaf()
	{
		return children.size() == 0;
	}

	/**
	 * 获取level 
	 */
	public int getLevel()
	{
		return parent == null ? 0 : parent.getLevel() + 1;
	}

	/**
	 * 设置展开 
	 * 
	 * @param isExpand
	 */
	public void setExpand(boolean isExpand)
	{
		this.isExpand = isExpand;
		if (!isExpand)
		{

			for (Node node : children)
			{
				node.setExpand(isExpand);
			}
		}
	}

}
