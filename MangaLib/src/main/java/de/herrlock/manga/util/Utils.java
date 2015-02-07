package de.herrlock.manga.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import de.herrlock.manga.connection.ConnectionFactory;
import de.herrlock.manga.connection.DirectConnectionFactory;
import de.herrlock.manga.connection.ProxyConnectionFactory;

public final class Utils {

    private static Map<String, String> arguments;
    private static ConnectionFactory conFactory;

    public static Map<String, String> getArguments() {
        if ( arguments == null )
            throw new RuntimeException( "arguments not yet initialized" );
        return arguments;
    }

    public static void setArguments( Properties p ) {
        String[] requiredParameters = new String[] {
            Constants.PARAM_URL
        };
        for ( String s : requiredParameters ) {
            String value = p.getProperty( s );
            if ( value == null || "".equals( value ) ) {
                throw new RuntimeException( "Please fill the field \"" + s + "\" in the file "
                    + new File( Constants.SETTINGS_FILE ).getAbsolutePath() );
            }
        }

        Map<String, String> m = new HashMap<>();
        for ( String s : p.stringPropertyNames() ) {
            m.put( s, p.getProperty( s ) );
        }
        arguments = Collections.unmodifiableMap( m );

        String host = m.get( Constants.PARAM_PROXY_HOST );
        String port = m.get( Constants.PARAM_PROXY_PORT );
        if ( host != null && !"".equals( host ) && port != null && !"".equals( port ) ) {
            String timeout = m.get( Constants.PARAM_TIMEOUT );
            conFactory = new ProxyConnectionFactory( timeout, host, port );
        } else {
            conFactory = new DirectConnectionFactory( port );
        }
    }

    public static URLConnection getConnection( URL url ) throws IOException {
        return conFactory.getConnection( url );
    }

    public static Document getDocument( URL url ) throws IOException {
        URLConnection con = getConnection( url );
        List<String> list = readStream( con.getInputStream() );
        StringBuilder sb = new StringBuilder();
        for ( String s : list ) {
            sb.append( s );
        }
        return Jsoup.parse( sb.toString() );
    }

    public static List<String> readStream( InputStream in ) throws IOException {
        List<String> list = new ArrayList<>();
        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( in, StandardCharsets.UTF_8 ) ) ) {
            String nextline = null;
            while ( ( nextline = reader.readLine() ) != null ) {
                list.add( nextline );
            }
        }
        return list;
    }

    public static URL getMangaURL() {
        try {
            String _url = arguments.get( Constants.PARAM_URL );
            if ( !_url.startsWith( "http" ) )
                _url = "http://" + _url;
            return new URL( _url );
        } catch ( MalformedURLException ex ) {
            throw new RuntimeException( ex );
        }
    }

    public static String getPattern() {
        return arguments.get( Constants.PARAM_PATTERN );
    }

    public static void startAndWaitForThreads( Collection<? extends Thread> threads ) {
        for ( Thread t : threads ) {
            t.start();
        }
        try {
            for ( Thread t : threads ) {
                t.join();
            }
        } catch ( InterruptedException ex ) {
            throw new RuntimeException( ex );
        }
    }

    private Utils() {
        // not called
    }

}