package cn.edu.nju.gqx.util;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtil {
	public static Iterator readXml(String path) {

		long lasting = System.currentTimeMillis();

		File f = new File(path);
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(f);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		Iterator i = root.elementIterator();
//		while (i.hasNext()) {
//			System.out.println("======");
//			Element element = (Element) i.next();
//			System.out.println(element.getName());
//
//			// 枚举属性
//			for (Iterator ia = element.attributeIterator(); ia.hasNext();) {
//				Attribute attribute = (Attribute) ia.next();
//				System.out.println(attribute.getName() + ":"
//						+ attribute.getData());
//			}
//			// 枚举当前节点下所有子节点
//			for (Iterator ieson = element.elementIterator(); ieson.hasNext();) {
//				Element elementSon = (Element) ieson.next();
//				System.out.println(elementSon.getName() + ":"
//						+ elementSon.getText());
//			}
//		}
		
		return i;
	}
}
