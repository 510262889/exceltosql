package main.java.datagenerator;

import java.io.IOException;
import java.text.Normalizer;

import util.FileUtil;
import util.StringUtil;

public class Main {
    private static final String EXCEL_FILE = "doc/2017.01.04 - 平台及业务系统接口调查表 - 02汇总数据整理（根据小卓的测试情况整理） - 罗建新（导入系统）.xls";
    private static final String EXCEL_SHEET_NAME_SYSTEM = "WM_SYSTEM";
    private static final String EXCEL_SHEET_NAME_INTE = "2-对外服务接口调查表";
    private static final String EXCEL_SHEET_NAME_SYS_REL = "3-请求外部服务接口调查表";

    public static void main( String[] args ) throws IOException {
        String file = EXCEL_FILE, target = "scripts";
        target = Normalizer.normalize( StringUtil.empty( target, "" ), Normalizer.Form.NFKC );
        args[0] = Normalizer.normalize( StringUtil.empty( args[0], "" ), Normalizer.Form.NFKC );
        args[1] = Normalizer.normalize( StringUtil.empty( args[1], "" ), Normalizer.Form.NFKC );
        if ( args.length >= 1 ) file = StringUtil.empty( args[0], EXCEL_FILE );
        if ( args.length >= 2 ) target = StringUtil.empty( args[1], "scripts" );
        FileUtil.createFolder( target );

        System.out.println( "正在生成【60_正式数据_业务系统.sql】数据库脚本...." );
        SystemDataGenerator.generate( file, EXCEL_SHEET_NAME_SYSTEM, target + "/60_正式数据_业务系统.sql" );
        System.out.println( "正在生成【61_正式数据_业务系统接口.sql】数据库脚本...." );
        InterfaceDataGenerator.generate( file, EXCEL_SHEET_NAME_INTE, target + "/61_正式数据_业务系统接口.sql" );
        System.out.println( "正在生成【62_正式数据_系统关系.sql】数据库脚本...." );
        SystemRelDataGenerator.generate( file, EXCEL_SHEET_NAME_SYS_REL, target + "/62_正式数据_系统关系.sql" );
        System.out.println( "生成数据库脚本完成...." );
    }
}
