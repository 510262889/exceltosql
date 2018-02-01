package main.java.datagenerator;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import util.DateUtil;
import util.ExcelUtil;
import util.StringUtil;

class InterfaceDataGenerator implements Closeable {
    private final Workbook workbook;

    private InterfaceDataGenerator(String excelFile) {
        excelFile = Normalizer.normalize( StringUtil.empty( excelFile, "" ), Normalizer.Form.NFKC );
        try {
            this.workbook = WorkbookFactory.create( new File( excelFile ), null, true );
        } catch ( IOException | InvalidFormatException e ) {
            throw new RuntimeException( e );
        }
    }

    private String getCellVal( Row row, int cell ) {
        return StringUtil.trim( ExcelUtil.getCellValString( row, cell ) );
    }

    private String getCellDate( Row row, int cell ) {
        Date cellValDate = ExcelUtil.getCellValDate( row, cell );
        if ( cellValDate == null ) return "";
        return DateUtil.dateString( cellValDate );
    }

    private void generate( String sheetName, String output ) throws IOException {
        Sheet sheet = workbook.getSheet( sheetName );
        try ( Writer writer = new FileWriter( output ) ) {
            writer.append( "DELETE FROM WM_SYS_INTE;" ).append( System.lineSeparator() ).append( System.lineSeparator() );

            Map<String, List<Row>> temps = new LinkedHashMap<>();

            for ( int i = 1; i <= sheet.getLastRowNum(); i++ ) {
                Row row = sheet.getRow( i );

                String system = getCellVal( row, 2 );
                List<Row> rows = temps.get( system );
                if ( rows == null ) {
                    rows = new ArrayList<>();
                    temps.put( system, rows );
                }
                rows.add( row );
            }

            for ( List<Row> rows : temps.values() ) {
                Row firstRow = rows.get( 0 );

                writer.append( "-- 系统代码: " ).append( getCellVal( firstRow, 2 ) ).append( System.lineSeparator() ).append( "-- DELETE FROM WM_SYS_INTE WHERE R_SYS = '" )
                        .append( getCellVal( firstRow, 1 ) ).append( "';" ).append( System.lineSeparator() );
                for ( Row row : rows ) {
                    writer.append(
                            "INSERT INTO WM_SYS_INTE(SID, R_SYS, S_SYS_NAME, C_CODE, C_NAME, C_DESC, P_PERM, S_PERM, C_URL, C_ACCOUNT, C_PASSWORD, C_PROTOCOL, T_ONLINE, C_PRINCIPAL, C_PRINCIPAL_TEL, C_REMARK) VALUES (" )
                            .append( "'" ).append( getCellVal( row, 3 ) ).append( "', " ) // 接口编号
                            .append( "'" ).append( getCellVal( row, 1 ) ).append( "', " ) // 系统SID（自动）
                            // .append( "'" ).append( getCellVal( row, 2 )
                            // ).append( "', " ) // 系统编号
                            // .append( "'" ).append( getCellVal( row, 4 )
                            // ).append( "', " ) // 临时系统编号
                            .append( "'" ).append( getCellVal( row, 5 ) ).append( "', " ) // 系统名称
                            .append( "'" ).append( getCellVal( row, 6 ) ).append( "', " ) // 接口编码（接口英文名）
                            .append( "'" ).append( getCellVal( row, 7 ) ).append( "', " ) // 接口名称
                            .append( "'" ).append( getCellVal( row, 8 ) ).append( "', " ) // 接口描述
                            .append( "'" ).append( getPermCode( getCellVal( row, 9 ) ) ).append( "', " ) // 接口访问权限(读、写)
                            .append( "'" ).append( getPermVal( getCellVal( row, 9 ) ) ).append( "', " ) // 接口访问权限(读、写)
                            .append( "'" ).append( getCellVal( row, 10 ) ).append( "', " ) // 接口访问URL
                            .append( "'" ).append( getCellVal( row, 11 ) ).append( "', " ) // 接口账号
                            .append( "'" ).append( getCellVal( row, 12 ) ).append( "', " ) // 接口密码
                            // .append( "'" ).append( getCellVal( row, 13 )
                            // ).append( "', " ) // 其它访问参数
                            .append( "'" ).append( getProto( getCellVal( row, 10 ), getCellVal( row, 14 ) ) ).append( "', " ) // 协议类型
                            // .append( "'" ).append( getCellVal( row, 14 )
                            // ).append( "', " ) // 协议类型
                            .append( "TO_DATE('" ).append( getCellDate( row, 15 ) ).append( "', 'YYYY-MM-DD'), " ) // 上线时间
                            .append( "'" ).append( getCellVal( row, 16 ) ).append( "', " ) // 接口负责人
                            .append( "'" ).append( getCellVal( row, 17 ) ).append( "', " ) // 负责人电话
                            // .append( "'" ).append( getCellVal( row, 18 )
                            // ).append( "', " ) // 企业门户ID
                            .append( "'" ).append( getCellVal( row, 19 ) ).append( "'" ) // 备注
                            .append( ");" ).append( System.lineSeparator() );
                }
                writer.append( System.lineSeparator() ).append( System.lineSeparator() );
            }
            writer.append( "UPDATE WM_SYS_INTE SET P_RUN = '10', S_RUN = '运行';" ).append( System.lineSeparator() );
            writer.append( "UPDATE WM_SYS_INTE SET C_CODE = SID WHERE C_CODE IS NULL;" ).append( System.lineSeparator() );
            writer.append( "UPDATE WM_SYS_INTE SET B_TEST = '0' WHERE C_URL IS NULL OR C_PROTOCOL NOT IN ('HTTP', 'HTTPS', 'WebService');" ).append( System.lineSeparator() );
            writer.append( "COMMIT;" ).append( System.lineSeparator() );
        }
    }

    private static final Map<String, String> perms = new HashMap<>();

    static {
        perms.put( "10", "不可读写" );
        perms.put( "11", "读" );
        perms.put( "12", "写" );
        perms.put( "13", "读写" );
    }

    private static String getPermCode( String code ) {
        String value = perms.get( code );
        if ( value == null ) return "11";
        return code;
    }

    private static String getPermVal( String code ) {
        String value = perms.get( code );
        if ( value == null ) return "读";
        return value;
    }

    private static String getProto( String url, String proto ) {
        if ( url == null ) return "";
        url = url.toLowerCase();
        if ( url.startsWith( "http" ) ) return url.endsWith( "?wsdl" ) ? "WebService" : "HTTP";
        if ( url.startsWith( "https" ) ) return url.endsWith( "?wsdl" ) ? "WebService" : "HTTPS";
        return proto;
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }

    public static void generate( String excelFile, String excelSheetName, String output ) throws IOException {
        excelFile = Normalizer.normalize( StringUtil.empty( excelFile, "" ), Normalizer.Form.NFKC );

        try ( InterfaceDataGenerator generator = new InterfaceDataGenerator( excelFile ) ) {
            generator.generate( excelSheetName, output );
        }
    }
}
