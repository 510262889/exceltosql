package util.resource;

import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Permission;
import java.text.Normalizer;

import util.StringUtil;
import util.URIUtil;

/**
 * Abstract resource class.
 */
@SuppressWarnings("WeakerAccess")
public class URLResource extends Resource {
    protected URL _url;
    protected String _urlString;

    protected URLConnection _connection;
    protected InputStream _in = null;
    transient boolean _useCaches = __defaultUseCaches;

    protected URLResource(URL url, URLConnection connection) {
        _url = url;

        _urlString = _url.toString();
        _urlString = Normalizer.normalize( StringUtil.empty( _urlString, "" ), Normalizer.Form.NFKC );
        _connection = connection;
    }

    protected URLResource(URL url, URLConnection connection, boolean useCaches) {
        this( url, connection );
        _urlString = Normalizer.normalize( StringUtil.empty( _urlString, "" ), Normalizer.Form.NFKC );
        _useCaches = useCaches;
    }

    protected synchronized boolean checkConnection() {
        if ( _connection == null ) {
            try {
                _connection = _url.openConnection();
                _connection.setUseCaches( _useCaches );
            } catch ( IOException e ) {
                ignoreLog( e );
            }
        }
        return _connection != null;
    }

    /**
     * Release any resources held by the resource.
     */
    @Override
    public synchronized void release() {
        if ( _in != null ) {
            try {
                _in.close();
            } catch ( IOException e ) {
                ignoreLog( e );
            }
            _in = null;
        }

        if ( _connection != null ) _connection = null;
    }

    /**
     * Returns true if the represented resource exists.
     */
    @Override
    public boolean exists() {
        try {
            synchronized (this) {
                if ( checkConnection() && _in == null ) _in = _connection.getInputStream();
            }
        } catch ( IOException e ) {
            ignoreLog( e );
        }
        return _in != null;
    }

    /**
     * Returns true if the respresenetd resource is a container/directory. If
     * the resource is not a file, resources ending with "/" are considered
     * directories.
     */
    @Override
    public boolean isDirectory() {
        return exists() && _url.toString().endsWith( "/" );
    }

    /**
     * Returns the last modified time
     */
    @Override
    public long lastModified() {
        if ( checkConnection() ) return _connection.getLastModified();
        return -1;
    }

    /**
     * Return the length of the resource
     */
    @Override
    public long length() {
        if ( checkConnection() ) return _connection.getContentLength();
        return -1;
    }

    /**
     * Returns an URL representing the given resource
     */
    @Override
    public URL getURL() {
        return _url;
    }

    /**
     * Returns an File representing the given resource or NULL if this is not
     * possible.
     */
    @Override
    public File getFile() throws IOException {
        // Try the permission hack
        if ( checkConnection() ) {
            Permission perm = _connection.getPermission();
            if ( perm instanceof FilePermission ) return new File( perm.getName() );
        }

        // Try the URL file arg
        try {
            return new File( _url.getFile() );
        } catch ( Exception e ) {
            ignoreLog( e );
        }

        // Don't know the file
        return null;
    }

    /**
     * Returns the name of the resource
     */
    @Override
    public String getName() {
        return _url.toExternalForm();
    }

    /**
     * Returns an input stream to the resource
     */
    @Override
    public synchronized InputStream getInputStream() throws IOException {
        if ( !checkConnection() ) throw new IOException( "Invalid resource" );

        try {
            if ( _in != null ) {
                InputStream in = _in;
                _in = null;
                return in;
            }
            return _connection.getInputStream();
        } finally {
            _connection = null;
        }
    }

    /**
     * Returns an output stream to the resource
     */
    @Override
    public OutputStream getOutputStream() throws IOException, SecurityException {
        throw new IOException( "Output not supported" );
    }

    /**
     * Deletes the given resource
     */
    @Override
    public boolean delete() throws SecurityException {
        throw new SecurityException( "Delete not supported" );
    }

    /**
     * Rename the given resource
     */
    @Override
    public boolean renameTo( Resource dest ) throws SecurityException {
        throw new SecurityException( "RenameTo not supported" );
    }

    /**
     * Returns a list of resource names contained in the given resource
     */
    @Override
    public String[] list() {
        return null;
    }

    /**
     * Returns the resource contained inside the current resource with the given
     * name
     */
    @Override
    public Resource addPath( String path ) throws IOException {
        if ( path == null ) return null;

        path = URIUtil.canonicalPath( path );

        return newResource( URIUtil.addPaths( _url.toExternalForm(), path ) );
    }

    @Override
    public String toString() {
        return _urlString;
    }

    @Override
    public int hashCode() {
        return _urlString.hashCode();
    }

    @Override
    public boolean equals( Object o ) {
        return o instanceof URLResource && _urlString.equals( ((URLResource) o)._urlString );
    }

    public boolean getUseCaches() {
        return _useCaches;
    }

    @Override
    public boolean isContainedIn( Resource containingResource ) throws MalformedURLException {
        return false; // TODO check this!
    }

    static void ignoreLog( Exception e ) {
        // LOG.warn( e, "IGNORED " );
    }
}
