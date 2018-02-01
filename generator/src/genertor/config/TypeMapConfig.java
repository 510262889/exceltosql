package genertor.config;

import org.w3c.dom.Element;

import util.StringUtil;
import util.XmlUtil;

/**
 * Excel类型映射配置
 */
public class TypeMapConfig {
    private String sheet;
    private int row;
    private int typeIndex;
    private int mysqlTypeIndex;
    private int oracleTypeIndex;
    private int nameIndex;
    private int remarkIndex;

    public String getSheet() {
        return sheet;
    }

    public int getRow() {
        return row;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public int getMysqlTypeIndex() {
        return mysqlTypeIndex;
    }

    public int getOracleTypeIndex() {
        return oracleTypeIndex;
    }

    public int getNameIndex() {
        return nameIndex;
    }

    public int getRemarkIndex() {
        return remarkIndex;
    }

    public static TypeMapConfig parse( Element node ) {
        TypeMapConfig config = new TypeMapConfig();

        config.sheet = XmlUtil.getProperty( node, "sheet" );
        // poi从0开始索引，Excel从1开始索引。
        config.row = StringUtil.toInteger( XmlUtil.getProperty( node, "script" ), 2 ) - 1;
        config.typeIndex = StringUtil.toInteger( XmlUtil.getProperty( node, "type" ), 3 ) - 1;
        config.mysqlTypeIndex = StringUtil.toInteger( XmlUtil.getProperty( node, "mysqlType" ), 4 ) - 1;
        config.oracleTypeIndex = StringUtil.toInteger( XmlUtil.getProperty( node, "oracleType" ), 5 ) - 1;
        config.nameIndex = StringUtil.toInteger( XmlUtil.getProperty( node, "name" ), 6 ) - 1;
        config.remarkIndex = StringUtil.toInteger( XmlUtil.getProperty( node, "remark" ), 7 ) - 1;

        return config;
    }
}
