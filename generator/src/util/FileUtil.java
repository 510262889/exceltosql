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
     * ��һ���ı��ļ��ж�ȡ�ļ�
     */
    public static String readTextFile( String path ) throws Exception {
        char[] buffer = {};
        BufferedReader br = null;
        try {
            br = new BufferedReader( new FileReader( path ) );
            br.read( buffer );
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new Exception( "�ļ���д����", e );
        } finally {
            try {
                if ( br != null ) br.close();
            } catch ( IOException ignored ) {
            }
        }
        return String.copyValueOf( buffer );
    }

    /**
     * ��ȡ�ı��ļ�����
     *
     * @param filePathAndName
     *            ������������·�����ļ���
     * @param encoding
     *            �ı��ļ��򿪵ı��뷽ʽ
     *
     * @return �����ı��ļ�������
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
     * ��ָ�������ж�ȡ�ı�
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
     * �����ļ�
     *
     * @param source
     *            Դ�ļ�
     * @param target
     *            Ŀ���ļ�
     *
     * @return �ɹ����Ʒ���true�����򷵻�false��
     */
    public static boolean copyFile( File source, File target ) throws RuntimeException {
        boolean copy = false;
        try {
            int byteread;
            if ( source.exists() ) // �ļ�����ʱ
            {
                InputStream inStream = new FileInputStream( source ); // ����ԭ�ļ�
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
            throw new RuntimeException( "�����ļ���������", e );
        }

        return copy;
    }

    /**
     * �����ļ�
     *
     * @param source
     *            Դ�ļ�
     * @param target
     *            Ŀ���ļ�
     *
     * @return �ɹ����Ʒ���true�����򷵻�false��
     */
    public static boolean copyFile( String source, String target ) throws RuntimeException {
        return copyFile( new File( source ), new File( target ) );
    }

    /**
     * ɾ���ļ�
     *
     * @param file
     *            Ҫɾ�����ļ�
     *
     * @return ɾ���ɹ�����true�����򷵻�false��
     */
    public static boolean delFile( File file ) {
        return file.exists() && file.delete();
    }

    /**
     * ɾ���ļ�
     */
    public static boolean delFile( String filename ) {
        return delFile( new File( filename ) );
    }

    /**
     * �ƶ��ļ�
     *
     * @param source
     *            Դ�ļ�
     * @param target
     *            Ŀ���ļ�
     *
     * @return �ƶ��ɹ�����true�����򷵻�false��
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
     * ���Ӹ���·��
     */
    public static String combine( String parent, String child ) {
        return new File( parent, child ).getPath();
    }

    /**
     * ���Ӹ���·��
     */
    public static String combine( String parent, String... paths ) {
        for ( String path : paths ) {
            parent = combine( parent, path );
        }
        return parent;
    }

    /**
     * �½�Ŀ¼
     *
     * @param folder
     *            Ŀ¼
     */
    public static boolean createFolder( File folder ) {
        try {
            if ( !folder.exists() ) folder.mkdirs();
        } catch ( Exception e ) {
            throw new RuntimeException( "����Ŀ¼����" + e.getMessage() );
        }
        return true;
    }

    /**
     * �½�Ŀ¼
     *
     * @param folderPath
     *            Ŀ¼
     */
    public static boolean createFolder( String folderPath ) throws RuntimeException {
        folderPath = Normalizer.normalize( StringUtil.empty( folderPath, "" ), Normalizer.Form.NFKC );
        return createFolder( new File( folderPath ) );
    }

    /**
     * ��ָ��Ŀ¼���½�Ŀ¼
     *
     * @param parentFolder
     *            ��Ŀ¼
     * @param childFolder
     *            ��Ŀ¼
     */
    public static boolean createFolder( String parentFolder, String childFolder ) {
        return createFolder( new File( parentFolder, childFolder ) );
    }

    /**
     * ��ȡ�ļ��Ĵ���ʱ�� TODO: ���⣺�ļ�ȫ·�����ܺ��пո�
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
     * �ж�Ŀ¼�Ƿ����
     */
    public static boolean dirExists( String path ) {
        File f = new File( path );
        return f.exists() && f.isDirectory();
    }

    /**
     * �ж��ļ��Ƿ����
     */
    public static boolean fileExists( String path ) {
        path = Normalizer.normalize( StringUtil.empty( path, "" ), Normalizer.Form.NFKC );
        File f = new File( path );
        return f.exists();
    }
}
