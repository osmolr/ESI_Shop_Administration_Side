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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
/**
 * Created by olr on 13/03/2017.
 */
public class Controller_marque_article implements Initializable {
    @FXML TextField tf;
    @FXML public ComboBox<String> chois;
    @FXML private TableView<tables.tm> tm;
    @FXML TableColumn<tables.tm, Integer> idmarque;
    @FXML TableColumn<tables.tm, String> marque;
    @FXML private TableView<tables.ta> ta;
    @FXML TableColumn<tables.ta, Integer> idarticle;
    @FXML TableColumn<tables.ta, String> article;
    @FXML private TableView<tables.tco> tco;
    @FXML TableColumn<tables.ta, Integer> idcouleur;
    @FXML TableColumn<tables.ta, String> couleur;
    @FXML Button BtnBack,BtnSupprimer;
    ObservableList<tables.tm> list = FXCollections.observableArrayList();
    ObservableList<tables.ta> list1 = FXCollections.observableArrayList();
    ObservableList<tables.tco> list2 = FXCollections.observableArrayList();
    db_connection db=new db_connection();
    ResultSet s;
    Magasinier m=new Magasinier();

    public void add(ActionEvent event) throws SQLException {
        //valides les champs
        if (!m.ValiderTextField(new TextField[]{},new DatePicker[]{},new TextArea[]{},chois)) return;
        if (!m.ValiderTextField(new TextField[]{tf},new DatePicker[]{},new TextArea[]{})) return;

        switch(chois.getSelectionModel().getSelectedItem()) {
            case "Marque":{   db.exécutionUpdate("INSERT INTO `marque` (`nom-marque`) VALUES ('" + tf.getText() + "')");
                list.clear();
                s = db.exécutionQuery("SELECT * FROM `marque` ORDER BY `IDMarque` ASC");
                try {
                    while (s.next()) {
                    list.add(new tables.tm(s.getInt("IDMarque"), s.getString("nom-marque")));

                }
                } catch (SQLException e) {
                e.printStackTrace();}
                m.alert(Alert.AlertType.INFORMATION,"","marque ajoutée");
                tf.setText("");}
            case "Article":{db.exécutionUpdate("INSERT INTO `article` (`type-article`) VALUES ('" + tf.getText() + "')");
                list1.clear();
                s = db.exécutionQuery("SELECT * FROM `article` ORDER BY `IDArticle` ASC");
                try {
                    while (s.next()) {
                    list1.add(new tables.ta(s.getInt("IDArticle"), s.getString("type-article")));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();}
                m.alert(Alert.AlertType.INFORMATION,"","catégorie ajouté");
                tf.setText("");}
            case "Couleur":{db.exécutionUpdate("INSERT INTO `couleur` (`nom-couleur`) VALUES ('" + tf.getText() + "')");
                list2.clear();
                s = db.exécutionQuery("SELECT * FROM `couleur` ORDER BY `IDCouleur` ASC");
                try {
                    while (s.next()) {
                    list2.add(new tables.tco(s.getInt("IDCouleur"), s.getString("nom-couleur")));m.alert(Alert.AlertType.INFORMATION,"","marque ajouté");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();}
                m.alert(Alert.AlertType.INFORMATION,"","couleur ajoutée");
                tf.setText("");}
            }
            //fermé la fenetre si on a ajouter un marque --ouvert par fournisseur
            if(data.openbyfournisseur==1){
                ((Node) event.getSource()).getScene().getWindow().hide();
                data.openbyfournisseur=0;
            }
            chois.getSelectionModel().selectFirst();
    }
    public void supp(ActionEvent event)  {
        //valides les champs
        if (!m.ValiderTextField(new TextField[]{},new DatePicker[]{},new TextArea[]{},chois)) return;

        if(chois.getSelectionModel().getSelectedIndex()==1) {
                if (m.OblierSelectione(tm,"une marque")) return;

                try {
                    if(!m.ConfirmationSupp("supprimer","Suppression","cette marque"))return;
                    db.exécutionUpdate("DELETE FROM `marque` WHERE `IDMarque` ='" + tm.getSelectionModel().getSelectedItem().getIdmarque() + "'");
                    list.remove(tm.getSelectionModel().getSelectedIndex());
                    m.alert(Alert.AlertType.INFORMATION,"suppression","marque supprimée");
                }catch (SQLException e) {
                    m.alert(Alert.AlertType.ERROR, "suppression Imposssible", "vous ne pouvez pas supprimer cette marque car elle exist un fournisseur ou un produit qui l'utilise");
                }

            }

            else if(chois.getSelectionModel().getSelectedIndex()==2) {
                if (m.OblierSelectione(ta,"un catégorie")) return;

                try {
                    if(!m.ConfirmationSupp("supprimer","Suppression","ce categorie"))return;
                    db.exécutionUpdate("DELETE FROM `article` WHERE `IDArticle` ='" + ta.getSelectionModel().getSelectedItem().getIdarticle() + "'");
                    list1.remove(ta.getSelectionModel().getSelectedIndex());
                    m.alert(Alert.AlertType.INFORMATION,"suppression","catégorie supprimé");
                }catch (SQLException e){
                    m.alert(Alert.AlertType.ERROR,"suppression Imposssible","vous ne pouvez pas supprimer ce Catégorie car il y a des produit qui l'utilise");
                }
            }

            else if(chois.getSelectionModel().getSelectedIndex()==3){
                if (m.OblierSelectione(tco,"une couleur")) return;

                try {
                    if(!m.ConfirmationSupp("supprimer","Suppression","cette couleur"))return;
                    db.exécutionUpdate("DELETE FROM `couleur` WHERE `IDCouleur` ='"+tco.getSelectionModel().getSelectedItem().getIdcouleur()+"'");
                    list2.remove(tco.getSelectionModel().getSelectedIndex());
                    m.alert(Alert.AlertType.INFORMATION,"suppression","couleur supprimée");
                }catch (SQLException e){
                    m.alert(Alert.AlertType.ERROR,"suppression Imposssible","vous ne pouvez pas supprimer cette couleur car il y a des produit qui l'utilise");
                }
            chois.getSelectionModel().selectFirst();
    }}
    public void Back(ActionEvent event) throws IOException {
        m.openWindow(event,"/interfaces/accueil","Accueil",1366,768,true,true);
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{tf},new DatePicker[]{},new TextArea[]{},chois);
        tco.setStyle("");
        ta.setStyle("");
        tm.setStyle("");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //open by fournisseur
        if (data.openbyfournisseur==1) {
            m.Desactive(new TextField[]{},new DatePicker[]{},new TextArea[]{},new Button[]{BtnBack,BtnSupprimer});
            m.DesactiveTableau(ta,tco);
            chois.getSelectionModel().select(1);
            chois.setPromptText("");
        }
        else {
            tf.requestFocus();
            chois.getSelectionModel().selectFirst();
        }

        //initialisation
        s=db.exécutionQuery("SELECT * FROM `marque` ORDER BY `IDMarque` ASC");
        try {
            while (s.next()) {
                list.add(new tables.tm(s.getInt("IDMarque"), s.getString("nom-marque")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        idmarque.setCellValueFactory(new PropertyValueFactory<tables.tm,Integer>("idmarque"));
        marque.setCellValueFactory(new PropertyValueFactory<tables.tm,String>("marque"));
        tm.setItems(list);

        s=db.exécutionQuery("SELECT * FROM `article` ORDER BY `IDArticle` ASC");
        try {
            while (s.next()) {
                list1.add(new tables.ta(s.getInt("IDArticle"), s.getString("type-article")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        idarticle.setCellValueFactory(new PropertyValueFactory<tables.ta,Integer>("idarticle"));
        article.setCellValueFactory(new PropertyValueFactory<tables.ta,String>("article"));
        ta.setItems(list1);

        s=db.exécutionQuery("SELECT * FROM `couleur` ORDER BY `IDCouleur` ASC");
        try {
            while (s.next()) {
                list2.add(new tables.tco(s.getInt("IDCouleur"), s.getString("nom-couleur")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        idcouleur.setCellValueFactory(new PropertyValueFactory<tables.ta,Integer>("idcouleur"));
        couleur.setCellValueFactory(new PropertyValueFactory<tables.ta,String>("couleur"));
        tco.setItems(list2);

    }
}
