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
     * ������ת�����ַ���
     *
     * @param split
     *            �ָ���
     * @param quote
     *            �Ƿ�ӵ�����
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
     * �Ƿ�����������
     */
    public static boolean isNumeric( String str ) {
        if ( str == null ) return false;

        int sz = str.length();
        for ( int i = 0; i < sz; i++ )
            if ( Character.isDigit( str.charAt( i ) ) == false ) return false;
        return true;
    }

    /**
     * ����ַ���Ϊ�ջ���û�����ݣ����滻��Ĭ�ϵ��ַ���
     *
     * @param str
     *            Ҫ������ַ���
     * @param defaultStr
     *            Ĭ���ַ���
     *
     * @return ����ַ���
     */
    public static String empty( String str, String defaultStr ) {
        return str == null || str.length() < 1 ? defaultStr : str;
    }

    public static String empty( String str ) {
        return empty( str, EMPTY );
    }

    /**
     * ����հ��ַ������˿մ����ж�
     */
    public static String trim( String str ) {
        return str == null ? null : str.trim();
    }

    /**
     * ����ַ���Ϊ�ջ���û�����ݣ����滻��Ĭ�ϵ��ַ���
     *
     * @param str
     *            Ҫ������ַ���
     * @param defaultStr
     *            Ĭ���ַ���
     *
     * @return ����ַ���
     */
    public static String trim( String str, String defaultStr ) {
        return str == null ? defaultStr : str.trim();
    }

    /**
     * ����ַ����Ƿ�Ϊ��
     */
    public static boolean isEmpty( String str ) {
        return str == null || str.length() == 0;
    }

    /**
     * ����Ƿ�Ϊ�հ��ַ�����NULL������Ϊ0���ո��
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
     * �з�Ϊ���顣
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
     * ���շָ�������䵽ĳһ���б���
     */
    public static void split2List( String text, String split, List<String> list ) {
        if ( isBlank( text ) ) return;
        Collections.addAll( list, text.split( split ) );
    }

    /**
     * ��"1:xxx,2:ppp,3:ddd"�����Ķ���ת��Ϊ��KEY-VALUE�����б�
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
     * ����ĸ��Сд
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
     * ����ĸ���д
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
     * �ַ�����Ϊ null ���Ҳ�Ϊ "" ʱ���� true
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
                // ��ǰ�ַ�Ϊ�»���ʱ����ָ�����һλ���������»��ߺ���һ���ַ�ת�ɴ�д�����
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
     * �򵥵ĸ�ʽ���ı�,��GOOGLE��GUAVA�и��Ƴ�����ʵ��%s�ļ��滻
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

    /* ת��ΪJS������ַ��� */
    public static String encodeInString( String o ) {
        return o == null ? null : o.replaceAll( "\\\\", "\\\\\\\\" ).replaceAll( "\\\"", "\\\\\"" ).replaceAll( "'", "\\\\\'" ).replace( "\n", "\\n" ).replace( "\r", "\\r" );

    }

    /* ת��ΪJS������ַ��� */
    public static String encodeJavascriptString( String o ) {
        return o == null ? null : o.replaceAll( "\\\\", "\\\\\\\\" ).replaceAll( "\\\"", "\\\\\"" ).replace( "\n", "\\n" ).replace( "\r", "\\r" );

    }

    /**
     * �ַ���ת��Ϊ����
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
     * �ַ���ת��Ϊ����
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
     * �ַ���ת��Ϊ������
     *
     * @param text
     *            ��ת���ַ���
     * @param v
     *            ת��ʧ�ܵ�Ĭ��ֵ
     *
     * @return ���
     */
    public static double toDouble( String text, double v ) {
        try {
            return Double.parseDouble( text );
        } catch ( Throwable e ) {
            return v;
        }
    }

    /**
     * ����ת���������Զ�ƥ���ʽ��������������ʱ�����ʽ
     *
     * @param str
     *            �ַ���
     * @param defval
     *            Ĭ��ֵ
     *
     * @return ֵ
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
            else format = "yyyyMMdd"; // ���������ȷ�����day

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
     * �ж��ַ���ָ��λ���Ƿ����ض��ַ�����ʼ�����Դ�Сд��
     *
     * @param text
     *            Ҫ�����ַ���
     * @param prefix
     *            ƥ��ͷ�����ַ���
     * @param begin
     *            ��ʼ�Ƚ�λ��
     *
     * @return TRUE = ���ϣ�FALSE = ������
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
     * �ж��ַ���ָ��λ���Ƿ����ض��ַ�����ʼ�����Դ�Сд��
     *
     * @param text
     *            Ҫ�����ַ���
     * @param prefix
     *            ƥ��ͷ�����ַ���
     *
     * @return TRUE = ���ϣ�FALSE = ������
     */
    public static boolean startsWithIgnoreCase( String text, String prefix ) {
        return startsWithIgnoreCase( text, prefix, 0 );
    }

    /**
     * ���ַ�������HTML���������룬�޳�HTML�б�������Ҫ��ת�������
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
