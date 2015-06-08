package de.herrlock.manga.util.configuration;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Properties;

import de.herrlock.exceptions.InitializeException;
import de.herrlock.manga.util.ChapterPattern;
import de.herrlock.manga.util.Constants;

public abstract class Configuration {

    protected static URL _createUrl( Properties p ) {
        // get url
        URL url;
        try {
            String urlString = p.getProperty( Constants.PARAM_URL );
            if ( urlString == null || "".equals( urlString ) ) {
                throw new InitializeException( "url is not filled but required" );
            }
            url = new URL( urlString );
        } catch ( MalformedURLException ex ) {
            throw new InitializeException( "url is malformed", ex );
        }
        return url;
    }

    protected static Proxy _createProxy( Properties p ) {
        // get proxy
        try {
            String urlString = p.getProperty( Constants.PARAM_PROXY );
            if ( urlString != null && !"".equals( urlString ) ) {
                URL proxyUrl = new URL( urlString );
                String proxyHost = proxyUrl.getHost();
                int proxyPort = proxyUrl.getPort();
                InetSocketAddress sa = new InetSocketAddress( proxyHost, proxyPort );
                return new Proxy( Proxy.Type.HTTP, sa );
            }
        } catch ( MalformedURLException ex ) {
            throw new InitializeException( "proxy-url is malformed", ex );
        }
        return Proxy.NO_PROXY;
    }

    protected static ChapterPattern _createPattern( Properties p ) {
        String patternString = p.getProperty( Constants.PARAM_PATTERN );
        ChapterPattern result = null;
        if ( patternString != null && !"".equals( patternString ) ) {
            result = new ChapterPattern( patternString );
        }
        return result;
    }

    protected static File _createFolderwatch( Properties p ) {
        String fwPath = p.getProperty( Constants.PARAM_JDFW );
        return new File( fwPath );
    }

    protected Configuration() {
        // not used directly
    }
}
