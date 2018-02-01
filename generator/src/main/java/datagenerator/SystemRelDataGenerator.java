package main.java.datagenerator;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
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
import util.UUIDUtil;

class SystemRelDataGenerator implements Closeable {
    private final Workbook workbook;

    private SystemRelDataGenerator(String excelFile) {
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
            writer.append( "DELETE FROM WM_SYS_REQ;" ).append( System.lineSeparator() ).append( System.lineSeparator() );

            Map<String, List<Row>> temps = new LinkedHashMap<>();

            for ( int i = 1; i <= sheet.getLastRowNum(); i++ ) {
                Row row = sheet.getRow( i );

                String system = getCellVal( row, 1 );
                List<Row> rows = temps.get( system );
                if ( rows == null ) {
                    rows = new ArrayList<>();
                    temps.put( system, rows );
                }
                rows.add( row );
            }

            for ( List<Row> rows : temps.values() ) {
                Row firstRow = rows.get( 0 );

                writer.append( "-- 系统代码: " ).append( getCellVal( firstRow, 1 ) ).append( System.lineSeparator() ).append( "-- DELETE FROM WM_SYS_REQ WHERE R_REQ_SYS = '" )
                        .append( getCellVal( firstRow, 4 ) ).append( "';" ).append( System.lineSeparator() );

                for ( Row row : rows ) {
                    if ( StringUtil.isBlank( getCellVal( row, 5 ) ) ) continue;

                    writer.append(
                            "INSERT INTO WM_SYS_REQ(SID, R_REQ_SYS, S_REQ_CODE, S_REQ_NAME, R_SVC_SYS, S_SVC_CODE, S_SVC_NAME, R_INTE, S_INTE_CODE, S_INTE_NAME, S_INTE_URL, T_ENABLE, C_REMARK) VALUES (" )
                            .append( "'" ).append( UUIDUtil.generate() ).append( "', " )

                            .append( "'" ).append( getCellVal( row, 4 ) ).append( "', " ) // 请求系统UUID
                            .append( "'" ).append( getCellVal( row, 5 ) ).append( "', " ) // 请求系统编码
                            // .append( "'" ).append( getCellVal( row, 2 )
                            // ).append( "', " ) // 请求编码
                            .append( "'" ).append( getCellVal( row, 9 ) ).append( "', " ) // 请求方系统名称

                            .append( "'" ).append( getCellVal( row, 6 ) ).append( "', " ) // 服务系统UUID
                            .append( "'" ).append( getCellVal( row, 7 ) ).append( "', " ) // 服务系统编码
                            .append( "'" ).append( getCellVal( row, 10 ) ).append( "', " ) // 服务提供方系统名称

                            .append( "'" ).append( getCellVal( row, 8 ) ).append( "', " ) // 接口编码
                            .append( "'" ).append( getCellVal( row, 8 ) ).append( "', " ) // 接口代码
                            .append( "'" ).append( getCellVal( row, 11 ) ).append( "', " ) // 服务方接口名称
                            .append( "'" ).append( getCellVal( row, 12 ) ).append( "', " ) // 调用接口URL

                            // .append( "'" ).append( getCellVal( row, 1 )
                            // ).append( "', " ) // 来源系统编码

                            // .append( "'" ).append( getCellVal( row, 3 )
                            // ).append( "', " ) // 临时系统编码

                            .append( "TO_DATE('" ).append( getCellDate( row, 13 ) ).append( "', 'YYYY-MM-DD'), " ) // 启用时间
                            .append( "'" ).append( String.format( "服务方系统:%s, 接口名称:%s, 接口URL:%s", getCellVal( row, 10 ), getCellVal( row, 11 ), getCellVal( row, 12 ) ) )
                            .append( "'" ) // 备注
                            .append( ");" ).append( System.lineSeparator() );
                }
                writer.append( System.lineSeparator() ).append( System.lineSeparator() );
            }

            writer.append( "UPDATE WM_SYS_REQ SET P_RUN = '10', S_RUN = '运行';" ).append( System.lineSeparator() );
            writer.append( "UPDATE WM_SYS_REQ A SET S_INTE_CODE = (SELECT C_CODE FROM WM_SYS_INTE B WHERE A.R_INTE = B.SID);" ).append( System.lineSeparator() );
            writer.append( "COMMIT;" ).append( System.lineSeparator() );
        }
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }

    public static void generate( String excelFile, String excelSheetName, String output ) throws IOException {
        excelFile = Normalizer.normalize( StringUtil.empty( excelFile, "" ), Normalizer.Form.NFKC );

        try ( SystemRelDataGenerator generator = new SystemRelDataGenerator( excelFile ) ) {
            generator.generate( excelSheetName, output );
        }
    }
}
