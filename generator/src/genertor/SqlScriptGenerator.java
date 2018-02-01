package genertor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Element;

import genertor.config.DefaultFieldConfig;
import genertor.config.ExcelConfig;
import genertor.config.FieldConfig;
import genertor.config.SheetConfig;
import genertor.config.TableConfig;
import genertor.config.TypeMapConfig;
import util.StringUtil;
import util.XmlUtil;

/**
 * SQL脚本生成器，基于Excel定义。
 */
public class SqlScriptGenerator {
    public static final String CONFIG_FILE = "config.xml";
    private final ScriptGenerater scriptGenerater;
    private ExcelConfig config;

    public SqlScriptGenerator(ScriptGenerater scriptGenerater) {
        this( scriptGenerater, CONFIG_FILE );
    }

    public SqlScriptGenerator(ScriptGenerater scriptGenerater, String config) {
        this.scriptGenerater = scriptGenerater;
        // 先试着从运行目录读取config.xml文件，如果没有再从包中读取。
        File configFile = new File( config );
        if ( configFile.exists() && configFile.isFile() ) loadConfig( configFile );
        else loadConfig( config );
    }

    /** 加载Excel定义配置 */
    private void loadConfig( File file ) {
        Element root = XmlUtil.getDocumentElement( file );
        this.config = ExcelConfig.parse( root );
    }

    /** 加载Excel定义配置 */
    private void loadConfig( String fileName ) {
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( fileName );
            if ( inputStream == null ) throw new IllegalArgumentException( "config file not found in classpath: " + fileName );
            Element root = XmlUtil.getDocumentElement( inputStream );
            this.config = ExcelConfig.parse( root );
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            if ( inputStream != null ) try {
                inputStream.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    /** 生成脚本 */
    public void generate() throws Exception {
        try ( Workbook workbook = WorkbookFactory
                .create( new File( URLDecoder.decode( Thread.currentThread().getContextClassLoader().getResource( config.getSrc() ).getPath(), "UTF-8" ) ), null, true ) ) {
            // 解析字段Map
            Map<String, FieldType> map = parseTypeMap( workbook );
            List<Field> defaultFields = parseDefaultFields( workbook );

            // 解析字段
            for ( SheetConfig sheetConfig : config.getSheetList() ) {
                Sheet sheet = workbook.getSheet( sheetConfig.getName() );
                // 脚本对象
                Script script = new Script( config.getOut(), sheetConfig.getScript() );
                TableConfig tableConfig = sheetConfig.getTable();
                FieldConfig fieldConfig = sheetConfig.getField();

                // 迭代行
                for ( int rowIndex = sheetConfig.getRow();; rowIndex++ ) {
                    Row row = sheet.getRow( rowIndex );
                    if ( row == null ) break;

                    // 解析表信息
                    Table table = Table.parse( row, tableConfig );
                    if ( StringUtil.isBlank( table.getName() ) ) break;
                    if ( script.tableMap.containsKey( table.getName() ) ) table = script.tableMap.get( table.getName() );
                    else script.tableMap.put( table.getName(), table );

                    // 解析字段
                    Field field = Field.parse( row, fieldConfig );
                    if ( StringUtil.isBlank( field.getName() ) ) break;
                    table.getFields().add( field );
                }

                // 合并默认字段到表中
                for ( Table table : script.tableMap.values() ) {
                    if ( sheetConfig.getSkipMergeList().contains( table.getName() ) ) continue;
                    mergeFields( table.getFields(), defaultFields );
                }

                // 使用脚本生成器生成脚本
                scriptGenerater.generate( map, script );
            }
        }
    }

    /**
     * 合并字段
     *
     * @param target
     *            要合并的字段集合
     * @param fields
     *            准备被合并的字段
     */
    private void mergeFields( Set<Field> target, List<Field> fields ) {
        for ( Field field : fields ) {
            // 如果字段已经存在则不进行覆盖
            if ( target.contains( field ) ) continue;
            // 追加要合并的字段到末尾
            target.add( field );
        }
    }

    /**
     * 解析类型映射
     */
    private Map<String, FieldType> parseTypeMap( Workbook workbook ) {
        TypeMapConfig typeMapConfig = config.getTypeMapConfig();
        Sheet sheet = workbook.getSheet( typeMapConfig.getSheet() );

        Map<String, FieldType> map = new HashMap<>();

        // 迭代行
        for ( int rowIndex = typeMapConfig.getRow();; rowIndex++ ) {
            Row row = sheet.getRow( rowIndex );
            if ( row == null ) break;

            FieldType fieldType = FieldType.parse( row, typeMapConfig );

            map.put( fieldType.getType(), fieldType );
        }

        return map;
    }

    /** 解析默认字段定义 */
    private List<Field> parseDefaultFields( Workbook workbook ) {
        DefaultFieldConfig defaultFieldConfig = config.getDefaultFieldConfig();
        FieldConfig fieldConfig = defaultFieldConfig.getFieldConfig();

        Sheet sheet = workbook.getSheet( defaultFieldConfig.getSheet() );

        List<Field> fields = new ArrayList<>();

        // 迭代行
        for ( int rowIndex = defaultFieldConfig.getRow();; rowIndex++ ) {
            Row row = sheet.getRow( rowIndex );
            if ( row == null ) break;

            // 解析字段
            Field field = Field.parse( row, fieldConfig );
            if ( StringUtil.isBlank( field.getName() ) ) break;
            fields.add( field );
        }

        return fields;
    }
}
