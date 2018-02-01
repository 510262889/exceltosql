package main.java.datagenerator;

import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import util.DateUtil;
import util.ExcelUtil;
import util.StringUtil;

class SystemDataGenerator implements Closeable {
    private final Workbook workbook;

    private SystemDataGenerator(String excelFile) {
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
            writer.append( "DELETE FROM WM_SYSTEM;" ).append( System.lineSeparator() );
            for ( int i = 1; i <= sheet.getLastRowNum(); i++ ) {
                Row row = sheet.getRow( i );
                /*
                 * SID,C_CODE,C_NAME,C_CAPTION,P_TYPE,S_TYPE,P_SYS_TYPE,
                 * S_SYS_TYPE,P_EXTENSION,S_EXTENSION,T_BEGIN,P_DEPLOY,S_DEPLOY,
                 * P_RUN,S_RUN,C_BIND,P_NET,S_NET,C_SAFE,C_URL,S_ORG_01,
                 * R_USER_01,Z_USER_01,S_USER_01,S_TEL_01,B_SMS_01,S_ORG_02,
                 * R_USER_02,Z_USER_02,S_USER_02,S_TEL_02,B_SMS_02,S_ORG_03,
                 * R_USER_03,Z_USER_03,S_USER_03,S_TEL_03,B_SMS_03,S_ORG_04,
                 * R_USER_04,Z_USER_04,S_USER_04,S_TEL_04,B_SMS_04,T_SYNC,
                 * C_SYS_DESC,C_REMARK,C_VERSION,Z_SOURCE
                 * 
                 */

                writer.append(
                        "INSERT INTO WM_SYSTEM(SID,C_CODE,C_NAME,C_CAPTION,P_TYPE,S_TYPE,P_SYS_TYPE,S_SYS_TYPE,P_EXTENSION,S_EXTENSION,T_BEGIN,P_DEPLOY,S_DEPLOY,P_RUN,S_RUN,C_BIND,P_NET,S_NET,C_SAFE,C_URL,S_ORG_01,R_USER_01,Z_USER_01,S_USER_01,S_TEL_01,B_SMS_01,S_ORG_02,R_USER_02,Z_USER_02,S_USER_02,S_TEL_02,B_SMS_02,S_ORG_03,R_USER_03,Z_USER_03,S_USER_03,S_TEL_03,B_SMS_03,S_ORG_04,R_USER_04,Z_USER_04,S_USER_04,S_TEL_04,B_SMS_04,C_SYS_DESC,C_REMARK,C_VERSION,Z_SOURCE) VALUES (" )
                        .append( "'" ).append( getCellVal( row, 1 ) ).append( "', " ) // 系统SID
                        .append( "'" ).append( getCellVal( row, 2 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 3 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 4 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 5 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 6 ) )
                        .append( "', " ).append( "'" ).append( getCellVal( row, 7 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 8 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 9 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 10 ) ).append( "', " ).append( "TO_DATE('" )
                        .append( getCellDate( row, 11 ) ).append( "', 'YYYY-MM-DD'), " ) // 上线时间
                        .append( "'" ).append( getCellVal( row, 12 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 13 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 14 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 15 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 16 ) )
                        .append( "', " ).append( "'" ).append( getCellVal( row, 17 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 18 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 19 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 20 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 21 ) )
                        .append( "', " ).append( "'" ).append( getCellVal( row, 22 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 23 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 24 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 25 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 26 ) )
                        .append( "', " ).append( "'" ).append( getCellVal( row, 27 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 28 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 29 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 30 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 31 ) )
                        .append( "', " ).append( "'" ).append( getCellVal( row, 32 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 33 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 34 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 35 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 36 ) )
                        .append( "', " ).append( "'" ).append( getCellVal( row, 37 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 38 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 39 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 40 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 41 ) )
                        .append( "', " ).append( "'" ).append( getCellVal( row, 42 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 43 ) ).append( "', " ).append( "'" )
                        .append( getCellVal( row, 44 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 46 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 47 ) )
                        .append( "', " ).append( "'" ).append( getCellVal( row, 48 ) ).append( "', " ).append( "'" ).append( getCellVal( row, 49 ) ).append( "'" ).append( ");" )
                        .append( System.lineSeparator() );

            }

            writer.append( "COMMIT;" ).append( System.lineSeparator() );
        }
    }

    private String mapType( String val ) {
        switch ( val ) {
        case "业务类":
            return "8a0e81464de8354e014debf0fd1543f7";
        case "平台类":
            return "8a0e81464de8354e014debf170ac4541";
        }
        return "";
    }

    private String mapType2( String val ) {
        switch ( val ) {
        case "业务类":
            return "业务应用";
        case "平台类":
            return "公共服务平台";
        }
        return "";
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }

    public static void generate( String excelFile, String excelSheetName, String output ) throws IOException {
        try ( SystemDataGenerator generator = new SystemDataGenerator( excelFile ) ) {
            generator.generate( excelSheetName, output );
        }
    }
}
