package genertor.config;

import org.w3c.dom.Element;

import util.StringUtil;
import util.XmlUtil;

public class TableConfig {
    private int nameIndex;
    private int commentIndex;

    public int getNameIndex() {
        return nameIndex;
    }

    public int getCommentIndex() {
        return commentIndex;
    }

    public static TableConfig parse( Element node ) {
        TableConfig config = new TableConfig();

        config.nameIndex = StringUtil.toInteger( XmlUtil.getProperty( node, "name" ), 3 ) - 1;
        config.commentIndex = StringUtil.toInteger( XmlUtil.getProperty( node, "commentIndex" ), 4 ) - 1;

        return config;
    }
}
