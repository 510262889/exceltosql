package util.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.text.Normalizer;

import util.StringUtil;
import util.URIUtil;

/**
 * File Resource.
 *
 * Handle resources of implied or explicit file type. This class can check for
 * aliasing in the filesystem (eg case insensitivity). By default this is turned
 * on, or it can be controlled by calling the static method @see
 * FileResource#setCheckAliases(boolean)
 */
@SuppressWarnings("WeakerAccess")
public class FileResource extends URLResource {
    private File _file;

    public FileResource(URL url) throws IOException, URISyntaxException {
        super( url, null );
        _urlString = Normalizer.normalize( StringUtil.empty( _urlString, "" ), Normalizer.Form.NFKC );

        try {
            // Try standard API to convert URL to file.
            _file = new File( new URI( url.toString() ) );
        } catch ( URISyntaxException e ) {
            throw e;
        } catch ( Exception e ) {
            ignoreLog( e );
            try {
                // Assume that File.toURL produced unencoded chars. So try
                // encoding them.
                String file_url = "file:" + URIUtil.encodePath( url.toString().substring( 5 ) );
                file_url = Normalizer.normalize( StringUtil.empty( file_url, "" ), Normalizer.Form.NFKC );

                URI uri = new URI( file_url );
                if ( uri.getAuthority() == null ) _file = new File( uri );
                else _file = new File( "//" + uri.getAuthority() + URIUtil.decodePath( url.getFile() ) );
            } catch ( Exception e2 ) {
                ignoreLog( e2 );

                // Still can't get the file. Doh! try good old hack!
                checkConnection();
                Permission perm = _connection.getPermission();
                _file = new File( perm == null ? url.getFile() : perm.getName() );
            }
        }
        if ( _file.isDirectory() ) {
            if ( !_urlString.endsWith( "/" ) ) _urlString = _urlString + "/";
        } else {
            if ( _urlString.endsWith( "/" ) ) _urlString = _urlString.substring( 0, _urlString.length() - 1 );
        }

    }

    FileResource(URL url, URLConnection connection, File file) {
        super( url, connection );
        _file = file;
        if ( _file.isDirectory() && !_urlString.endsWith( "/" ) ) _urlString = _urlString + "/";
    }

    @Override
    public Resource addPath( String path ) throws IOException {
        URLResource r;
        String url;

        path = URIUtil.canonicalPath( path );

        if ( "/".equals( path ) ) return this;
        else if ( !isDirectory() ) {
            r = (FileResource) super.addPath( path );
        } else {
            if ( path == null ) throw new MalformedURLException();

            // treat all paths being added as relative
            String rel = path;
            if ( path.startsWith( "/" ) ) rel = path.substring( 1 );

            url = URIUtil.addPaths( _urlString, URIUtil.encodePath( rel ) );
            r = (URLResource) newResource( url );
        }
        return r;
    }

    /**
     * Returns true if the resource exists.
     */
    @Override
    public boolean exists() {
        return _file.exists();
    }

    /**
     * Returns the last modified time
     */
    @Override
    public long lastModified() {
        return _file.lastModified();
    }

    /**
     * Returns true if the respresenetd resource is a container/directory.
     */
    @Override
    public boolean isDirectory() {
        return _file.isDirectory();
    }

    /* --------------------------------------------------------- */

    /**
     * Return the length of the resource
     */
    @Override
    public long length() {
        return _file.length();
    }

    /* --------------------------------------------------------- */

    /**
     * Returns the name of the resource
     */
    @Override
    public String getName() {
        return _file.getAbsolutePath();
    }

    /**
     * Returns an File representing the given resource or NULL if this is not
     * possible.
     */
    @Override
    public File getFile() {
        return _file;
    }

    /* --------------------------------------------------------- */

    /**
     * Returns an input stream to the resource
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream( _file );
    }

    /* --------------------------------------------------------- */

    /**
     * Returns an output stream to the resource
     */
    @Override
    public OutputStream getOutputStream() throws IOException, SecurityException {
        return new FileOutputStream( _file );
    }

    /* --------------------------------------------------------- */

    /**
     * Deletes the given resource
     */
    @Override
    public boolean delete() throws SecurityException {
        return _file.delete();
    }

    /* --------------------------------------------------------- */

    /**
     * Rename the given resource
     */
    @Override
    public boolean renameTo( Resource dest ) throws SecurityException {
        return dest instanceof FileResource && _file.renameTo( ((FileResource) dest)._file );
    }

    /* --------------------------------------------------------- */

    /**
     * Returns a list of resources contained in the given resource
     */
    @Override
    public String[] list() {
        String[] list = _file.list();
        if ( list == null ) return null;
        for ( int i = list.length; i-- > 0; ) {
            if ( new File( _file, list[i] ).isDirectory() && !list[i].endsWith( "/" ) ) list[i] += "/";
        }
        return list;
    }

    /**
     * @return <code>true</code> of the object <code>o</code> is a
     *         {@link FileResource} pointing to the same file as this resource.
     */
    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;

        if ( null == o || !(o instanceof FileResource) ) return false;

        FileResource f = (FileResource) o;
        return f._file == _file || (null != _file && _file.equals( f._file ));
    }

    /**
     * @return the hashcode.
     */
    @Override
    public int hashCode() {
        return null == _file ? super.hashCode() : _file.hashCode();
    }

}
