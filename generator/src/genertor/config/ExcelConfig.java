package genertor.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import util.XmlUtil;

/** Excel∂®“Â */
public class ExcelConfig {
    private String src;
    private String out;
    private TypeMapConfig typeMapConfig;
    private DefaultFieldConfig defaultFieldConfig;
    private List<SheetConfig> sheetList = new ArrayList<>();

    public String getSrc() {
        return src;
    }

    public String getOut() {
        return out;
    }

    public TypeMapConfig getTypeMapConfig() {
        return typeMapConfig;
    }

    public DefaultFieldConfig getDefaultFieldConfig() {
        return defaultFieldConfig;
    }

    public Iterable<SheetConfig> getSheetList() {
        return sheetList;
    }

    public static ExcelConfig parse( Element root ) {
        final ExcelConfig config = new ExcelConfig();

        config.src = XmlUtil.getProperty( root, "src" );
        config.out = XmlUtil.getProperty( root, "out" );
        config.typeMapConfig = TypeMapConfig.parse( XmlUtil.getSubNode( root, "map" ) );
        config.defaultFieldConfig = DefaultFieldConfig.parse( XmlUtil.getSubNode( root, "defaultFields" ) );
        XmlUtil.selectNodes( root, "sheet", new XmlUtil.INodeParser() {
            @Override
            public void parse( Element node ) {
                config.sheetList.add( SheetConfig.parse( node ) );
            }
        } );

        return config;
    }
}
