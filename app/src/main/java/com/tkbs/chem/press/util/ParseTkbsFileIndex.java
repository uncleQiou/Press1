package com.tkbs.chem.press.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;



public class ParseTkbsFileIndex {
	/**
	 * 快速查询 xpath ，使用xpath表达式进行查询 / 表示 从根开始 // 表示任意位置开始
	 */
	public static Map<String, String> textPath(InputStream inputStream,int number) {
		// 1 获得document
		Document document = getDocument(inputStream);
		// * 通过表达式查询单个
		Node node = document.selectSingleNode("//Page[@Num="+number+"]");
		Element bookElement = (Element) node;
		// Num属性
		Map<String,String> map = new HashMap<String,String>();
		String num = bookElement.attributeValue("Num");
		String pngPos = bookElement.attributeValue("PngPos");
		String pngLen = bookElement.attributeValue("PngLen");
		String pngEncodeLen = bookElement.attributeValue("PngEncodeLen");
		String htmlPos = bookElement.attributeValue("HtmlPos");
		String htmlLen = bookElement.attributeValue("HtmlLen");
		String htmlEncodeLen = bookElement.attributeValue("HtmlEncodeLen");
		
		map.put("num", num);
		map.put("pngPos", pngPos);
		map.put("pngLen", pngLen);
		map.put("pngEncodeLen", pngEncodeLen);
		map.put("htmlPos", htmlPos);
		map.put("htmlLen", htmlLen);
		map.put("htmlEncodeLen", htmlEncodeLen);
		
		return map;
	}
	
	/**
	 * 获取pdf的总的页数：
	 */
	public static int getPageNumbers(InputStream inputStream){
		Document document = getDocument(inputStream);
		List node = document.selectNodes("//Page");
		int pages= node.size();
		return pages;
	}
	
	/**
	 * 遍历所有
	 * @throws DocumentException 
	 */
	public static Map<String,String > findResourceAuthInfo(InputStream inputStream) throws DocumentException {
		//1 获得document
		Document document = getDocument(inputStream);
		//2 必须获得根元素
		Element rootElelemnt = document.getRootElement();
		//3 获得所有book元素
		List<Element> allBookElement = rootElelemnt.elements();
		Map<String,String> map = new HashMap<String,String>();
		//4 遍历
		for(Element bookElement : allBookElement){
			map.put(bookElement.getName(), bookElement.getText());
		}
		return map;
	}
	/**
	 * 遍历所有
	 * 
	 */
	private static void findAll(InputStream inputStream) {
		
//		Document document = getDocument(indexPath);
		Document document = getDocument(inputStream);
		
		// 2 必须获得根元素
		Element rootElelemnt = document.getRootElement();
		// 3 获得所有book元素
		List<Element> allBookElement = rootElelemnt.elements();
		for (Element bookElement : allBookElement) {
			// id属性
			String num = bookElement.attributeValue("Num");
			String pngPos = bookElement.attributeValue("PngPos");
			String pngLen = bookElement.attributeValue("PngLen");
			String pngEncodeLen = bookElement.attributeValue("PngEncodeLen");
			String htmlPos = bookElement.attributeValue("HtmlPos");
			String htmlLen = bookElement.attributeValue("HtmlLen");
			String htmlEncodeLen = bookElement.attributeValue("HtmlEncodeLen");
		}
	}

	/**
	 * 获得dom4j document 对象
	 * 
	 * @throws DocumentException
	 */
	private static Document getDocument(InputStream inputStream) {
		// 1 创建核心类（解析器）
		SAXReader saxReader = null;
		// 2 获得document
		Document document = null;
		try {
			saxReader = new SAXReader();
			document = saxReader.read(inputStream);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	public static void main(String[] args) throws Exception {
		String indexPath = "E:\\Test\\index.xml";
		//解密文件返回文件流信息:
		InputStream inputStream = DESUtil.decryptFile(indexPath, "1234567890", false);
		ParseTkbsFileIndex tkbsIndex = new ParseTkbsFileIndex();
		Map<String, String> index = tkbsIndex.textPath(inputStream, 7); //xpath表达式进行查询
		System.out.println(index.get("num") +"  "+index.get("pngPos") +"  "+index.get("pngLen") +"  "+index.get("htmlLen"));
		System.exit(0);
	}
}
