package genertor;

import java.util.Iterator;

public class OracleTableBuilder extends TableBuilder {
    public OracleTableBuilder(StringBuilder builder) {
        super( builder );
    }

    @Override
    protected TableBuilder newBuilder( StringBuilder stringBuilder ) {
        return new OracleTableBuilder( stringBuilder );
    }

    @Override
    public TableBuilder writeTableComment( Table table, String comment ) {
        write( "COMMENT ON TABLE %s IS '%s';", table.getName(), table.getComment() );
        return this;
    }

    @Override
    public TableBuilder writeFieldComment( Table table, Field field, String comment ) {
        write( "COMMENT ON COLUMN %s.%s IS '%s';", table.getName(), field.getName(), comment );
        return this;
    }

    @Override
    public TableBuilder writeTableName( String name ) {
        write( "\"%s\"", name );
        return this;
    }

    @Override
    public TableBuilder writeFiledName( String name ) {
        write( "\"%s\"", name );
        return this;
    }

    @Override
    public <T> TableBuilder writeUnique( String uniqueName, Iterator<T> fields, Joiner<T> joiner ) {
        return write( "CONSTRAINT \"" ).write( uniqueName ).write( "\" UNIQUE(" ).join( fields, joiner, "," ).write( ')' );
    }
}
