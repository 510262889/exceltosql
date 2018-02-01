package genertor.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import util.StringUtil;
import util.XmlUtil;

public class SheetConfig {
    private int row;
    private String name;
    private String script;
    private TableConfig table;
    private FieldConfig field;
    private List<String> skipMergeList = new ArrayList<>();

    public int getRow() {
        return row;
    }

    public String getName() {
        return name;
    }

    public String getScript() {
        return script;
    }

    public TableConfig getTable() {
        return table;
    }

    public FieldConfig getField() {
        return field;
    }

    public List<String> getSkipMergeList() {
        return skipMergeList;
    }

    public static SheetConfig parse( Element node ) {
        SheetConfig config = new SheetConfig();

        // poi从0开始索引，Excel从1开始索引。
        config.row = StringUtil.toInteger( XmlUtil.getProperty( node, "row" ), 2 ) - 1;
        config.name = XmlUtil.getProperty( node, "name" );
        config.script = XmlUtil.getProperty( node, "script" );
        config.table = TableConfig.parse( XmlUtil.getSubNode( node, "table" ) );
        config.field = FieldConfig.parse( XmlUtil.getSubNode( node, "field" ) );
        Element element = XmlUtil.getSubNode( node, "skip-merge" );
        if ( element != null ) {
            String context = XmlUtil.getTrimTextContext( element );
            StringUtil.split2List( context, ",", config.skipMergeList );
        }
        return config;
    }
}
