package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by olr on 15/03/2017.
 */
public class loginController {
    @FXML TextField nu,mp;
    @FXML Label vb;
    ResultSet s;
    Magasinier m=new Magasinier();

    public void login (ActionEvent event) throws SQLException, IOException {
        //valider les champs
        if (!m.ValiderTextField(new TextField[]{nu,mp},new DatePicker[]{},new TextArea[]{})) return;

        db_connection db=new db_connection();
        s= db.exécutionQuery("SELECT * FROM `login` WHERE `nomdutilisateur`='"+nu.getText()+"' AND `motdepasse`='"+mp.getText()+"'");
        if (s.next()) {
                m.openWindow(event,"/interfaces/accueil","Accueil",1366,786,true,true);
        }else {
            m.alert(Alert.AlertType.ERROR,"Identification Incorrect","le nom d'utilisateur ou le mot de passe est incorrect");
            m.viderchamp(nu,mp);
        }
     }
    public void loginbtn (Event event) throws SQLException, IOException {
        if (event instanceof KeyEvent && ((KeyEvent)event).getCode().equals(KeyCode.ENTER)){
            //valider les champs
            if (!m.ValiderTextField(new TextField[]{nu,mp},new DatePicker[]{},new TextArea[]{})) return;

            db_connection db=new db_connection();
            s= db.exécutionQuery("SELECT * FROM `login` WHERE `nomdutilisateur`='"+nu.getText()+"' AND `motdepasse`='"+mp.getText()+"'");
            if (s.next()) {
                m.openWindow(event,"/interfaces/accueil","Accueil",1366,786,true,true);
            }else {
                m.alert(Alert.AlertType.ERROR,"Identification Incorrect","le nom d'utilisateur ou le mot de passe est incorrect");
                m.viderchamp(nu,mp);
            }
    }}
    public void check(ActionEvent event){
        m.CheckDb(event,vb);
    }
    public void mdpo(ActionEvent event) throws SQLException {
        db_connection db=new db_connection();
        s=db.exécutionQuery("SELECT * FROM `login`");
        if (s.next()) if (s.getString("nomdutilisateur").equals(nu.getText())){

             mp.setPromptText(s.getString("hint"));
        }
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{nu,mp},new DatePicker[]{},new TextArea[]{});
    }
}


