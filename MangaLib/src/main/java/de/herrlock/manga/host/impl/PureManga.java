package de.herrlock.manga.host.impl;

import java.io.IOException;
import java.net.URL;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.auto.service.AutoService;

import de.herrlock.manga.downloader.pmc.EntryList;
import de.herrlock.manga.host.ChapterList;
import de.herrlock.manga.host.HosterImpl;
import de.herrlock.manga.host.annotations.ChapterListDetails;
import de.herrlock.manga.host.annotations.Details;
import de.herrlock.manga.util.configuration.DownloadConfiguration;

@AutoService( HosterImpl.class )
@Details( name = "PureManga", baseUrl = "http://www.pure-manga.org/" )
public final class PureManga extends HosterImpl {

    @Override
    public ChapterList getChapterList( final DownloadConfiguration conf ) throws IOException {
        return new MangaTubeChapterList( conf );
    }

}

@ChapterListDetails( reversed = true )
final class PureMangaChapterList extends ChapterList {
    private final String name;

    public PureMangaChapterList( final DownloadConfiguration conf ) throws IOException {
        super( conf );
        Document document = getDocument( conf.getUrl() );

        this.name = document.select( "#content h2.titlebg" ).first().text();

        Elements tr = document.getElementsByClass( "element" );
        for ( Element e : tr ) {
            Element link = e.select( "div>a" ).first();

            String[] chapterNrAndName = link.text().split( ":" );
            String number = chapterNrAndName[0].split( " " )[1];

            URL chapterUrl = new URL( conf.getUrl(), link.attr( "href" ) );

            super.addChapter( number, chapterUrl );
        }
    }

    @Override
    public String getMangaName() {
        return this.name;
    }

    @Override
    public URL imgLink( final URL url ) throws IOException {
        String src = getDocument( url ).select( "#page > .inner > a > img" ).first().attr( "src" );
        return new URL( url, src );
    }

    @Override
    protected EntryList<Integer, URL> _getAllPageURLs( final URL url ) throws IOException {
        EntryList<Integer, URL> result = new EntryList<>();
        Elements li = getDocument( url ).select( ".dropdown_right > ul > li" );
        for ( Element e : li ) {
            Element link = e.getElementsByTag( "a" ).first();
            int number = Integer.parseInt( link.text().substring( 6 ) );
            URL absUrl = new URL( url, link.attr( "href" ) );
            result.addEntry( number, absUrl );
        }
        return result;
    }

}
