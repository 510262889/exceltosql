package util;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.text.Normalizer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import util.resource.Resource;

/**
 * XML工具类
 */
public class XmlUtil {

    /**
     * 获取指定输入流的XML的根节点
     */
    public static Element getDocumentElement( InputStream stream ) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            return factory.newDocumentBuilder().parse( stream ).getDocumentElement();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    public static Element getDocumentElement( Resource resource ) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            return factory.newDocumentBuilder().parse( resource.getInputStream() ).getDocumentElement();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    public static Element getDocumentElement( String filepath ) {
        filepath = Normalizer.normalize( StringUtil.empty( filepath, "" ), Normalizer.Form.NFKC );
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            return factory.newDocumentBuilder().parse( filepath ).getDocumentElement();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    public static Element getDocumentElement( File file ) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            return factory.newDocumentBuilder().parse( file ).getDocumentElement();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * 获得XML节点的去除头尾空白字符值
     *
     * @param item
     *            XML节点
     * @param tag
     *            属性名
     *
     * @return 属性值，并去除头尾的空白字符
     */
    public static String getTrimAttribute( Element item, String tag ) {
        String v = item.getAttribute( tag );
        if ( v == null || v.length() == 0 ) return v;
        else return v.trim();
    }

    /**
     * 获得XML节点的去除头尾空白字符值，如果为空则取默认值
     */
    public static String getAttribute( Element item, String tag, String defaultValue ) {
        String v = item.getAttribute( tag );
        if ( v == null || v.length() == 0 ) return defaultValue;
        else return v.trim();
    }

    /**
     * 获得XML节点的去除头尾空白字符值，如果为空则取默认值
     */
    public static boolean getAttribute( Element item, String tag, boolean defaultValue ) {
        String v = item.getAttribute( tag );
        if ( v == null || v.length() == 0 ) return defaultValue;
        else return StringUtil.toBoolean( v.trim(), defaultValue );
    }

    /**
     * 解析文件的指定节点
     */
    public static INodeParser selectNodes( String fileName, String express, INodeParser actor ) {
        fileName = Normalizer.normalize( StringUtil.empty( fileName, "" ), Normalizer.Form.NFKC );
        File file = new File( fileName );

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        Element root;
        try {
            root = factory.newDocumentBuilder().parse( file ).getDocumentElement();
        } catch ( Throwable e ) {
            throw new RuntimeException( "XML文件[" + fileName + "]读写错误", e );
        }

        return selectNodes( root, express, actor );
    }

    /**
     * 利用XSL语法选择节点来处理
     *
     * @param e
     *            要处理的XML节点
     * @param express
     *            查找的XSL表达式
     * @param actor
     *            行处理函数
     */
    public static INodeParser selectNodes( Element e, String express, INodeParser actor ) {
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        NodeList nodes;
        try {
            nodes = (NodeList) xpath.evaluate( express, e, XPathConstants.NODESET );
        } catch ( XPathExpressionException e1 ) {
            throw new IllegalArgumentException( "没有找到对应规则[" + express + "]的XML阶段", e1 );
        }

        for ( int i = 0; i < nodes.getLength(); i++ ) {
            Node node = nodes.item( i );
            if ( node.getNodeType() != Node.ELEMENT_NODE ) continue;
            try {
                actor.parse( (Element) node );
            } catch ( Throwable e1 ) {
                throw new RuntimeException( e1 );
            }

        }
        return actor;
    }

    /**
     * 遍历子节点
     */
    public static INodeParser selectChildrenNodes( Element node, INodeParser actor ) {
        NodeList nodes = node.getChildNodes();
        for ( int i = 0; i < nodes.getLength(); i++ ) {
            Node n = nodes.item( i );
            if ( n.getNodeType() == Node.ELEMENT_NODE ) {
                try {
                    actor.parse( (Element) n );
                } catch ( Throwable e1 ) {
                    throw new RuntimeException( e1 );
                }
            }
        }
        return actor;
    }

    /**
     * 获得指定的一个子节点，如果有多个的话，则取第一个，其他忽略
     *
     * @param node
     *            父节点
     * @param subtag
     *            子节点名称
     *
     * @return 第一个查找到的节点
     */
    public static Element getSubNode( Element node, String subtag ) {
        NodeList nodes = node.getChildNodes();
        for ( int i = 0; i < nodes.getLength(); i++ ) {
            Node n = nodes.item( i );
            if ( n.getNodeType() == Node.ELEMENT_NODE && subtag.equals( n.getNodeName() ) ) return (Element) n;
        }
        return null;
    }

    /**
     * 获得文本内容，并删除头头尾尾的空白字符
     */
    public static String getTrimTextContext( Element n ) {
        String text = n.getTextContent();
        if ( text == null || text.length() == 0 ) return text;
        return text.replaceAll( "^\\s", "" ).replaceAll( "\\s$", "" );
    }

    /**
     * 获得节点属性，如果有属性则找属性，如果没有则找子节点
     */
    public static String getProperty( Element node, String tagname ) {
        String v = getTrimAttribute( node, tagname );
        if ( v == null || v.length() == 0 ) {
            Element sub = XmlUtil.getSubNode( node, tagname );
            if ( sub != null ) v = getTrimTextContext( sub );
        }
        return v;
    }

    /**
     * 获得节点属性，如果有属性则找属性，如果没有则找子节点
     */
    public static String getProperty( Element node, String tagname, String defval ) {
        String v = getProperty( node, tagname );
        if ( v == null ) return defval;
        return v;
    }

    /**
     * 处理所有的子节点
     */
    public static void getSubNodes( Element node, INodeParser np ) {
        NodeList nodes = node.getChildNodes();
        for ( int i = 0; i < nodes.getLength(); i++ ) {
            Node n = nodes.item( i );
            if ( n.getNodeType() != Node.ELEMENT_NODE ) continue;
            np.parse( (Element) n );
        }
    }

    /**
     * 查找子节点中第一个符合条件的节点
     */
    public static Element findSonElement( Element e, String tagName, IAttributeEquals attr ) {
        NodeList nodes = e.getChildNodes();
        for ( int i = 0; i < nodes.getLength(); i++ ) {
            Node node = nodes.item( i );
            if ( node.getNodeType() != Node.ELEMENT_NODE ) continue;
            if ( node.getNodeName().equals( tagName ) ) {
                if ( attr == null || (attr != null && attr.isEqual( (Element) node )) ) return (Element) node;
            }
        }
        return null;
    }

    /**
     * 从字符串获得XML的DOCUMENT对象
     */
    public static Document loadString( String xmldata ) throws RuntimeException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            return factory.newDocumentBuilder().parse( new InputSource( new StringReader( xmldata ) ) );
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * 新建一个XML节点，指定节点名和属性
     *
     * @param attrs
     *            必须是“属性名”，“值”,...这样的可变长参数
     */
    public static Element createElement( Document doc, String tagName, String... attrs ) {
        Element e = doc.createElement( tagName );

        if ( attrs != null ) {
            for ( int i = 0; i < (attrs.length / 2); i++ ) {
                e.setAttribute( attrs[i * 2], StringUtil.encodeHTML( attrs[i * 2 + 1] ) );
            }
        }
        return e;
    }

    /**
     * 每个节点的处理接口定义
     */
    public interface INodeParser {
        void parse( Element node );
    }

    /**
     * 属性比较的接口，用户查找节点
     *
     * @author chis
     */
    public interface IAttributeEquals {
        boolean isEqual( Element item );
    }
}
