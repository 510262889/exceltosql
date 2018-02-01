package genertor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.StringUtil;

public class OracleScriptGenerater extends ScriptGenerater {

    @Override
    protected void generate( StringBuilder builder, Table table, Map<String, FieldType> map ) throws IOException {
        generate( new OracleTableBuilder( builder ), table, map );
    }

    private void generate( TableBuilder builder, Table table, final Map<String, FieldType> typeMap ) throws IOException {
        builder.writeBlockComment( table.getComment() );
        builder.write( "DROP TABLE" ).writeSpace().writeTableName( table.getName() ).writeLine( ";" );
        builder.write( "CREATE TABLE" ).writeSpace().writeTableName( table.getName() ).writeLine( "(" );

        final List<Field> pk = new ArrayList<>();
        final Map<String, List<Field>> unique = new LinkedHashMap<>();
        builder.writeFields( table.getFields().iterator(), new TableBuilder.Joiner<Field>() {
            @Override
            public void build( Field field, TableBuilder builder ) {
                if ( field.isPrimaryKey() ) pk.add( field );
                if ( StringUtil.notBlank( field.getUnique() ) ) {
                    List<Field> list = unique.get( field.getUnique() );
                    if ( list == null ) {
                        list = new ArrayList<>();
                        unique.put( field.getUnique(), list );
                    }
                    list.add( field );
                }

                builder.writeIndent();
                builder.writeFiledName( field.getName() );
                String type = field.getType();
                FieldType mapType = typeMap.get( type );
                if ( mapType != null ) type = mapType.getOracleType();
                builder.writeSpace().writeType( type, field.getPrecision() );
                if ( StringUtil.notBlank( field.getDefaultValue() ) ) builder.writeSpace().writeDefault( field.getDefaultValue() );
                if ( field.isNullable() == false ) builder.writeSpace().writeNullable( field.isNullable() );
                if ( field.isAutoIncrement() ) builder.writeSpace().writeIncrement( field.isAutoIncrement() );
            }
        } );
        if ( pk.isEmpty() == false ) {
            builder.writeLine( ',' ).writeIndent().writePrimary( pk.iterator(), new TableBuilder.Joiner<Field>() {
                @Override
                public void build( Field item, TableBuilder builder ) {
                    builder.write( "\"%s\"", item.getName() );
                }
            } );
        }

        // 唯一索引
        if ( unique.isEmpty() == false ) {
            builder.writeLine( ',' );
            for ( String uniqueName : unique.keySet() ) {
                Iterator<Field> iterator = unique.get( uniqueName ).iterator();
                builder.writeIndent().writeUnique( uniqueName, iterator, new TableBuilder.Joiner<Field>() {
                    @Override
                    public void build( Field item, TableBuilder builder ) {
                        builder.write( "\"%s\"", item.getName() );
                    }
                } );
                if ( iterator.hasNext() ) builder.write( ',' );
            }
        }

        builder.writeLine().write( ")" ).write( ';' );

        // 表注释
        builder.writeLine().writeTableComment( table, table.getComment() );

        // 写字段注释
        for ( Field field : table.getFields() ) {
            String comment = field.getComment();
            if ( StringUtil.notBlank( comment ) ) {
                if ( StringUtil.notBlank( field.getRemark() ) ) {
                    if ( StringUtil.notBlank( comment ) ) comment += ' ';
                    comment += "#" + field.getRemark() + "#";
                }
                builder.writeLine().writeFieldComment( table, field, comment );
            }
        }

        builder.writeLine( 2 );
    }
}
