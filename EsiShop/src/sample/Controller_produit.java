package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by olr on 13/03/2017.
 */
public class Controller_produit implements Initializable {
    @FXML private TableView<tables.tp> tp;
    @FXML TableColumn<tables.tp, Integer> idproduit;
    @FXML TableColumn<tables.tp, String> cb;
    @FXML TableColumn<tables.tp, String> article;
    @FXML TableColumn<tables.tp, Integer> qt;
    @FXML TableColumn<tables.tp, String> marque;
    @FXML TableColumn<tables.tp, String> couleur;
    @FXML TableColumn<tables.tp, String> taille;
    @FXML TableColumn<tables.tp, Double> pa ;
    @FXML TableColumn<tables.tp, Double> pv ;
    @FXML TableColumn<tables.tp, Double> remise ;
    @FXML TableColumn<tables.tp, Double> tva ;
    @FXML TableColumn<tables.tp, String> img;
    @FXML TextField cbp,qtp,pap,pvp,ftva,qtm,rechp,tremise,tn,idps,time,noti;
    @FXML Label imgSelect;
    @FXML TextArea description;
    @FXML RadioButton Npro,Apro;
    @FXML public ComboBox<String> cmarque;
    @FXML public ComboBox<String> carticle;
    @FXML public ComboBox<String> ctaille;
    @FXML public ComboBox<String> ccouleur;
    @FXML Button BtnAjouter,BtnModifier,BtnVider,BtnSupprimer,BtnPasserCommande,BtnVente,BtnImage,back;
    ObservableList<tables.tp> list= FXCollections.observableArrayList();
    ObservableList<String> list1= FXCollections.observableArrayList();
    ObservableList<String> list2= FXCollections.observableArrayList();
    ObservableList<String> list3= FXCollections.observableArrayList();
    SimpleDateFormat sdf1 = new SimpleDateFormat("20YY:MM:dd--hh:mm:ss");
    db_connection db=new db_connection();
    Magasinier ma=new Magasinier();
    ResultSet s;
    int id,idcp,a,m,c=0;
    String t;
    Boolean modification=false,addImage=false;
    File selectfile;
    @FXML ImageView ImgProduit;
    Image image;


