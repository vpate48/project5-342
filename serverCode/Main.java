package sample;

import javafx.fxml.FXMLLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class Main extends Application {

    Button portNumber,quit;
    TextField text;
    Label serverStatus;
    BorderPane pane;

    private NetworkConnection  conn;
    private TextArea messages = new TextArea();


    private Parent createContent() {
        pane.setPrefSize(600, 600);
        pane.setPadding(new Insets(70));
        VBox root = new VBox(20, serverStatus, quit, portNumber,text);
        pane.setCenter(root);
        pane.setBottom(messages);
        return pane;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        primaryStage.setTitle("Server");
        serverStatus = new Label("Status: OFF");
        portNumber = new Button("Set Port Number");
        quit = new Button("Quit Server");
        text = new TextField();
        pane = new BorderPane();

        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();


        portNumber.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                try {
                    conn = createServer(Integer.parseInt(text.getText()));
                    text.clear();
                    conn.startConn();
                    serverStatus.setText("Status: ON");
                    messages.clear();
                    portNumber.setDisable(true);
                }
                catch(Exception e){
                    messages.appendText("Failed to start connection\n");
                    serverStatus.setText("Status: OFF");
                    portNumber.setDisable(false);
                }
            }
        });

        quit.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                try {
                    primaryStage.close();
                }
                catch(Exception e){
                    messages.appendText("Failed to close connection");
                }
            }
        });
    }


    private Server createServer(int port) {
        return new Server(port, data-> {
            Platform.runLater(()->{
                messages.appendText( data.toString() + "\n");
            });
        });
    }

}
