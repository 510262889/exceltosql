package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;

public class FileUtil {
    /**
     * 从一个文本文件中读取文件
     */
    public static String readTextFile( String path ) throws Exception {
        char[] buffer = {};
        BufferedReader br = null;
        try {
            br = new BufferedReader( new FileReader( path ) );
            br.read( buffer );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new Exception( "文件读写错误", e );
        } finally {
            try {
                if ( br != null ) br.close();
            } catch ( IOException ignored ) {
            }
        }
        return String.copyValueOf( buffer );
    }

    /**
     * 读取文本文件内容
     *
     * @param filePathAndName
     *            带有完整绝对路径的文件名
     * @param encoding
     *            文本文件打开的编码方式
     *
     * @return 返回文本文件的内容
     */
    public static String readTxt( String filePathAndName, String encoding ) throws IOException {
        encoding = encoding.trim();
        StringBuffer str = new StringBuffer( "" );
        String st = "";
        FileInputStream fs = new FileInputStream( filePathAndName );
        try {
            InputStreamReader isr;
            if ( encoding.equals( "" ) ) {
                isr = new InputStreamReader( fs, "UTF-8" );
            } else {
                isr = new InputStreamReader( fs, encoding );
            }
            BufferedReader br = new BufferedReader( isr );
            try {
                String data;
                while ( (data = br.readLine()) != null ) {
                    str.append( data + "\n" );
                }
            } catch ( Exception e ) {
                str.append( e.toString() );
            } finally {
                br.close();
            }
            st = str.toString();
        } catch ( IOException es ) {
            st = "";
        } finally {
            fs.close();
        }

        return st;
    }

    /**
     * 从指定的流中读取文本
     */
    public static String readTxt( InputStream s, String encoding ) throws IOException {
        encoding = encoding.trim();
        StringBuffer str = new StringBuffer( "" );
        String st = "";
        try {
            InputStreamReader isr;
            if ( encoding.equals( "" ) ) {
                isr = new InputStreamReader( s );
            } else {
                isr = new InputStreamReader( s, encoding );
            }
            BufferedReader br = new BufferedReader( isr );
            try {
                String data;
                while ( (data = br.readLine()) != null ) {
                    str.append( data + "\n" );
                }
            } catch ( Exception e ) {
                str.append( e.toString() );
            }
            st = str.toString();
        } catch ( IOException es ) {
            st = "";
        } finally {
            s.close();
        }

        return st;
    }

    /**
     * 复制文件
     *
     * @param source
     *            源文件
     * @param target
     *            目标文件
     *
     * @return 成功复制返回true，否则返回false。
     */
    public static boolean copyFile( File source, File target ) throws RuntimeException {
        boolean copy = false;
        try {
            int byteread;
            if ( source.exists() ) // 文件存在时
            {
                InputStream inStream = new FileInputStream( source ); // 读入原文件
                FileOutputStream fs = new FileOutputStream( target );

                byte[] buffer = new byte[1024 * 5];
                while ( (byteread = inStream.read( buffer )) != -1 ) {
                    fs.write( buffer, 0, byteread );
                }
                inStream.close();
                fs.flush();
                fs.close();

                copy = true;
            }
        } catch ( Exception e ) {
            throw new RuntimeException( "复制文件操作出错", e );
        }

        return copy;
    }

    /**
     * 复制文件
     *
     * @param source
     *            源文件
     * @param target
     *            目标文件
     *
     * @return 成功复制返回true，否则返回false。
     */
    public static boolean copyFile( String source, String target ) throws RuntimeException {
        return copyFile( new File( source ), new File( target ) );
    }

    /**
     * 删除文件
     *
     * @param file
     *            要删除的文件
     *
     * @return 删除成功返回true，否则返回false。
     */
    public static boolean delFile( File file ) {
        return file.exists() && file.delete();
    }

    /**
     * 删除文件
     */
    public static boolean delFile( String filename ) {
        return delFile( new File( filename ) );
    }

    /**
     * 移动文件
     *
     * @param source
     *            源文件
     * @param target
     *            目标文件
     *
     * @return 移动成功返回true，否则返回false。
     */
    public static synchronized boolean moveFile( File source, File target ) {
        try {
            boolean result = copyFile( source, target );
            if ( result ) result = delFile( source );
            return result;
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * 连接父子路径
     */
    public static String combine( String parent, String child ) {
        return new File( parent, child ).getPath();
    }

    /**
     * 连接父子路径
     */
    public static String combine( String parent, String... paths ) {
        for ( String path : paths ) {
            parent = combine( parent, path );
        }
        return parent;
    }

    /**
     * 新建目录
     *
     * @param folder
     *            目录
     */
    public static boolean createFolder( File folder ) {
        try {
            if ( !folder.exists() ) folder.mkdirs();
        } catch ( Exception e ) {
            throw new RuntimeException( "创建目录错误：" + e.getMessage() );
        }
        return true;
    }

    /**
     * 新建目录
     *
     * @param folderPath
     *            目录
     */
    public static boolean createFolder( String folderPath ) throws RuntimeException {
        folderPath = Normalizer.normalize( StringUtil.empty( folderPath, "" ), Normalizer.Form.NFKC );
        return createFolder( new File( folderPath ) );
    }

    /**
     * 在指定目录下新建目录
     *
     * @param parentFolder
     *            父目录
     * @param childFolder
     *            子目录
     */
    public static boolean createFolder( String parentFolder, String childFolder ) {
        return createFolder( new File( parentFolder, childFolder ) );
    }

    /**
     * 获取文件的创建时间 TODO: 问题：文件全路径不能含有空格
     */
    public synchronized static String getCreateTime( String filename ) {
        String getTime = null;
        try {
            Process p = Runtime.getRuntime().exec( "cmd /C dir " + filename + " /tc" );

            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
            String result;

            while ( (result = br.readLine()) != null ) {
                String[] str = result.split( " " );
                for ( int i = str.length - 1; i >= 0; i-- ) {
                    if ( str[i].equals( filename.substring( filename.lastIndexOf( "\\" ) + 1 ) ) ) {
                        getTime = str[0] + " " + str[2];
                    }
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return getTime;
    }

    /**
     * 判断目录是否存在
     */
    public static boolean dirExists( String path ) {
        File f = new File( path );
        return f.exists() && f.isDirectory();
    }

    /**
     * 判断文件是否存在
     */
    public static boolean fileExists( String path ) {
        path = Normalizer.normalize( StringUtil.empty( path, "" ), Normalizer.Form.NFKC );
        File f = new File( path );
        return f.exists();
    }
}
