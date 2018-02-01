package genertor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import util.FileUtil;

public abstract class ScriptGenerater {
    public final void generate( Map<String, FieldType> map, Script script ) throws IOException {
        FileUtil.createFolder( script.getOutFolder() );
        try ( FileWriter writer = new FileWriter( script.getScriptFile() ) ) {
            for ( Table table : script.tableMap.values() ) {
                StringBuilder builder = new StringBuilder();
                generate( builder, table, map );
                writer.write( beforeWirte( builder.toString() ) );
            }
        }
    }

    protected String beforeWirte( String script ) {
        return script;
    }

    protected abstract void generate( StringBuilder builder, Table table, Map<String, FieldType> map ) throws IOException;
}
