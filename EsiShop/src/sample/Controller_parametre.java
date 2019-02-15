package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by olr on 26/03/2017.
 */
public class Controller_parametre {
    @FXML TextField nu,mp,nmp,hint;
    db_connection db=new db_connection();
    Magasinier m=new Magasinier();
    ResultSet s;

    public void Back(ActionEvent event) throws IOException {
        m.openWindow(event,"/interfaces/accueil","Accueil",1366,768,true,true);
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{nu,mp,nmp},new DatePicker[]{},new TextArea[]{});
    }
    public void sauvegarder(ActionEvent event) throws SQLException {
        //valides les champs
        if (!m.ValiderTextField(new TextField[]{nu,mp,nmp},new DatePicker[]{},new TextArea[]{})) return;

        s=db.exécutionQuery("SELECT * FROM `login` WHERE `IDLogin`=1");
        s.next();
        if ( s.getString(2).equals(nu.getText()) && s.getString(3).equals(mp.getText())){
            db.exécutionUpdate("UPDATE `login` SET `motdepasse` = '"+nmp.getText()+"', `hint` = '"+hint.getText()+"' WHERE `login`.`IDLogin` = 1");
        }else if(!s.getString(3).equals(mp.getText())){
            m.alert(Alert.AlertType.ERROR,"Erreur","l'ancient mot de passe est incorrect");
            m.viderchamp(nu,mp,nmp,hint);
            return;
        }else if(!s.getString(2).equals(nu.getText())){
        m.alert(Alert.AlertType.ERROR,"Erreur","le nom d'utilisateur est incorrect");
        m.viderchamp(nu,mp,nmp,hint);
        return;
    }
        m.viderchamp(nu,mp,nmp,hint);
        m.alert(Alert.AlertType.INFORMATION,"Modification","Modification sauvegardé");
    }

}
