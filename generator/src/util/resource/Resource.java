package util.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.Normalizer;

import util.Loader;
import util.StringUtil;

/**
 * Abstract resource class.
 */
@SuppressWarnings("ALL")
public abstract class Resource implements ResourceFactory {
    static boolean __defaultUseCaches = true;

    /**
     * Change the default setting for url connection caches. Subsequent
     * URLConnections will use this default.
     */
    public static void setDefaultUseCaches( boolean useCaches ) {
        __defaultUseCaches = useCaches;
    }

    public static boolean getDefaultUseCaches() {
        return __defaultUseCaches;
    }

    /**
     * Construct a resource from a uri.
     *
     * @param uri
     *            A URI.
     *
     * @return A Resource object.
     *
     * @throws IOException
     *             Problem accessing URI
     */
    public static Resource newResource( URI uri ) throws IOException {
        return newResource( uri.toURL() );
    }

    /**
     * Construct a resource from a url.
     *
     * @param url
     *            A URL.
     *
     * @return A Resource object.
     *
     * @throws IOException
     *             Problem accessing URL
     */
    public static Resource newResource( URL url ) throws IOException {
        return newResource( url, __defaultUseCaches );
    }

    /**
     * Construct a resource from a url.
     *
     * @param url
     *            the url for which to make the resource
     * @param useCaches
     *            true enables URLConnection caching if applicable to the type
     *            of resource
     */
    static Resource newResource( URL url, boolean useCaches ) {
        if ( url == null ) return null;

        String url_string = url.toExternalForm();
        if ( url_string.startsWith( "file:" ) ) {
            try {
                return new FileResource( url );
            } catch ( Exception e ) {
                return new BadResource( url, e.toString() );
            }
        } else if ( url_string.startsWith( "jar:file:" ) ) {
            return new JarFileResource( url, useCaches );
        } else if ( url_string.startsWith( "jar:" ) ) { return new JarResource( url, useCaches ); }

        return new URLResource( url, null, useCaches );
    }

    /**
     * Construct a resource from a string.
     *
     * @param resource
     *            A URL or filename.
     *
     * @return A Resource object.
     */
    public static Resource newResource( String resource ) throws IOException {
        return newResource( resource, __defaultUseCaches );
    }

    /**
     * Construct a resource from a string.
     *
     * @param resource
     *            A URL or filename.
     * @param useCaches
     *            controls URLConnection caching
     *
     * @return A Resource object.
     */
    public static Resource newResource( String resource, boolean useCaches ) throws IOException {
        URL url;
        resource = Normalizer.normalize( StringUtil.empty( resource, "" ), Normalizer.Form.NFKC );

        try {
            // Try to format as a URL?
            url = new URL( resource );
        } catch ( MalformedURLException e ) {
            if ( !resource.startsWith( "ftp:" ) && !resource.startsWith( "file:" ) && !resource.startsWith( "jar:" ) ) {
                try {
                    // It's a file.
                    if ( resource.startsWith( "./" ) ) resource = resource.substring( 2 );

                    File file = new File( resource ).getCanonicalFile();
                    url = Resource.toURL( file );

                    URLConnection connection = url.openConnection();
                    connection.setUseCaches( useCaches );
                    return new FileResource( url, connection, file );
                } catch ( Exception e2 ) {
                    throw e;
                }
            } else {
                System.err.println( "Bad Resource: " + resource );
                throw e;
            }
        }

        return newResource( url );
    }

    public static Resource newResource( File file ) throws IOException {
        file = file.getCanonicalFile();
        URL url = Resource.toURL( file );

        URLConnection connection = url.openConnection();
        return new FileResource( url, connection, file );
    }

    /**
     * Construct a system resource from a string. The resource is tried as
     * classloader resource before being treated as a normal resource.
     *
     * @param resource
     *            Resource as string representation
     *
     * @return The new Resource
     *
     * @throws IOException
     *             Problem accessing resource.
     */
    public static Resource newSystemResource( String resource ) throws IOException {
        URL url = null;
        // Try to format as a URL?
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if ( loader != null ) {
            try {
                url = loader.getResource( resource );
                if ( url == null && resource.startsWith( "/" ) ) url = loader.getResource( resource.substring( 1 ) );
            } catch ( IllegalArgumentException e ) {
                // Catches scenario where a bad Windows path like "C:\dev" is
                // improperly escaped, which various downstream classloaders
                // tend to have a problem with
                url = null;
            }
        }
        if ( url == null ) {
            loader = Resource.class.getClassLoader();
            if ( loader != null ) {
                url = loader.getResource( resource );
                if ( url == null && resource.startsWith( "/" ) ) url = loader.getResource( resource.substring( 1 ) );
            }
        }

        if ( url == null ) {
            url = ClassLoader.getSystemResource( resource );
            if ( url == null && resource.startsWith( "/" ) ) url = ClassLoader.getSystemResource( resource.substring( 1 ) );
        }

        if ( url == null ) return null;

        return newResource( url );
    }

