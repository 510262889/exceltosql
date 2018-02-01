package genertor;

import java.util.Iterator;

import util.StringUtil;

class TableBuilder {
    protected final StringBuilder builder;

    public TableBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    protected TableBuilder newBuilder( StringBuilder stringBuilder ) {
        return new TableBuilder( stringBuilder );
    }

    public TableBuilder write( String str ) {
        builder.append( str );
        return this;
    }

    public TableBuilder write( String str, Object... args ) {
        write( String.format( str, args ) );
        return this;
    }

    public TableBuilder write( char c ) {
        builder.append( c );
        return this;
    }

    public TableBuilder writeLine() {
        builder.append( Const.LINE_SEPARATOR );
        return this;
    }

    public TableBuilder writeLine( int line ) {
        for ( int i = 0; i < line; i++ )
            builder.append( Const.LINE_SEPARATOR );
        return this;
    }

    public TableBuilder writeLine( char c ) {
        builder.append( c ).append( Const.LINE_SEPARATOR );
        return this;
    }

    public TableBuilder writeLine( String str ) {
        builder.append( str ).append( Const.LINE_SEPARATOR );
        return this;
    }

    public TableBuilder writeLine( String str, Object... args ) {
        writeLine( String.format( str, args ) );
        return this;
    }

    public TableBuilder writeBlockComment( String comment ) {
        writeLine( "/* %s */", comment );
        return this;
    }

    public TableBuilder writeLineComment( String comment ) {
        writeLine( "-- %s", comment );
        return this;
    }

    public TableBuilder writeDefault( String defaultValue ) {
        if ( StringUtil.notBlank( defaultValue ) ) write( "DEFAULT %s", defaultValue );
        return this;
    }

    public TableBuilder writeTableName( String name ) {
        write( "`%s`", name );
        return this;
    }

    public TableBuilder writeTableComment( Table table, String comment ) {
        write( "COMMENT '%s'", comment );
        return this;
    }

    public TableBuilder writeIndent() {
        return writeIndent( 1 );
    }

    public TableBuilder writeIndent( int size ) {
        for ( int i = 0; i < size * Const.INDENT_SIZE; i++ )
            builder.append( ' ' );
        return this;
    }

    public TableBuilder writeFiledName( String name ) {
        write( "`%s`", name );
        return this;
    }

    public TableBuilder writeSpace() {
        return writeSpace( 1 );
    }

    public TableBuilder writeSpace( int size ) {
        for ( int i = 0; i < size; i++ )
            builder.append( ' ' );
        return this;
    }

    public TableBuilder writeType( String type, String precision ) {
        builder.append( type );
        if ( StringUtil.notBlank( precision ) ) builder.append( '(' ).append( precision ).append( ')' );
        return this;
    }

    public TableBuilder writeNullable( boolean nullable ) {
        if ( nullable == false ) builder.append( "NOT NULL" );
        return this;
    }

    public TableBuilder writeIncrement( boolean increment ) {
        if ( increment == true ) builder.append( "AUTO_INCREMENT" );
        return this;
    }

    public TableBuilder writeFieldComment( Table table, Field field, String comment ) {
        write( "COMMENT '%s'", comment );
        return this;
    }

    protected final <T> TableBuilder join( Iterator<T> items, Joiner<T> joiner, String separator ) {
        while ( items.hasNext() ) {
            TableBuilder tempBuilder = newBuilder( new StringBuilder() );
            joiner.build( items.next(), tempBuilder );
            write( tempBuilder );
            if ( items.hasNext() ) write( separator );
        }
        return this;
    }

    public <T> TableBuilder writeFields( Iterator<T> iterator, final Joiner<T> joiner ) {
        return join( iterator, new Joiner<T>() {
            @Override
            public void build( T item, TableBuilder builder ) {
                joiner.build( item, builder );
            }
        }, "," + Const.LINE_SEPARATOR );
    }

    public <T> TableBuilder writePrimary( Iterator<T> keys, Joiner<T> joiner ) {
        return write( "PRIMARY KEY(" ).join( keys, joiner, "," ).write( ')' );
    }

    public <T> TableBuilder writeUnique( String uniqueName, Iterator<T> fields, Joiner<T> joiner ) {
        return write( "UNIQUE INDEX(" ).join( fields, joiner, "," ).write( ')' );
    }

    private void write( TableBuilder builder ) {
        this.builder.append( builder.builder );
    }

    public interface Joiner<T> {
        void build( T item, TableBuilder builder );
    }
}
