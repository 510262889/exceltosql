package util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtil {
    public static final String EMPTY = "";

    /**
     * 将数组转化成字符串
     *
     * @param split
     *            分隔符
     * @param quote
     *            是否加单引号
     */
    public static String join( Object[] list, String split, boolean quote ) {
        if ( list == null ) return "";
        if ( split == null ) split = "";
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < list.length; i++ ) {
            if ( i > 0 ) sb.append( split );
            if ( quote ) sb.append( "'" );
            sb.append( list[i].toString() );
            if ( quote ) sb.append( "'" );
        }
        return sb.toString();
    }

    /**
     * 是否是数字类型
     */
    public static boolean isNumeric( String str ) {
        if ( str == null ) return false;

        int sz = str.length();
        for ( int i = 0; i < sz; i++ )
            if ( Character.isDigit( str.charAt( i ) ) == false ) return false;
        return true;
    }

    /**
     * 如果字符串为空或者没有内容，则替换成默认的字符串
     *
     * @param str
     *            要处理的字符串
     * @param defaultStr
     *            默认字符串
     *
     * @return 结果字符串
     */
    public static String empty( String str, String defaultStr ) {
        return str == null || str.length() < 1 ? defaultStr : str;
    }

    public static String empty( String str ) {
        return empty( str, EMPTY );
    }

    /**
     * 清楚空白字符，加了空串的判断
     */
    public static String trim( String str ) {
        return str == null ? null : str.trim();
    }

    /**
     * 如果字符串为空或者没有内容，则替换成默认的字符串
     *
     * @param str
     *            要处理的字符串
     * @param defaultStr
     *            默认字符串
     *
     * @return 结果字符串
     */
    public static String trim( String str, String defaultStr ) {
        return str == null ? defaultStr : str.trim();
    }

    /**
     * 检查字符串是否为空
     */
    public static boolean isEmpty( String str ) {
        return str == null || str.length() == 0;
    }

    /**
     * 检查是否为空白字符串，NULL，长度为0，空格等
     */
    public static boolean isBlank( String str ) {
        if ( str == null ) return true;
        int strLen = str.length();
        if ( strLen < 1 ) return true;
        for ( int i = 0; i < strLen; i++ )
            if ( (Character.isWhitespace( str.charAt( i ) ) == false) ) return false;
        return true;
    }

    /**
     * 切分为数组。
     */
    public static String[] split2Array( String text, String split ) {
        if ( isEmpty( text ) ) return new String[0];
        return text.split( split );
    }

    public static List<String> split2List( String text, String split ) {
        List<String> list = new ArrayList<>();
        if ( isBlank( text ) ) return list;
        Collections.addAll( list, text.split( split ) );
        return list;
    }

    /**
     * 按照分隔符号填充到某一个列表中
     */
    public static void split2List( String text, String split, List<String> list ) {
        if ( isBlank( text ) ) return;
        Collections.addAll( list, text.split( split ) );
    }

    /**
     * 将"1:xxx,2:ppp,3:ddd"这样的定义转换为“KEY-VALUE”的列表
     */
    public static Map<String, String> split2Map( String src ) {
        Map<String, String> m = new HashMap<>();
        if ( src == null ) return m;
        for ( String o : src.split( "," ) ) {
            if ( isBlank( o ) ) continue;
            String[] item = o.split( ":" );
            String key = item[0].trim();
            String value = key;
            if ( item.length > 1 ) value = item[1].trim();
            if ( key.length() < 1 ) key = value;
            m.put( key, value );
        }
        return m;
    }

    /**
     * 首字母变小写
     */
    public static String firstCharToLowerCase( String str ) {
        char firstChar = str.charAt( 0 );
        if ( firstChar >= 'A' && firstChar <= 'Z' ) {
            char[] arr = str.toCharArray();
            arr[0] += ('a' - 'A');
            return new String( arr );
        }
        return str;
    }

    /**
     * 首字母变大写
     */
    public static String firstCharToUpperCase( String str ) {
        char firstChar = str.charAt( 0 );
        if ( firstChar >= 'a' && firstChar <= 'z' ) {
            char[] arr = str.toCharArray();
            arr[0] -= ('a' - 'A');
            return new String( arr );
        }
        return str;
    }

    /**
     * 字符串不为 null 而且不为 "" 时返回 true
     */
    public static boolean notBlank( String str ) {
        return str != null && !"".equals( str.trim() );
    }

    public static boolean notBlank( String... strings ) {
        if ( strings == null ) return false;
        for ( String str : strings )
            if ( str == null || "".equals( str.trim() ) ) return false;
        return true;
    }

    public static boolean notNull( Object... paras ) {
        if ( paras == null ) return false;
        for ( Object obj : paras )
            if ( obj == null ) return false;
        return true;
    }

    public static String toCamelCase( String stringWithUnderline ) {
        if ( stringWithUnderline.indexOf( '_' ) == -1 ) return stringWithUnderline;

        stringWithUnderline = stringWithUnderline.toLowerCase();
        char[] fromArray = stringWithUnderline.toCharArray();
        char[] toArray = new char[fromArray.length];
        int j = 0;
        for ( int i = 0; i < fromArray.length; i++ ) {
            if ( fromArray[i] == '_' ) {
                // 当前字符为下划线时，将指针后移一位，将紧随下划线后面一个字符转成大写并存放
                i++;
                if ( i < fromArray.length ) toArray[j++] = Character.toUpperCase( fromArray[i] );
            } else {
                toArray[j++] = fromArray[i];
            }
        }
        return new String( toArray, 0, j );
    }

    public static String join( String[] stringArray ) {
        StringBuilder sb = new StringBuilder();
        for ( String s : stringArray )
            sb.append( s );
        return sb.toString();
    }

    public static String join( String[] stringArray, String separator ) {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < stringArray.length; i++ ) {
            if ( i > 0 ) sb.append( separator );
            sb.append( stringArray[i] );
        }
        return sb.toString();
    }

    /**
     * 简单的格式化文本,从GOOGLE的GUAVA中复制出来，实现%s的简单替换
     */
    public static String format( String template, Object... args ) {
        template = String.valueOf( template );

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder( template.length() + 16 * args.length );
        int templateStart = 0;
        int i = 0;
        while ( i < args.length ) {
            int placeholderStart = template.indexOf( "%s", templateStart );
            if ( placeholderStart == -1 ) {
                break;
            }
            builder.append( template.substring( templateStart, placeholderStart ) );
            builder.append( args[i++] );
            templateStart = placeholderStart + 2;
        }
        builder.append( template.substring( templateStart ) );

        // if we run out of placeholders, append the extra args in square braces
        if ( i < args.length ) {
            builder.append( " [" );
            builder.append( args[i++] );
            while ( i < args.length ) {
                builder.append( ", " );
                builder.append( args[i++] );
            }
            builder.append( ']' );
        }

        return builder.toString();
    }

    /* 转换为JS里面的字符串 */
    public static String encodeInString( String o ) {
        return o == null ? null : o.replaceAll( "\\\\", "\\\\\\\\" ).replaceAll( "\\\"", "\\\\\"" ).replaceAll( "'", "\\\\\'" ).replace( "\n", "\\n" ).replace( "\r", "\\r" );

    }

    /* 转换为JS里面的字符串 */
    public static String encodeJavascriptString( String o ) {
        return o == null ? null : o.replaceAll( "\\\\", "\\\\\\\\" ).replaceAll( "\\\"", "\\\\\"" ).replace( "\n", "\\n" ).replace( "\r", "\\r" );

    }

    /**
     * 字符串转换为整数
     */
    public static int toInteger( String text, int defvalue ) {
        if ( text == null || text.length() == 0 ) return defvalue;
        try {
            return Integer.parseInt( text );
        } catch ( Throwable e ) {
            return defvalue;
        }
    }

    /**
     * 字符串转换为整数
     */
    public static long toLong( String text, int defvalue ) {
        if ( text == null || text.length() == 0 ) return defvalue;
        try {
            return Long.parseLong( text );
        } catch ( Throwable e ) {
            return defvalue;
        }
    }

    /**
     * 字符串转化为浮点数
     *
     * @param text
     *            待转换字符串
     * @param v
     *            转换失败的默认值
     *
     * @return 结果
     */
    public static double toDouble( String text, double v ) {
        try {
            return Double.parseDouble( text );
        } catch ( Throwable e ) {
            return v;
        }
    }

    /**
     * 日期转换函数，自动匹配格式，必须是年月日时分秒格式
     *
     * @param str
     *            字符串
     * @param defval
     *            默认值
     *
     * @return 值
     */
    public static Date toDate( String str, Date defval ) {
        try {
            if ( isBlank( str ) ) return defval;

            StringBuilder tmp = new StringBuilder();
            for ( int i = 0; i < str.length(); i++ ) {
                char c = str.charAt( i );
                if ( c >= '0' && c <= '9' ) tmp.append( c );
            }
            String val = tmp.toString();
            int ilen = val.length();
            String format = null;
            if ( ilen == 4 ) format = "MMdd";
            else if ( ilen == 8 ) format = "yyyyMMdd";
            else if ( ilen == 10 ) format = "yyyyMMddHH";
            else if ( ilen == 12 ) format = "yyyyMMddHHmm";
            else if ( ilen == 14 ) format = "yyyyMMddHHmmss";
            else format = "yyyyMMdd"; // 非正常长度分析到day

            return new SimpleDateFormat( format ).parse( str );
        } catch ( Throwable e ) {
            return defval;
        }
    }

    public static boolean toBoolean( String str, boolean defvalue ) {
        if ( isBlank( str ) ) return defvalue;
        if ( str.trim().equalsIgnoreCase( "true" ) ) return true;
        if ( str.trim().equalsIgnoreCase( "1" ) ) return true;
        if ( str.trim().equalsIgnoreCase( "T" ) ) return true;
        if ( str.trim().equalsIgnoreCase( "false" ) ) return false;
        if ( str.trim().equalsIgnoreCase( "0" ) ) return false;
        if ( str.trim().equalsIgnoreCase( "F" ) ) return false;

        return defvalue;
    }

    /**
     * 判断字符串指定位置是否由特定字符串开始（忽略大小写）
     *
     * @param text
     *            要检查的字符串
     * @param prefix
     *            匹配头部的字符串
     * @param begin
     *            起始比较位置
     *
     * @return TRUE = 符合，FALSE = 不符合
     */
    public static boolean startsWithIgnoreCase( String text, String prefix, int begin ) {
        int to = begin, po = 0;
        int pc = prefix.length();
        if ( (begin < 0) || (begin > text.length() - pc) ) return false;
        while ( --pc >= 0 ) {
            if ( Character.toUpperCase( text.charAt( to++ ) ) != Character.toUpperCase( prefix.charAt( po++ ) ) ) return false;
        }
        return true;
    }

    /**
     * 判断字符串指定位置是否由特定字符串开始（忽略大小写）
     *
     * @param text
     *            要检查的字符串
     * @param prefix
     *            匹配头部的字符串
     *
     * @return TRUE = 符合，FALSE = 不符合
     */
    public static boolean startsWithIgnoreCase( String text, String prefix ) {
        return startsWithIgnoreCase( text, prefix, 0 );
    }

    /**
     * 将字符串按照HTML规则来编码，剔除HTML中被保留而要求转义的数据
     */
    public static String encodeHTML( String input ) {
        if ( input == null ) return "";
        int size = input.length();
        StringBuffer buffer = new StringBuffer( size );
        for ( int i = 0; i < size; i++ ) {
            char c = input.charAt( i );
            switch ( c ) {
            case '<':
                buffer.append( "&lt;" );
                break;
            case '>':
                buffer.append( "&gt;" );
                break;
            case '"':
                buffer.append( "&quot;" );
                break;
            default:
                buffer.append( c );
            }
        }
        return buffer.toString();
    }
}
