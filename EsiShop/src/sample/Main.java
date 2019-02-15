package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        ResultSet s;
        Statement statement =db_connection.db_connectionSQLITE();
        s=statement.executeQuery ("SELECT * FROM `info` ");
        s.next();
        if(s.getInt("First")==0){
            db_connection.Host=s.getString("IPAdresse");
            db_connection.Port=s.getString("PORT");
            db_connection.NomBdd=s.getString("DBName");
            db_connection.username=s.getString("DBUserName");
            db_connection.password=s.getString("DBPWD");

            Parent root = FXMLLoader.load(getClass().getResource("/interfaces/login.fxml"));
            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root, 587, 415));
            db_connection.conn.close();
            primaryStage.show();}
        else {
            Parent root = FXMLLoader.load(getClass().getResource("/interfaces/premier1.fxml"));
            primaryStage.setTitle("Etap 1");
            primaryStage.setScene(new Scene(root, 600, 600));
            db_connection.conn.close();
            primaryStage.show();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
