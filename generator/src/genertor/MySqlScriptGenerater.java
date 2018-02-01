package genertor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.StringUtil;

public class MySqlScriptGenerater extends ScriptGenerater {

    @Override
    protected String beforeWirte( String script ) {
        return script.toLowerCase();
    }

    @Override
    protected void generate( StringBuilder builder, Table table, Map<String, FieldType> map ) throws IOException {
        generate( new TableBuilder( builder ), table, map );
    }

    private void generate( TableBuilder builder, final Table table, final Map<String, FieldType> typeMap ) throws IOException {
        builder.writeBlockComment( table.getComment() );
        builder.write( "DROP TABLE IF EXISTS" ).writeSpace().writeTableName( table.getName() ).writeLine( ';' );
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
                if ( mapType != null ) type = mapType.getMysqlType();
                builder.writeSpace().writeType( type, field.getPrecision() );
                if ( StringUtil.notBlank( field.getDefaultValue() ) ) builder.writeSpace().writeDefault( field.getDefaultValue() );
                if ( field.isNullable() == false ) builder.writeSpace().writeNullable( field.isNullable() );
                if ( field.isAutoIncrement() ) builder.writeSpace().writeIncrement( field.isAutoIncrement() );
                String comment = field.getComment();
                if ( StringUtil.notBlank( comment ) ) {
                    if ( StringUtil.notBlank( field.getRemark() ) ) {
                        if ( StringUtil.notBlank( comment ) ) comment += ' ';
                        comment += "#" + field.getRemark() + "#";
                    }
                    builder.writeSpace().writeFieldComment( table, field, comment );
                }
            }
        } );
        if ( pk.isEmpty() == false ) {
            builder.writeLine( ',' ).writeIndent().writePrimary( pk.iterator(), new TableBuilder.Joiner<Field>() {
                @Override
                public void build( Field item, TableBuilder builder ) {
                    builder.write( "`%s`", item.getName() );
                }
            } );
        }
        if ( unique.isEmpty() == false ) {
            builder.writeLine( ',' );
            Iterator<List<Field>> iterator = unique.values().iterator();
            while ( iterator.hasNext() ) {
                builder.writeIndent().writeUnique( null, iterator.next().iterator(), new TableBuilder.Joiner<Field>() {
                    @Override
                    public void build( Field item, TableBuilder builder ) {
                        builder.write( "`%s`", item.getName() );
                    }
                } );
                if ( iterator.hasNext() ) builder.write( ',' );
            }
        }

        builder.writeLine().write( ")" ).writeSpace().writeTableComment( table, table.getComment() ).writeSpace().write( "engine=InnoDB" ) // 采用InnoDB存储
                .writeSpace().write( "COLLATE='utf8mb4_general_ci'" ) // 设置表的文本编码
                .writeLine( ';' ).writeLine().writeLine();
    }
}
