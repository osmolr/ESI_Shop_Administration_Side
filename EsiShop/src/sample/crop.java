package sample;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;


public class crop  {

    static RubberBandSelection rubberBandSelection;
    static  ImageView imageView;
    static Stage primaryStage;
    static ScrollPane scrollPane;
    static double x,y;


    public void start(Stage primaryStage,String path) {

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Image Crop");
        primaryStage.setMaximized(true);
        BorderPane root = new BorderPane();
        // container for image layers
        scrollPane = new ScrollPane();
        // image layer: a group of images
        Group imageLayer = new Group();
        // load the image
        Image image = new Image("file:///"+path);
        // the container for the image as a javafx node
        imageView = new ImageView( image);
        // add image to layer
        imageLayer.getChildren().add( imageView);
        // use scrollpane for image view in case the image is large
        scrollPane.setContent(imageLayer);
        // put scrollpane in scene
        root.setCenter(scrollPane);
        // rubberband selection
        rubberBandSelection = new RubberBandSelection(imageLayer);
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }


    public static class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();

        Group group;


        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection( Group group) {

            this.group = group;

            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }


        public static void crop( Bounds bounds) {
            File file = new File("a.jpg");
            if (file == null)
                return;
            int width = (int) bounds.getWidth();
            int height = (int) bounds.getHeight();

            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));

            WritableImage wi = new WritableImage( width, height);
            imageView.snapshot(parameters, wi);

            BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
            BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

            Graphics2D graphics = bufImageRGB.createGraphics();
            graphics.drawImage(bufImageARGB, 0, 0, null);

            try {

                ImageIO.write(bufImageRGB, "jpg", file);

                System.out.println( "Image saved to " + file.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }

            graphics.dispose();

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                // remove old rect
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);

                // prepare new drag operation
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add( rect);

            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;
                if( offsetX > 0 && offsetY >0 )
                {
                    if (offsetX>offsetY){
                        rect.setWidth( offsetX);
                        rect.setHeight( offsetX);}
                    else if (offsetX<offsetY){
                        rect.setWidth( offsetY);
                        rect.setHeight( offsetY);}
                    else if (offsetX==offsetY){
                        rect.setWidth( offsetX);
                        rect.setHeight( offsetY);}
                }
                if(offsetX>600)
                    x=x+0.01;
                scrollPane.setHvalue(x);
                if(offsetY>400)
                    y=y+0.01;
                scrollPane.setVvalue(y);

                if(offsetX>900 && offsetY>600){
                    x=x+0.01;
                    scrollPane.setHvalue(x);
                    y=y+0.01;
                    scrollPane.setVvalue(y);
                }
            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if( event.isSecondaryButtonDown()) return;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("enregistre");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/interfaces/sample.css").toExternalForm());
                dialogPane.getStyleClass().add("sample");
                dialogPane.setHeaderText(null);
                dialogPane.setContentText("Voulez vous enregistrer cette photos ???");
                Optional<ButtonType> answer =alert.showAndWait();

                if (answer.get()==ButtonType.OK) {
                    crop(rubberBandSelection.getBounds());
                    ((Node) event.getSource()).getScene().getWindow().hide();
                }
                else{
                    x=y=0;
                    rect.setX(0);
                    rect.setY(0);
                    rect.setWidth(0);
                    rect.setHeight(0);
                    group.getChildren().remove( rect);
                }
            }
        };
        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;


        }
    }
}