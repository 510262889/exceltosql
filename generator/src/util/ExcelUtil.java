package util;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.NumberToTextConverter;

public class ExcelUtil {

    public static String getCellValString( Row row, int cellIndex ) {
        Cell cell = CellUtil.getCell( row, cellIndex );
        String v = "";
        if ( cell != null ) switch ( cell.getCellType() ) {
        case Cell.CELL_TYPE_STRING:
            v = cell.getStringCellValue();
            break;
        case Cell.CELL_TYPE_NUMERIC:
            if ( DateUtil.isCellDateFormatted( cell ) ) {
                v = cell.toString();
            } else {
                v = NumberToTextConverter.toText( cell.getNumericCellValue() );
            }
            break;
        case Cell.CELL_TYPE_FORMULA:
            // TODO: 表达式值计算
        case Cell.CELL_TYPE_ERROR:
            break;
        }

        return v;
    }

    public static double getCellValNumber( Row row, int cellIndex ) {
        Cell cell = CellUtil.getCell( row, cellIndex );
        double v = 0.0;
        if ( cell != null ) switch ( cell.getCellType() ) {
        case Cell.CELL_TYPE_NUMERIC:
            v = cell.getNumericCellValue();
            break;
        case Cell.CELL_TYPE_STRING:
            // TODO: 字符串值转换
        case Cell.CELL_TYPE_FORMULA:
            // TODO: 表达式值计算
        case Cell.CELL_TYPE_ERROR:
            break;
        }

        return v;
    }

    public static Date getCellValDate( Row row, int colIndex ) {
        Cell cell = CellUtil.getCell( row, colIndex );
        Date v = null;
        if ( cell != null ) switch ( cell.getCellType() ) {
        case Cell.CELL_TYPE_NUMERIC:
            if ( DateUtil.isCellDateFormatted( cell ) ) {
                v = cell.getDateCellValue();
            } else {
                v = DateUtil.getJavaDate( cell.getNumericCellValue() );
            }
            break;
        case Cell.CELL_TYPE_STRING:
            // TODO: 类型转换
        case Cell.CELL_TYPE_FORMULA:
            // TODO: 表达式值计算
        case Cell.CELL_TYPE_ERROR:
            break;
        }

        return v;
    }

    /**
     * 用于将Excel表格中列号字母转成列索引，从1对应A开始
     *
     * @param column
     *            列号
     *
     * @return 列索引
     */
    public static int columnToIndex( String column ) {
        if ( !column.matches( "[A-Z]+" ) ) {
            try {
                throw new Exception( "Invalid parameter" );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        int index = 0;
        char[] chars = column.toUpperCase().toCharArray();
        for ( int i = 0; i < chars.length; i++ ) {
            index += (chars[i] - 'A' + 1) * (int) Math.pow( 26, chars.length - i - 1 );
        }
        return index;
    }

    /**
     * 用于将excel表格中列索引转成列号字母，从A对应1开始
     *
     * @param index
     *            列索引
     *
     * @return 列号
     */
    public static String indexToColumn( int index ) {
        if ( index <= 0 ) {
            try {
                throw new Exception( "Invalid parameter" );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        index--;
        String column = "";
        do {
            if ( column.length() > 0 ) {
                index--;
            }
            column = ((char) (index % 26 + 'A')) + column;
            index = (index - index % 26) / 26;
        } while ( index > 0 );
        return column;
    }
}
