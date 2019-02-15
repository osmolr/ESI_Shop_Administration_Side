package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by olr on 12/05/2017.
 */
public class Controller_Premier3 {
    @FXML TextField user,mdp,hint;
    Magasinier m=new Magasinier();

    public void save(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (!m.ValiderTextField(new TextField[]{user,mdp,hint},new DatePicker[]{},new TextArea[]{})) return ;

        db_connection db=new db_connection();
        db.exécutionUpdate("UPDATE `login` SET `nomdutilisateur`= '"+user.getText()+"' ,`motdepasse`= '"+mdp.getText()+"' ,`hint`= '"+hint.getText()+"' ,`nom`= '"+data.nom+"' ,`prenom`= '"+data.prenom+"' ,`adresse`= '"+data.adresse+"' ,`ntel`= '"+data.ntel+"' ");

        m.openWindow(event,"/interfaces/accueil","Accueil",1366,768,true,true);
    }
    public void Precedent(ActionEvent event) throws IOException {
        m.openWindow(event,"/interfaces/premier2","Etape 2",600,600,true,false);
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{user,mdp,hint},new DatePicker[]{},new TextArea[]{});
    }
}
