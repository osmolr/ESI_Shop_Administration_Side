package sample;

import com.jfoenix.controls.JFXButton;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Created by olr on 13/03/2017.
 */
public class Controlleraccueil{
    Magasinier m=new Magasinier();
    public RotateTransition tr;
    public RotateTransition tr2;
    public RotateTransition tr3;


    public void a(ActionEvent event) throws Exception {
        m.openWindow(event,"/interfaces/sample","Espace vente", 1366,768 ,true,true);
    }
    public void b(ActionEvent event) throws Exception {
        m.openWindow(event,"/interfaces/gestion_client","Gestion clients", 1366,768 ,true,true);
    }
    public void c(ActionEvent event) throws Exception {
        m.openWindow(event,"/interfaces/gestion_produit","Gestion produits", 1366,768 ,true,true);
    }
    public void d(ActionEvent event) throws Exception {
        m.openWindow(event,"/interfaces/gestion_fournisseur","Gestion Fournisseur", 1366,768 ,true,true);
    }
    public void e(ActionEvent event) throws Exception {
        m.openWindow(event,"/interfaces/Marque_Article","Marque/Article/Couleur", 572, 570 ,true,false);
    }
    public void f(ActionEvent event) throws Exception {
        m.openWindow(event,"/interfaces/Achat_Commande","Achat/Commande", 1046, 641 ,true,false);
    }
    public void p(ActionEvent event) throws Exception {
        m.openWindow(event,"/interfaces/parametre","Parametre", 530, 387 ,true,false);
    }
    public void h(ActionEvent event) throws Exception {
        m.openWindow(event,"/interfaces/historique_des_ventes","historique des ventes", 683, 687 ,true,false);
    }
    public void LogOut(ActionEvent event) throws IOException {
        m.openWindow(event,"/interfaces/login","Login", 575, 415,true,false);
    }
    @FXML
    private  void rotate(MouseEvent event) {
        tr=new RotateTransition(Duration.millis(70), (JFXButton) event.getSource());
        tr2=new RotateTransition(Duration.millis(70), (JFXButton) event.getSource());
        tr3=new RotateTransition(Duration.millis(70), (JFXButton) event.getSource());
        tr.setCycleCount(1);
        tr.setFromAngle(0);
        tr.setToAngle(5);
        tr2.setCycleCount(1);
        tr2.setFromAngle(5);
        tr2.setToAngle(-5);
        tr3.setCycleCount(1);
        tr3.setFromAngle(-5);
        tr3.setToAngle(0);
        tr.setOnFinished(e->{tr2.play();});
        tr2.setOnFinished(e->{tr3.play();});
        //tr3.setOnFinished(e->{tr3.stop();});
        tr.setAutoReverse(true);
        tr.playFromStart();
    }
    @FXML
    private void rotateStop(MouseEvent event) {
        tr=new RotateTransition(Duration.millis(70), (JFXButton) event.getSource());
        tr.setToAngle(0);
        tr.play();
    }

}
