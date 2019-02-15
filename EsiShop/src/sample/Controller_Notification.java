package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by olr on 18/03/2017.
 */
public class Controller_Notification implements Initializable {
    @FXML private TableView<tables.tn> tn;
    @FXML TableColumn<tables.tn,Integer> idnotification;
    @FXML TableColumn<tables.tn,String> fournisseur;
    @FXML TableColumn<tables.tn,String> produit;
    @FXML TableColumn<tables.tn,String> titre;
    @FXML TableColumn<tables.tn,String> description;
    @FXML Button btnvalider,PasserC,SuppN;
    ObservableList<tables.tn> list= FXCollections.observableArrayList();
    ResultSet s,ss;
    db_connection db=new db_connection();
    Magasinier m=new Magasinier();

    public void validernoti(ActionEvent event) throws SQLException {
        //oblier de selectione un client
        if (m.OblierSelectione(tn,"une notification")) return;

        if(!m.ConfirmationSupp("valider","Validation","la reception de cette commande?"))return;

        //recuperer les information de la commande
        s=db.exécutionQuery("SELECT * FROM `notification`,`produit` WHERE `notification`.`IDProduit`=`produit`.`IDProduit` AND `IDNotification`="+tn.getSelectionModel().getSelectedItem().getIdnotification());
        s.next();
        int quantite =s.getInt("quantite");
        int idproduit=s.getInt("IDProduit");
        s=db.exécutionQuery("SELECT * FROM `achat-commande` WHERE `IDProduit`="+idproduit+" AND `Etat`='en route'");
        s.next();
        int idachatcommande=s.getInt("IDAchat_Commande");
        //ajouter la nouvelle contite
        db.exécutionUpdate("UPDATE `produit` SET  `quantite`="+String.valueOf(quantite+s.getInt("quantite")));
        //changer l'etat de la commande
        db.exécutionUpdate("UPDATE `achat-commande` SET `Etat`='achat' WHERE `IDAchat_Commande`="+idachatcommande);
        //supprimer la notification
        db.exécutionUpdate("DELETE FROM `notification` WHERE `IDNotification`="+tn.getSelectionModel().getSelectedItem().getIdnotification());
        //mise a jour du tableau
        list.remove(tn.getSelectionModel().getSelectedIndex());
    }
    public void passercommande(Event event) throws SQLException, IOException {
        //oblier de selectione un client
        if (m.OblierSelectione(tn,"une notification")) return;

        s=db.exécutionQuery("SELECT * FROM `notification` WHERE `IDNotification`="+tn.getSelectionModel().getSelectedItem().getIdnotification());
        if(s.next()) {
            data.idproduit=s.getInt(3);
            data.idnotification=s.getInt("IDNotification");
        }
        data.openbynoti=1;

        m.openWindow(event,"/interfaces/Achat_Commande","Achat & Commande",1046, 641,false,false);
    }
    public  void suppr(ActionEvent event) throws SQLException {
        //oblier de selectione un client
        if (m.OblierSelectione(tn,"une notification")) return;

        //si vous avez pas selectione une notification
        if (m.OblierSelectione(tn,"notification")) return;

        s=db.exécutionQuery("SELECT * FROM `notification` WHERE `IDNotification`="+tn.getSelectionModel().getSelectedItem().getIdnotification());
        s.next();
        //mettre le produit ne fait pas des notifications
        db.exécutionUpdate("UPDATE `produit` SET `notifiable` = '0' WHERE `produit`.`IDProduit` ="+s.getInt("IDProduit"));
        //supprimer la notification
        db.exécutionUpdate("DELETE FROM `notification` WHERE `notification`.`IDNotification`="+tn.getSelectionModel().getSelectedItem().getIdnotification());
        list.remove(tn.getSelectionModel().getSelectedIndex());
    }
    public void DefaultTheme(Event event){
        tn.setStyle("");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    //desactiver les button selon le type du notification
        tn.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
          if (tn.getSelectionModel().getSelectedItem().getTitre().equals("stock min")){
              btnvalider.setDisable(true);
              SuppN.setDisable(false);
              PasserC.setDisable(false);
          }else if (tn.getSelectionModel().getSelectedItem().getTitre().equals("en route")){
              btnvalider.setDisable(false);
              SuppN.setDisable(true);
              PasserC.setDisable(true);
          }else {
              btnvalider.setDisable(true);
              PasserC.setDisable(true);
              SuppN.setDisable(false);
          }
        });
        //intialiser le tableau des notification
        s=db.exécutionQuery("SELECT * FROM `notification`,`fournisseur`,`produit`,`marque`,`article`,`couleur` WHERE `notification`.`IDFournisseur`=`fournisseur`.`IDFournisseur` AND `notification`.`IDProduit`=`produit`.`IDProduit` AND `produit`.`IDMarque`=`marque`.`IDMarque` AND `produit`.`IDArticle`=`article`.`IDArticle` AND `produit`.`IDCouleur`=`couleur`.`IDCouleur`");
        try {
            while (s.next())
            {
                list.add(new tables.tn(s.getInt("IDNotification"),s.getString("nom")+" "+s.getString("prenom"),s.getString("type-article")+" "+s.getString("nom-marque")+" "+s.getString("nom-couleur")+" "+s.getString("taille"),s.getString("titre"),s.getString("description")));
            }
        } catch (SQLException e) {}
        idnotification.setCellValueFactory(new PropertyValueFactory<tables.tn,Integer>("idnotification"));
        fournisseur.setCellValueFactory(new PropertyValueFactory<tables.tn,String>("fournisseur"));
        produit.setCellValueFactory(new PropertyValueFactory<tables.tn,String>("produit"));
        titre.setCellValueFactory(new PropertyValueFactory<tables.tn,String>("titre"));
        description.setCellValueFactory(new PropertyValueFactory<tables.tn,String>("description"));
        tn.setItems(list);
    }
}
