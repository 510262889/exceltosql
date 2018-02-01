package genertor.config;

import org.w3c.dom.Element;

import util.StringUtil;
import util.XmlUtil;

/**
 * Ĭ���ֶ�����
 */
public class DefaultFieldConfig {
    private String sheet;
    private int row;
    private FieldConfig fieldConfig;

    public String getSheet() {
        return sheet;
    }

    public int getRow() {
        return row;
    }

    public FieldConfig getFieldConfig() {
        return fieldConfig;
    }

    public static DefaultFieldConfig parse( Element node ) {
        DefaultFieldConfig config = new DefaultFieldConfig();

        config.sheet = XmlUtil.getProperty( node, "sheet" );
        // poi��0��ʼ������Excel��1��ʼ������
        config.row = StringUtil.toInteger( XmlUtil.getProperty( node, "row" ), 2 ) - 1;
        config.fieldConfig = FieldConfig.parse( node );
        return config;
    }
}
