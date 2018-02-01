package genertor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

class Script {

    public final Map<String, Table> tableMap = new LinkedHashMap<>();
    private final String out;
    private final String script;

    public Script(String out, String script) {
        this.out = out;
        this.script = script;
    }

    public String getOutFolder() {
        return out;
    }

    public String getScript() {
        return script;
    }

    public File getScriptFile() {
        return new File( out, script );
    }
}
