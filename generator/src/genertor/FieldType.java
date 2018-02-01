package genertor;

import static util.ExcelUtil.getCellValString;

import org.apache.poi.ss.usermodel.Row;

import genertor.config.TypeMapConfig;

/**
 * 类型定义
 */
public class FieldType {
    private String type;
    private String mysqlType;
    private String oracleType;
    private String name;
    private String remark;

    /** 生成器类型 */
    public String getType() {
        return type;
    }

    /** MySql类型 */
    public String getMysqlType() {
        return mysqlType;
    }

    /** Oracle类型 */
    public String getOracleType() {
        return oracleType;
    }

    /** 类型名称 */
    public String getName() {
        return name;
    }

    /** 类型备注 */
    public String getRemark() {
        return remark;
    }

    public static FieldType parse( Row row, TypeMapConfig config ) {
        FieldType fieldType = new FieldType();

        fieldType.name = getCellValString( row, config.getNameIndex() );
        fieldType.mysqlType = getCellValString( row, config.getMysqlTypeIndex() );
        fieldType.oracleType = getCellValString( row, config.getOracleTypeIndex() );
        fieldType.type = getCellValString( row, config.getTypeIndex() );
        fieldType.remark = getCellValString( row, config.getRemarkIndex() );

        return fieldType;
    }
}
