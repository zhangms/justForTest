package test;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZMS
 * @date 2021/11/30
 */
public class XmlParser {

    /**
     * 从xml文本加载
     *
     * @param xmlContent
     * @return
     * @throws Exception
     */
    public static Document loadContent(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try (ByteArrayInputStream in = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8))) {
            return builder.parse(in);
        }
    }

    /**
     * 从xml文件加载
     *
     * @param xmlFile
     * @return
     * @throws Exception
     */
    public static Document loadFile(File xmlFile) throws Exception {
        List<String> list = Files.readAllLines(xmlFile.toPath(), StandardCharsets.UTF_8);
        return loadContent(String.join("\n", list));
    }

    /**
     * 获取某名称的子节点
     *
     * @param element
     * @param nodeName
     * @return
     */
    public static List<Element> getChildrenByName(Element element, String nodeName) {
        List<Element> ret = new ArrayList<>();
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals(nodeName)) {
                ret.add((Element) node);
            }
        }
        return ret;
    }

    /**
     * 获取某节点的字符串内容
     *
     * <pre>
     *     <hello>world</hello>
     *     输出world
     * </pre>
     *
     * @param element
     * @return
     */
    public static String getString(Element element) {
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                return node.getNodeValue().trim();
            }
        }
        return "";
    }

    /**
     * 获取属性值
     *
     * <pre>
     *
     *     <person age="12", weight="50"></person>
     *
     *     getAttribute(person, "age") 输出12
     *
     * </pre>
     *
     * @param element
     * @param attribute
     * @return
     */
    public static String getAttribute(Element element, String attribute) {
        return element.getAttribute(attribute);
    }

    /**
     * 获取所有属性值
     *
     * <pre>
     *
     *     <person age="12", weight="50"></person>
     *
     *     getAttributes(person) 输出 {age=12,weight=50}
     *
     * </pre>
     *
     * @param element
     * @return
     */
    public static Map<String, String> getAttributes(Element element) {
        Map<String, String> ret = new HashMap<>();
        NamedNodeMap attr = element.getAttributes();
        for (int i = 0; i < attr.getLength(); i++) {
            String attribute = attr.item(i).getNodeName();
            ret.put(attribute, element.getAttribute(attribute));
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {

        String content = "" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "\n" +
                "<root>\n" +
                "\n" +
                "    <family name=\"张三李四的Family\">\n" +
                "        <id>123</id>\n" +
                "        <person>\n" +
                "            <name>张三</name>\n" +
                "        </person>\n" +
                "        <person>\n" +
                "            <name>李四</name>\n" +
                "        </person>\n" +
                "    </family>\n" +
                "\n" +
                "</root>\n";

        Document document = XmlParser.loadContent(content);
        Element element = document.getDocumentElement();

        List<Element> families = XmlParser.getChildrenByName(element, "family");
        for (Element family : families) {
            String familyName = XmlParser.getAttribute(family, "name");
            System.out.println(familyName);
            List<Element> persons = XmlParser.getChildrenByName(family, "person");
            for (Element person : persons) {
                String name = XmlParser.getString(XmlParser.getChildrenByName(person, "name").get(0));
                System.out.println(name);
            }
        }
    }


}
