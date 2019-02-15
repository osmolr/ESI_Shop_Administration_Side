package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by olr on 12/05/2017.
 */
public class Controller_Premier2 implements Initializable {
    @FXML TextField nom,prenom,adresse,ntel;
    Magasinier m=new Magasinier();

    public void Suivant(ActionEvent event) throws IOException {
        //valides les champs
        if (!m.ValiderTextField(new TextField[]{nom,prenom,adresse,ntel},new DatePicker[]{},new TextArea[]{})) return;

        data.nom=nom.getText();
        data.prenom=prenom.getText();
        data.adresse=adresse.getText();
        data.ntel=ntel.getText();
        m.openWindow(event,"/interfaces/premier3","Etape 3",600,600,true,false);
    }
    public void Precedent(ActionEvent event) throws IOException {
        m.openWindow(event,"/interfaces/premier1","Etape 1",600,600,true,false);
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{nom,prenom,adresse,ntel},new DatePicker[]{},new TextArea[]{});
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        m.MultiTextfieldNumiric(ntel);
    }
}
