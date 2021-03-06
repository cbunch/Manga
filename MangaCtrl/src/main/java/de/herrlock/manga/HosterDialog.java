package de.herrlock.manga;

import java.util.Arrays;
import java.util.List;

import de.herrlock.javafx.AbstractApplication;
import de.herrlock.javafx.scene.SceneContainer;
import de.herrlock.manga.host.Hoster;
import de.herrlock.manga.host.Hosters;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TableViewBuilder;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HosterDialog extends AbstractApplication {

    public static void showHosterDialog() {
        new HosterDialog().start( new Stage() );
    }

    @Override
    public void start( final Stage stage ) {
        HosterSceneContainer container = new HosterSceneContainer();
        setScene( container );
        stage.setResizable( false );
        stage.initStyle( StageStyle.UTILITY );
        super.start( stage );

        container.table.setPrefWidth( container.nameColumn.getWidth() + container.urlColumn.getWidth() + 2 );
        stage.sizeToScene();

    }

    static class HosterSceneContainer extends SceneContainer {
        private final TableColumn<Hoster, Object> nameColumn;
        private final TableColumn<Hoster, Object> urlColumn;
        private final TableView<Hoster> table;

        public HosterSceneContainer() {
            this.nameColumn = TableColumnBuilder.<Hoster, Object> create()//
                .text( "Name" )//
                .editable( false )//
                .cellValueFactory( new PropertyValueFactory<Hoster, Object>( "name" ) )//
                .build();
            this.urlColumn = TableColumnBuilder.<Hoster, Object> create()//
                .text( "URL" )//
                .editable( false )//
                .cellValueFactory( new PropertyValueFactory<Hoster, Object>( "baseUrl" ) )//
                .build();
            final List<TableColumn<Hoster, Object>> columns = Arrays.asList( this.nameColumn, this.urlColumn );

            this.table = TableViewBuilder.<Hoster> create()//
                .columns( columns )//
                .editable( false )//
                .items( FXCollections.observableArrayList( Hosters.sortedValues() ) )//
                .build();

            HBox hbox = new HBox();
            hbox.setPadding( new Insets( 8 ) );
            hbox.getChildren().add( this.table );
            HBox.setHgrow( this.table, Priority.ALWAYS );
            Scene scene = new Scene( hbox );
            super.setScene( scene );
        }

        @Override
        public String getTitle() {
            return "Hosters";
        }
    }
}
