package genertor;

import static util.ExcelUtil.getCellValString;

import org.apache.poi.ss.usermodel.Row;

import genertor.config.FieldConfig;

public class Field {
    private String name;
    private String type;
    private String precision;
    private String comment;
    private String remark;
    private String defaultValue;
    private boolean primaryKey;
    private boolean autoIncrement;
    private boolean nullable;
    private String unique;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPrecision() {
        return precision;
    }

    public String getComment() {
        return comment;
    }

    public String getRemark() {
        return remark;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public boolean isNullable() {
        return nullable;
    }

    public String getUnique() {
        return unique;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( !(o instanceof Field) ) return false;

        Field field = (Field) o;

        return name.equals( field.name );

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public static Field parse( Row row, FieldConfig fieldConfig ) {
        Field field = new Field();

        field.name = getCellValString( row, fieldConfig.getNameIndex() );
        field.type = getCellValString( row, fieldConfig.getTypeIndex() );
        field.precision = getCellValString( row, fieldConfig.getPrecisionIndex() );
        field.defaultValue = getCellValString( row, fieldConfig.getDefaultValueIndex() );
        field.primaryKey = "Y".equalsIgnoreCase( getCellValString( row, fieldConfig.getPrimiaryKeyIndex() ) );
        field.autoIncrement = "Y".equalsIgnoreCase( getCellValString( row, fieldConfig.getAutoIncrementIndex() ) );
        field.nullable = "Y".equalsIgnoreCase( getCellValString( row, fieldConfig.getNullableIndex() ) ) == false;
        field.unique = getCellValString( row, fieldConfig.getUniqueIndex() );
        field.comment = getCellValString( row, fieldConfig.getCommentIndex() );
        field.remark = getCellValString( row, fieldConfig.getRemarkIndex() );

        return field;
    }
}
