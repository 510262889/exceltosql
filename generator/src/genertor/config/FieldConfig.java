package genertor.config;

import org.w3c.dom.Element;

import util.ExcelUtil;
import util.XmlUtil;

/**
 * 字段定义
 */
public class FieldConfig {
    private int nameIndex;
    private int commentIndex;
    private int remarkIndex;
    private int typeIndex;
    private int precisionIndex;
    private int defaultValueIndex;
    private int primiaryKeyIndex;
    private int autoIncrementIndex;
    private int nullableIndex;
    private int uniqueIndex;

    public int getNameIndex() {
        return nameIndex;
    }

    public int getCommentIndex() {
        return commentIndex;
    }

    public int getRemarkIndex() {
        return remarkIndex;
    }

    public int getTypeIndex() {
        return typeIndex;
    }

    public int getPrecisionIndex() {
        return precisionIndex;
    }

    public int getDefaultValueIndex() {
        return defaultValueIndex;
    }

    public int getPrimiaryKeyIndex() {
        return primiaryKeyIndex;
    }

    public int getAutoIncrementIndex() {
        return autoIncrementIndex;
    }

    public int getNullableIndex() {
        return nullableIndex;
    }

    public int getUniqueIndex() {
        return uniqueIndex;
    }

    public static FieldConfig parse( Element node ) {
        FieldConfig config = new FieldConfig();

        // poi从0开始索引，Excel从1开始索引。
        config.nameIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "name" ) ) - 1;
        config.commentIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "comment" ) ) - 1;
        config.remarkIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "remark" ) ) - 1;
        config.typeIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "type" ) ) - 1;
        config.precisionIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "precision" ) ) - 1;
        config.defaultValueIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "default" ) ) - 1;
        config.primiaryKeyIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "primiaryKey" ) ) - 1;
        config.autoIncrementIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "autoIncrement" ) ) - 1;
        config.nullableIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "nullable" ) ) - 1;
        config.uniqueIndex = ExcelUtil.columnToIndex( XmlUtil.getProperty( node, "unique" ) ) - 1;

        return config;
    }
}