    public void RadioSelect(ActionEvent event) throws SQLException {
        //recuperer le dernier id de chaque produit
        if (Npro.isSelected()) idps.setText(String.valueOf(ma.RecupererLeDernierChaqueId("produit","IDcp")));
        if (Apro.isSelected()) {
            if(ma.OblierSelectione(tp,"un produit")) {
                Npro.setSelected(true);
                return;
            }
            s=db.exécutionQuery("SELECT * FROM `produit` WHERE `IDProduit`="+tp.getSelectionModel().getSelectedItem().getIdproduit());
            s.next();
            idps.setText(String.valueOf(s.getInt("IDcp")));
            idps.setDisable(true);
        }
    }
    public void addp(ActionEvent event) throws SQLException, IOException {
        //valider les champs
        if (!ma.ValiderTextField(new TextField[]{cbp,qtp,qtm,pap,pvp,ftva,tremise},new DatePicker[]{},new TextArea[]{description},cmarque,carticle,ccouleur)) return;

        imgSelect.setText("Image non Selectionné");
        //recuperer les IDs depuis les Combobox
        m=ma.returnIdParChamp("marque","nom-marque",cmarque.getSelectionModel().getSelectedItem());
        a=ma.returnIdParChamp("article","type-article",carticle.getSelectionModel().getSelectedItem());
        c=ma.returnIdParChamp("couleur","nom-couleur",ccouleur.getSelectionModel().getSelectedItem());
        if (ctaille.getSelectionModel().getSelectedItem().equals("NUMERIQUE")) t = tn.getText();
        else t = ctaille.getSelectionModel().getSelectedItem();

        //l'insertion
        db.exécutionUpdate("INSERT INTO `produit` (`IDProduit`, `IDcp` , `IDArticle`, `IDMarque`, `IDCouleur`, `taille`, `quantiteinitial`, `quantite`, `quantitemin`, `codebarre`, `description`, `prixachat`, `prixvente`, `remise`, `TVA`, `img` , `notifiable`) VALUES (NULL, '" + idps.getText() + "' , '" + a + "', '" + m + "', '" + c + "', '" + t + "', '" + qtp.getText() + "', '" + qtp.getText() + "', '" + qtm.getText() + "', '" + cbp.getText() + "', '" + description.getText() + "', '" + pap.getText() + "', '" + pvp.getText() + "', '" + tremise.getText() + "', '" + ftva.getText() + "', 'standard.png' ,'" + 1 + "')");
        if (addImage) ma.InsererImage();

        idps.setText(String.valueOf(ma.RecupererLeDernierChaqueId("produit","IDcp")));

        //mise a jour de l'affichage
        s=db.exécutionQuery("SELECT * FROM `produit`  NATURAL JOIN `marque` NATURAL JOIN `article` NATURAL JOIN `couleur` WHERE `supprimer`='0'");
        list.clear();
        while (s.next()) {
            list.add(new tables.tp(s.getInt("IDProduit"), s.getString("codebarre"),s.getString("type-article"), s.getInt("quantite"), s.getString("nom-marque"),s.getString("nom-couleur"), s.getString("taille"), s.getDouble("prixachat"), s.getDouble("prixvente"), s.getDouble("remise"), s.getDouble("TVA"), s.getString("img")));
        }
        ma.alert(Alert.AlertType.INFORMATION,"","produit ajouté");
        //vider les champs
        viderC(event);
    }
    public void suppp(ActionEvent event) throws SQLException {
        //oblier de sectione un produit
        if (ma.OblierSelectione(tp,"un produit")) return;

        if(!ma.ConfirmationSupp("supprimer","Suppression","ce produit?"))return;
        //supprimer tout l'existence de ce produit dans les autres tableaux
        db.exécutionUpdate("UPDATE `produit` SET `supprimer`='1' WHERE `IDProduit` ='"+tp.getSelectionModel().getSelectedItem().getIdproduit()+"'");
        db.exécutionUpdate("DELETE FROM `notification` WHERE `IDProduit`="+tp.getSelectionModel().getSelectedItem().getIdproduit());
        //supprimer de tableau
        list.remove(tp.getSelectionModel().getSelectedIndex());

        ma.alert(Alert.AlertType.INFORMATION,"suppression","produit supprimé");
    }
    public void modp(ActionEvent event) throws SQLException {
        //oblier de sectione un produit
        if (ma.OblierSelectione(tp,"un produit")) return;

        if(!modification) {
            //recuperer le produit depuit la base de donnée
            id = tp.getSelectionModel().getSelectedItem().getIdproduit();
            s = db.exécutionQuery("SELECT * FROM `produit` NATURAL JOIN `marque` NATURAL JOIN `article` NATURAL JOIN `couleur` WHERE `IDProduit`=" + id);

            //remplir les champs
            if (s.next()) {
                ma.remplirechamp(new String[]{s.getString("codebarre"), String.valueOf(s.getDouble("prixachat")), String.valueOf(s.getDouble("prixvente")), String.valueOf(s.getDouble("quantitemin")), String.valueOf(s.getInt("TVA")), String.valueOf(s.getDouble("remise"))}, cbp, pap, pvp, qtm, ftva, tremise);
                description.setText(s.getString("description"));
                modification = true;
            }
            if (ctaille.getItems().contains(s.getString("taille")))
                ctaille.getSelectionModel().select(s.getString("taille"));
            else tn.setText(s.getString("taille"));
            cmarque.getSelectionModel().select(s.getString("nom-marque"));
            ccouleur.getSelectionModel().select(s.getString("nom-couleur"));
            carticle.getSelectionModel().select(s.getString("type-article"));

            BtnModifier.setText("Enregistrer");
        }else {
            if(!ma.ConfirmationSupp("modifier","Modification","ce produit"))return;

            m=ma.returnIdParChamp("marque","nom-marque",cmarque.getSelectionModel().getSelectedItem());
            a=ma.returnIdParChamp("article","type-article",carticle.getSelectionModel().getSelectedItem());
            c=ma.returnIdParChamp("couleur","nom-couleur",ccouleur.getSelectionModel().getSelectedItem());
            if (ctaille.getSelectionModel().getSelectedItem().equals("NUMERIQUE")) t = tn.getText();
            else t = ctaille.getSelectionModel().getSelectedItem();

            db.exécutionUpdate("UPDATE `produit` SET `IDArticle` = '"+a+"', `IDMarque` = '"+m+"', `IDCouleur` = '"+c+"', `taille` = 'm', `quantitemin` = '"+qtm.getText()+"', `codebarre` = '"+cbp.getText()+"', `description` = '"+description.getText()+"' , `prixachat` = '"+pap.getText()+"' , `prixvente` = '"+pvp.getText()+"', `remise` = '"+tremise.getText()+"', `TVA` = '"+ftva.getText()+"', `taille`='"+t+"' WHERE `produit`.`IDProduit` = "+id);
            modification=false;

            //mise a jour de l'affichage
            s=db.exécutionQuery("SELECT * FROM `produit`  NATURAL JOIN `marque` NATURAL JOIN `article` NATURAL JOIN `couleur` WHERE `supprimer`='0'");
            list.clear();
            while (s.next()) {
                list.add(new tables.tp(s.getInt("IDProduit"), s.getString("codebarre"),s.getString("type-article"), s.getInt("quantite"), s.getString("nom-marque"),s.getString("nom-couleur"), s.getString("taille"), s.getDouble("prixachat"), s.getDouble("prixvente"), s.getDouble("remise"), s.getDouble("TVA"), s.getString("img")));
            }
            ma.alert(Alert.AlertType.INFORMATION,"modification","produit modifié");
            viderC(event);
        }
    }
    public void viderC(ActionEvent event) {
        ma.viderchamp(new TextField[]{cbp,qtp,pap,pvp,qtm,tremise,tn});modification=false;
        description.setText("");
        cmarque.getSelectionModel().selectFirst();
        ccouleur.getSelectionModel().selectFirst();
        carticle.getSelectionModel().selectFirst();
        ctaille.getSelectionModel().selectFirst();
        BtnModifier.setText("Modifier");
        addImage=false;
        Npro.setSelected(true);
        imgSelect.setText("Image non Selectionné");
    }
    public void rech(KeyEvent keyEvent){
        s=db.exécutionQuery("SELECT * FROM `produit`  NATURAL JOIN `marque` NATURAL JOIN `article` NATURAL JOIN `couleur` WHERE (`codebarre` LIKE '"+rechp.getText()+"%' OR `type-article` LIKE '"+rechp.getText()+"%' OR `quantite` LIKE '"+rechp.getText()+"%' OR `nom-marque` LIKE '"+rechp.getText()+"%' OR `nom-couleur` LIKE '"+rechp.getText()+"%' OR `taille` LIKE '"+rechp.getText()+"%' OR `prixachat` LIKE '"+rechp.getText()+"%' OR `prixvente` LIKE '"+rechp.getText()+"%') AND `supprimer`=0");
        list.clear();
        try {
            while (s.next()) {
                list.add(new tables.tp(s.getInt("IDProduit"), s.getString("codebarre"),s.getString("type-article"), s.getInt("quantite"), s.getString("nom-marque"), s.getString("nom-couleur"), s.getString("taille"), s.getDouble("prixachat"), s.getDouble("prixvente"), s.getDouble("remise"), s.getDouble("TVA"), s.getString("img")));
            }
        } catch (SQLException e) {}
    }
    public void Back(ActionEvent event) throws IOException {
        ma.openWindow(event,"/interfaces/accueil","Accueil",1366,768,true,true);
        data.openbynoti=0;
        data.openbyproduit=0;
        data.openbyespacevente=0;
        data.openbyachatcommande=0;
    }
    public void passercommande(Event event) throws IOException {
        //oblier de sectione un produit
        if (ma.OblierSelectione(tp,"un produit")) return;

        data.idproduit=tp.getSelectionModel().getSelectedItem().getIdproduit();
        data.openbyproduit=1;
        if (data.openbyachatcommande==1) {
            ma.openWindow(event,"/interfaces/Achat_Commande","Achat & Commande",1046, 641,true,false);
        }
        else ma.openWindow(event,"/interfaces/Achat_Commande","Achat & Commande",1046, 641,false,false);
    }
    public void vente(ActionEvent event) throws IOException, SQLException {
        //oblier de sectione un produit
        if (ma.OblierSelectione(tp,"produit")) return;

        data.openbyproduit=1;
        data.openbyespacevente=0;
        s=db.exécutionQuery("SELECT * FROM `produit` NATURAL JOIN `marque` NATURAL JOIN `article` WHERE `IDProduit`="+tp.getSelectionModel().getSelectedItem().getIdproduit());
        if (s.next()) data.tvps.add(new tables.tvp(s.getInt("IDProduit"),s.getString("type-article")+" "+ s.getString("nom-marque")+" "+s.getString("description"), 1, s.getDouble("prixvente"), s.getDouble("remise"),s.getDouble("prixvente")));
        if (data.total.isEmpty()) data.total= String.valueOf(s.getDouble("prixvente"));
        else data.total=String.valueOf(Double.valueOf(data.total)+s.getDouble("prixvente"));
        ma.openWindow(event,"/interfaces/sample","Espace de vente",966,670,true,true);
    }
    public void choisieimage(ActionEvent event) throws IOException {
        //open file choser
        FileChooser fc=new FileChooser();
        //ajouter les extentions possibles
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image jpg","*.jpg"));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image jpeg","*.jpeg"));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image png","*.png"));
        selectfile=fc.showOpenDialog(null);

        crop c=new crop();
        Stage s=new Stage();
        c.start(s,selectfile.getAbsolutePath());
        addImage=true;
        imgSelect.setText("Selectione");
    }
    public void defaultTheme(Event event){
        tp.setStyle("");
        ma.RestaurerStyleDesChampsOblier(new TextField[]{cbp,qtp,qtm,pap,pvp,ftva,tremise},new DatePicker[]{},new TextArea[]{description},cmarque,carticle,ccouleur);
    }
    public void opennoti(ActionEvent event) throws IOException, SQLException {
        db.exécutionUpdate("UPDATE `notification` SET `vu` = '1' WHERE `notification`.`vu` =0 ");
        ma.openWindow(event,"/interfaces/Notification","Notification",600,560,false,false);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //l'heure
        ma.time(sdf1,time);
        //acctualisation des notification
        ma.Noti(noti);

        tp.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            s=db.exécutionQuery("SELECT * FROM `produit` WHERE `IDProduit`="+tp.getSelectionModel().getSelectedItem().getIdproduit());
            try {
                s.next();
                image = new Image("http://127.0.0.1/esishop/uploads/"+s.getString("img"));
                ImgProduit.setImage(image);
            } catch (SQLException e) {e.printStackTrace();}
        });

        //set numeric textfield
        ma.MultiTextfieldNumiric(qtp,qtm,pap,pvp,ftva,tremise,tn,idps);

        //desactive composant si l'ouvert a partir d'une autre page
        if (data.openbyespacevente==1) {
            ma.Desactive(new TextField[]{cbp,qtp,qtm,pap,pvp,ftva,tremise,tn,idps},new DatePicker[]{},new TextArea[]{description},new Button[]{BtnAjouter,BtnModifier,BtnVider,BtnSupprimer,BtnPasserCommande,BtnImage,back},cmarque,carticle,ccouleur,ctaille);
            Npro.setDisable(true);Apro.setDisable(true);
        }
        else if (data.openbyachatcommande==1) {
            ma.Desactive(new TextField[]{cbp,qtp,qtm,pap,pvp,ftva,tremise,tn,idps,rechp},new DatePicker[]{},new TextArea[]{description},new Button[]{BtnAjouter,BtnModifier,BtnVider,BtnSupprimer,BtnImage,BtnVente,back},cmarque,carticle,ccouleur,ctaille);
            Npro.setDisable(true);Apro.setDisable(true);
        }

        imgSelect.setText("Image non Selectionné");
        //initialisation des combobox
        ma.InitialiserComboBox(list1,"nom-marque","marque",cmarque);
        ma.InitialiserComboBox(list2,"type-article","article",carticle);
        ma.InitialiserComboBox(list3,"nom-couleur","couleur",ccouleur);

        //recupérer idcp pour ajouter des nouvelle critere pour un ancient produit
        try {idps.setText(String.valueOf(ma.RecupererLeDernierChaqueId("produit","IDcp")));} catch (SQLException e) {}
        tp.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (Apro.isSelected()) {idcp=tp.getSelectionModel().getSelectedItem().getIdproduit();
                    s=db.exécutionQuery("SELECT * FROM `produit` WHERE `IDProduit`="+idcp);
                    try {if(s.next()) idcp=s.getInt("IDcp");} catch (SQLException e) {}
                    idps.setText(String.valueOf(idcp));}
            }
        });

        //initialisation de tableau des produits
        s=db.exécutionQuery("SELECT * FROM `produit`  NATURAL JOIN `marque` NATURAL JOIN `article` NATURAL JOIN `couleur` WHERE `supprimer`='0'");
        try {
            while (s.next()) {
                list.add(new tables.tp(s.getInt("IDProduit"), s.getString("codebarre"),s.getString("type-article"), s.getInt("quantite"), s.getString("nom-marque"), s.getString("nom-couleur"), s.getString("taille"), s.getDouble("prixachat"), s.getDouble("prixvente"), s.getDouble("remise"), s.getDouble("TVA"), s.getString("img")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        idproduit.setCellValueFactory(new PropertyValueFactory<tables.tp,Integer>("idproduit"));
        cb.setCellValueFactory(new PropertyValueFactory<tables.tp,String>("cb"));
        article.setCellValueFactory(new PropertyValueFactory<tables.tp,String>("article"));
        qt.setCellValueFactory(new PropertyValueFactory<tables.tp,Integer>("qt"));
        marque.setCellValueFactory(new PropertyValueFactory<tables.tp,String>("marque"));
        couleur.setCellValueFactory(new PropertyValueFactory<tables.tp,String>("couleur"));
        taille.setCellValueFactory(new PropertyValueFactory<tables.tp,String>("taille"));
        pa.setCellValueFactory(new PropertyValueFactory<tables.tp,Double>("pa"));
        pv.setCellValueFactory(new PropertyValueFactory<tables.tp,Double>("pv"));
        remise.setCellValueFactory(new PropertyValueFactory<tables.tp,Double>("remise"));
        tva.setCellValueFactory(new PropertyValueFactory<tables.tp,Double>("tva"));
        img.setCellValueFactory(new PropertyValueFactory<tables.tp,String>("img"));
        tp.setItems(list);
        ctaille.getSelectionModel().selectFirst();

    }
}
