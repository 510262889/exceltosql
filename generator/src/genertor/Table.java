package genertor;

import static util.ExcelUtil.getCellValString;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;

import genertor.config.TableConfig;

public class Table {
    private String name;
    private String comment;
    private final Set<Field> fields = new LinkedHashSet<>();

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public Set<Field> getFields() {
        return fields;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof Table) ) return false;

        Table table = (Table) o;

        return name.equals( table.name );

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static Table parse( Row row, TableConfig tableConfig ) {
        Table table = new Table();
        table.name = getCellValString( row, tableConfig.getNameIndex() );
        table.comment = getCellValString( row, tableConfig.getCommentIndex() );

        return table;
    }
}
