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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by olr on 13/03/2017.
 */
public class ControllerAchat_Commande implements Initializable
{
    @FXML TextField qtac,rechac,time,noti;
    @FXML public ComboBox<String> cetat;
    @FXML private TableView<tables.tac> tac;
    @FXML TableColumn<tables.tac,Integer> id;
    @FXML TableColumn<tables.tac,String> produit;
    @FXML TableColumn<tables.tac,String> marque;
    @FXML TableColumn<tables.tac,String> article;
    @FXML TableColumn<tables.tac,String> couleur;
    @FXML TableColumn<tables.tac,String> taille;
    @FXML TableColumn<tables.tac,Double> prix;
    @FXML TableColumn<tables.tac,Integer> qt;
    @FXML TableColumn<tables.tac,Integer> Fournisseur;
    @FXML TableColumn<tables.tac,String> d;
    @FXML TableColumn<tables.tac,Double> total;
    @FXML TableColumn<tables.tac,String> etat;
    @FXML Button BtnNC,BtnAC,BtnMC,back;
    ObservableList<tables.tac> list1= FXCollections.observableArrayList();
    ObservableList<String> list= FXCollections.observableArrayList();
    ObservableList<String> list2= FXCollections.observableArrayList();
    SimpleDateFormat sdf = new SimpleDateFormat("20YY:MM:dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("20YY:MM:dd--hh:mm:ss");
    Calendar cal;
    Magasinier m=new Magasinier();
    db_connection db=new db_connection();
    String sql;
    boolean modifier=false;
    ResultSet s;
    @FXML Label produitsel;

    public void addac(ActionEvent event) throws SQLException, IOException {
        //ouvrir depuit notification ou produit
        if (data.openbynoti==1 || data.openbyproduit==1) {
            //valider les champs
            if (!m.ValiderTextField(new TextField[]{qtac}, new DatePicker[]{}, new TextArea[]{})) return;
            //recuperer les IDs des fournisseur et le prix d'achat de produit
            s = db.exécutionQuery("SELECT * FROM `fournisseur` NATURAL JOIN `marque` NATURAL JOIN `produit` WHERE `IDProduit`=" + data.idproduit);
            cal = Calendar.getInstance();
            int idfournisseur = -1;
            double prixachat = 0;
            if (s.next()) {idfournisseur = s.getInt("IDFournisseur");prixachat = s.getDouble("prixachat");}

            db.exécutionUpdate("INSERT INTO `achat-commande` (`IDAchat_Commande`, `IDProduit`, `IDFournisseur`, `quantite`, `total`, `date`, `Etat`) VALUES (NULL, '"+data.idproduit+"', '"+idfournisseur+"', '"+qtac.getText()+"', '"+Integer.valueOf(qtac.getText())*prixachat+"', '"+sdf.format(cal.getTime())+"', 'commande');");

            //alert commande transferer et ferme la fenetre
            m.alert(Alert.AlertType.INFORMATION,"commande transfere","votre commande a bien été transféré au fournisseur");
            if (data.openbynoti==1) db.exécutionUpdate("DELETE FROM `notification` WHERE `IDNotification`="+data.idnotification);
            data.idnotification=0;
            data.openbyproduit=0;
            data.openbynoti=0;
            if(data.openbyachatcommande==0) ((Node) event.getSource()).getScene().getWindow().hide();

        }
            //sinon ouvrir gestion produit pour choisi un produit
            else {
                data.openbyachatcommande=1;
                m.openWindow(event,"/interfaces/gestion_produit","Gestion produits", 873, 645 ,true,true);
                return;
            }

        //mise a jour d'affichageafficher
        list1.clear();
        s=db.exécutionQuery("SELECT * FROM `achat-commande`,`produit`,`couleur`,`article`,`marque`,`fournisseur` WHERE `achat-commande`.`IDProduit`=`produit`.`IDProduit` AND `produit`.`IDCouleur`=`couleur`.`IDCouleur` AND `produit`.`IDArticle`=`article`.`IDArticle` AND `produit`.`IDMarque`=`marque`.`IDMarque` AND `achat-commande`.`IDFournisseur`=`fournisseur`.`IDFournisseur`");
        try {
            while (s.next())
            {
                list1.add(new tables.tac(s.getInt("IDAchat_Commande"),s.getString("description"),s.getString("type-article"),s.getString("nom-marque"),s.getString("nom-couleur"),s.getString("taille"),s.getDouble("prixachat"),s.getInt("quantite"),s.getDouble("total"),s.getString("nom")+" "+s.getString("prenom"),s.getString("date"),s.getString("Etat")));
            }
        } catch (SQLException e) {}
        m.viderchamp(qtac);
        data.idproduit=0;
        produitsel.setText("produit non sélectionné");

    }
    public void modac(ActionEvent event) throws SQLException {
        //charger les information dans les champs
        if(!modifier){
            if (m.OblierSelectione(tac,"une commande")) return;
            qtac.setText(String.valueOf(tac.getSelectionModel().getSelectedItem().getQt()));
            modifier=true;
            BtnMC.setText("Enregistrer les modification");
        //enregistrer les modifications
        }else {
            if(!m.ConfirmationSupp("modifier","Modification","cette commande?"))return;

            db.exécutionUpdate("UPDATE `achat-commande` SET `quantite` = '"+qtac.getText()+"' WHERE `achat-commande`.`IDAchat_Commande` ="+tac.getSelectionModel().getSelectedItem().getId());
            modifier=false;
            list1.clear();
            s=db.exécutionQuery("SELECT * FROM `achat-commande`,`produit`,`couleur`,`article`,`marque`,`fournisseur` WHERE `achat-commande`.`IDProduit`=`produit`.`IDProduit` AND `produit`.`IDCouleur`=`couleur`.`IDCouleur` AND `produit`.`IDArticle`=`article`.`IDArticle` AND `produit`.`IDMarque`=`marque`.`IDMarque` AND `achat-commande`.`IDFournisseur`=`fournisseur`.`IDFournisseur`");
            try {
                while (s.next())
                {
                    list1.add(new tables.tac(s.getInt("IDAchat_Commande"),s.getString("description"),s.getString("type-article"),s.getString("nom-marque"),s.getString("nom-couleur"),s.getString("taille"),s.getDouble("prixachat"),s.getInt("quantite"),s.getDouble("total"),s.getString("nom")+" "+s.getString("prenom"),s.getString("date"),s.getString("Etat")));
                }
            } catch (SQLException e) {}
            qtac.setText("");
            BtnMC.setText("Modifier Commande");
            m.alert(Alert.AlertType.INFORMATION,"Modification","commande modifiée");
        }
    }
    public void annac(ActionEvent event) throws SQLException {
        if (m.OblierSelectione(tac,"une commande")) return;
        if (!m.ConfirmationSupp("annuler","Annulation","cette commande")) return;

        db.exécutionUpdate("DELETE FROM `achat-commande` WHERE `IDAchat_Commande` ='" + tac.getSelectionModel().getSelectedItem().getId() + "'");
        list1.remove(tac.getSelectionModel().getSelectedIndex());
        m.alert(Alert.AlertType.INFORMATION,"","commande annulée");

    }
    public void rechAC(Event event) throws SQLException {
        sql="SELECT * FROM `achat-commande`,`produit`,`couleur`,`article`,`marque`,`fournisseur` WHERE (`achat-commande`.`IDProduit`=`produit`.`IDProduit` AND `produit`.`IDCouleur`=`couleur`.`IDCouleur` AND `produit`.`IDArticle`=`article`.`IDArticle` AND `produit`.`IDMarque`=`marque`.`IDMarque` AND `achat-commande`.`IDFournisseur`=`fournisseur`.`IDFournisseur`) AND (`type-article` LIKE '%"+rechac.getText()+"%' OR `nom-couleur` LIKE '%"+rechac.getText()+"%' OR `nom` LIKE '%"+rechac.getText()+"%' OR `prenom` LIKE '%"+rechac.getText()+"%' OR `nom-marque` LIKE '%"+rechac.getText()+"%')  AND `achat-commande`.`Etat` LIKE '%"+cetat.getSelectionModel().getSelectedItem()+"%'  " ;
        s=db.exécutionQuery(sql);//AND `article`.`type-article` LIKE '%"+rechac.getText()+"%'
        list1.clear();
        try {
            while (s.next())
            {
                list1.add(new tables.tac(s.getInt("IDAchat_Commande"),s.getString("description"),s.getString("type-article"),s.getString("nom-marque"),s.getString("nom-couleur"),s.getString("taille"),s.getDouble("prixachat"),s.getInt("quantite"),s.getDouble("total"),s.getString("nom")+" "+s.getString("prenom"),s.getString("date"),s.getString("Etat")));
            }
        } catch (SQLException e) {}
    }
    public void Back(ActionEvent event) throws IOException {
        m.openWindow(event,"/interfaces/accueil","Accuil",1366,768,true,true);
        data.openbyproduit=0;
        data.openbynoti=0;
        data.idproduit=0;
        data.openbyachatcommande=0;
        }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{qtac},new DatePicker[]{},new TextArea[]{});
        tac.setStyle("");
    }
    public void opennoti(ActionEvent event) throws IOException, SQLException {
        db.exécutionUpdate("UPDATE `notification` SET `vu` = '1' WHERE `notification`.`vu` =0 ");
        m.openWindow(event,"/interfaces/Notification","Notification",600,560,false,false);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //l'heure
        m.time(sdf1,time);
        //acctualisation des notification
        m.Noti(noti);

        tac.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (tac.getSelectionModel().getSelectedItem().getEtat().equals("achat") || tac.getSelectionModel().getSelectedItem().getEtat().equals("en route")) {
                    BtnMC.setDisable(true);BtnAC.setDisable(true);
                }
                else {
                    BtnMC.setDisable(false);BtnAC.setDisable(false);
                }
            }
        });


        //desactive composant si l'ouvert a partir d'une autre page
        if ((data.openbyproduit==1 && data.openbyachatcommande==1)) {
        }else if (data.openbyproduit==1 || data.openbynoti==1)  {
            m.Desactive(new TextField[]{rechac},new DatePicker[]{},new TextArea[]{},new Button[]{BtnAC,BtnMC,back},cetat);
        }
        //mettre des champs numerique
        m.MultiTextfieldNumiric(qtac);
        //produit selectione ou non
        if(data.idproduit!=0) produitsel.setText("produit sélectionné");
        else produitsel.setText("produit non sélectionné");

        s=db.exécutionQuery("SELECT * FROM `achat-commande`,`produit`,`couleur`,`article`,`marque`,`fournisseur` WHERE `achat-commande`.`IDProduit`=`produit`.`IDProduit` AND `produit`.`IDCouleur`=`couleur`.`IDCouleur` AND `produit`.`IDArticle`=`article`.`IDArticle` AND `produit`.`IDMarque`=`marque`.`IDMarque` AND `achat-commande`.`IDFournisseur`=`fournisseur`.`IDFournisseur`");
        try {
            while (s.next())
            {
                list1.add(new tables.tac(s.getInt("IDAchat_Commande"),s.getString("description"),s.getString("type-article"),s.getString("nom-marque"),s.getString("nom-couleur"),s.getString("taille"),s.getDouble("prixachat"),s.getInt("quantite"),s.getDouble("total"),s.getString("nom")+" "+s.getString("prenom"),s.getString("date"),s.getString("Etat")));
            }
        } catch (SQLException e) {}
        id.setCellValueFactory(new PropertyValueFactory<tables.tac,Integer>("id"));
        produit.setCellValueFactory(new PropertyValueFactory<tables.tac,String>("produit"));
        marque.setCellValueFactory(new PropertyValueFactory<tables.tac,String>("marque"));
        article.setCellValueFactory(new PropertyValueFactory<tables.tac,String>("article"));
        couleur.setCellValueFactory(new PropertyValueFactory<tables.tac,String>("couleur"));
        taille.setCellValueFactory(new PropertyValueFactory<tables.tac,String>("taille"));
        prix.setCellValueFactory(new PropertyValueFactory<tables.tac,Double>("prix"));
        qt.setCellValueFactory(new PropertyValueFactory<tables.tac,Integer>("qt"));
        Fournisseur.setCellValueFactory(new PropertyValueFactory<tables.tac,Integer>("Fournisseur"));
        d.setCellValueFactory(new PropertyValueFactory<tables.tac,String>("d"));
        total.setCellValueFactory(new PropertyValueFactory<tables.tac,Double>("total"));
        etat.setCellValueFactory(new PropertyValueFactory<tables.tac,String>("etat"));
        tac.setItems(list1);

        cetat.getSelectionModel().selectFirst();
    }
}