    /**
     * Find a classpath resource.
     */
    public static Resource newClassPathResource( String resource ) {
        return newClassPathResource( resource, true, false );
    }

    /**
     * Find a classpath resource. The {@link Class#getResource(String)} method
     * is used to lookup the resource. If it is not found, then the
     * {@link Loader#getResource(Class, String, boolean)} method is used. If it
     * is still not found, then {@link ClassLoader#getSystemResource(String)} is
     * used. Unlike {@link ClassLoader#getSystemResource(String)} this method
     * does not check for normal resources.
     *
     * @param name
     *            The relative name of the resource
     * @param useCaches
     *            True if URL caches are to be used.
     * @param checkParents
     *            True if forced searching of parent Classloaders is performed
     *            to work around loaders with inverted priorities
     *
     * @return Resource or null
     */
    public static Resource newClassPathResource( String name, boolean useCaches, boolean checkParents ) {
        URL url = Resource.class.getResource( name );

        if ( url == null ) url = Loader.getResource( Resource.class, name, checkParents );
        if ( url == null ) return null;
        return newResource( url, useCaches );
    }

    public static boolean isContainedIn( Resource r, Resource containingResource ) throws MalformedURLException {
        return r.isContainedIn( containingResource );
    }

    @SuppressWarnings("FinalizeDoesntCallSuperFinalize")
    @Override
    protected void finalize() {
        release();
    }

    public abstract boolean isContainedIn( Resource r ) throws MalformedURLException;

    /**
     * Release any temporary resources held by the resource.
     */
    public abstract void release();

    /**
     * Returns true if the respresened resource exists.
     */
    public abstract boolean exists();

    /**
     * Returns true if the respresenetd resource is a container/directory. If
     * the resource is not a file, resources ending with "/" are considered
     * directories.
     */
    public abstract boolean isDirectory();

    /**
     * Returns the last modified time
     */
    public abstract long lastModified();

    /**
     * Return the length of the resource
     */
    public abstract long length();

    /**
     * Returns an URL representing the given resource
     */
    public abstract URL getURL();

    /**
     * Returns an URI representing the given resource
     */
    public URI getURI() {
        try {
            return getURL().toURI();
        } catch ( Exception e ) {
            throw new RuntimeException( e );
        }
    }

    /**
     * Returns an File representing the given resource or NULL if this is not
     * possible.
     */
    public abstract File getFile() throws IOException;

    /**
     * Returns the name of the resource
     */
    public abstract String getName();

    /**
     * Returns an input stream to the resource
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Returns an output stream to the resource
     */
    public abstract OutputStream getOutputStream() throws IOException, SecurityException;

    /**
     * Deletes the given resource
     */
    public abstract boolean delete() throws SecurityException;

    /**
     * Rename the given resource
     */
    public abstract boolean renameTo( Resource dest ) throws SecurityException;

    /**
     * Returns a list of resource names contained in the given resource The
     * resource names are not URL encoded.
     */
    public abstract String[] list();

    /**
     * Returns the resource contained inside the current resource with the given
     * name.
     *
     * @param path
     *            The path segment to add, which should be encoded by the encode
     *            method.
     */
    public abstract Resource addPath( String path ) throws IOException;

    /**
     * Get a resource from withing this resource.
     * <p>
     * This method is essentially an alias for {@link #addPath(String)}, but
     * without checked exceptions. This method satisfied the
     * {@link ResourceFactory} interface.
     *
     * @see ResourceFactory#getResource(String)
     */
    @Override
    public Resource getResource( String path ) {
        try {
            return addPath( path );
        } catch ( Exception e ) {
            return null;
        }
    }

    /**
     * Generate a properly encoded URL from a {@link File} instance.
     *
     * @param file
     *            Target file.
     *
     * @return URL of the target file.
     */
    public static URL toURL( File file ) throws MalformedURLException {
        return file.toURI().toURL();
    }
}
