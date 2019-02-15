package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * Created by olr on 13/03/2017.
 */
public class Controller_fournisseur implements Initializable{
    @FXML TextField nomf,prenomf,ntelf,nometuf,motpassef,nregf,faxf,emailf,adrf,tfr,time,noti;
    @FXML Button btnmod;
    @FXML private TableView<tables.tmdf> tmdf;
    @FXML TableColumn<tables.tf, String> marque;
    @FXML private TableView<tables.tf> tf;
    @FXML TableColumn<tables.tf, Integer> idf;
    @FXML TableColumn<tables.tf, String> nom;
    @FXML TableColumn<tables.tf, String> prenom;
    @FXML TableColumn<tables.tf, String> ntel;
    @FXML TableColumn<tables.tf, String> nregcom;
    @FXML TableColumn<tables.tf, String> fax ;
    @FXML TableColumn<tables.tf, String> nometu ;
    @FXML TableColumn<tables.tf, String> mdp;
    @FXML public ComboBox<String> cmarque;
    @FXML public ComboBox<String> bmarque;
    SimpleDateFormat sdf1 = new SimpleDateFormat("20YY:MM:dd--hh:mm:ss");
    ObservableList<tables.tmdf> list2 = FXCollections.observableArrayList();
    ObservableList<tables.tf> list = FXCollections.observableArrayList();
    ObservableList<String> list1= FXCollections.observableArrayList();
    boolean add,modifier=false;
    Magasinier m=new Magasinier();
    ResultSet s,ss;
    db_connection db=new db_connection();
    int id,idm,idcf,idcf1=0;

    public void viderC(ActionEvent event) {
        m.viderchamp(new TextField[]{nomf,prenomf,ntelf,nometuf,motpassef,nregf,faxf,emailf,adrf});
        add=false;
        bmarque.getSelectionModel().selectFirst();
        bmarque.getSelectionModel().selectFirst();
        btnmod.setText(" Modifier founisseur ");
    }
    public void addf(ActionEvent event) throws SQLException {
        //inserer un nouveau fournisseur-->recupere le darnier id chaque fournisseur
        idcf=m.RecupererLeDernierChaqueId("fournisseur","IDcf");

        ////l'insertion de nouveau fournisseur
            //valider champs
            if (!m.ValiderTextField(new TextField[]{nomf,prenomf,ntelf,nometuf,motpassef,nregf,emailf,adrf},new DatePicker[]{},new TextArea[]{},bmarque)) return;
            //recuperer l'id de la marque a partir de combobox
            idm=m.returnIdParChamp("marque","nom-marque",bmarque.getSelectionModel().getSelectedItem());
            if (!m.ValiderTextField(new TextField[]{},new DatePicker[]{},new TextArea[]{})) return;
            db.exécutionUpdate("INSERT INTO `fournisseur`(`IDFournisseur`,`IDcf` , `IDMarque`, `nom`, `prenom`, `nomdutilisateur`, `motdepasse`, `ntelephone`, `email`, `adresse`, `fax`, `nregcomm`) VALUES(NULL,'"+idcf+"','"+idm+" ','" + nomf.getText() + "', '" + prenomf.getText() + "', '" + nometuf.getText() + "', '" + motpassef.getText() + "', '" + ntelf.getText() + "', '" + emailf.getText() + "', '" + adrf.getText() + "', '" + faxf.getText() + "', '" + nregf.getText() + "')");

        //la mise a jour
            if (!m.ValiderTextField(new TextField[]{nomf,prenomf,ntelf,nometuf,motpassef,nregf,emailf,adrf},new DatePicker[]{},new TextArea[]{})) return;
            db.exécutionUpdate("UPDATE `fournisseur` SET `nom` = '"+nomf.getText()+"', `prenom` = '"+prenomf.getText()+"', `nomdutilisateur` = '"+nometuf.getText()+"', `motdepasse` = '"+motpassef.getText()+"', `ntelephone` = '"+ntelf.getText()+"', `email` = '"+emailf.getText()+"', `adresse` = '"+adrf.getText()+"', `fax` = '"+faxf.getText()+"', `nregcomm` = '"+nregf.getText()+"' WHERE `IDcf` ="+ id);
            add=false;

        int idcf1=0;
        //re affichage le tableau aprer la mise a jour
        list.clear();
        s=db.exécutionQuery("SELECT * FROM `fournisseur` NATURAL JOIN `marque` WHERE `supprimerF`=0 ORDER BY `IDcf` ASC ");
        try {while (s.next() ){
            if(s.getInt("IDcf")!=idcf1) {
                ss=db.exécutionQuery("SELECT * FROM `fournisseur` WHERE `IDcf`="+s.getInt("IDcf"));
                while (ss.next()){}
                list.add(new tables.tf(s.getInt("IDcf"), s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("nregcomm"), s.getString("fax"), s.getString("nomdutilisateur"), s.getString("motdepasse")));
            }
            idcf1=s.getInt("IDcf");
        }
        } catch (SQLException e) {}
        m.alert(Alert.AlertType.INFORMATION,"","fournisseur ajouté");
        viderC(event);
    }
    public void addm(ActionEvent event) throws SQLException {
        if (m.OblierSelectione(tf, "un fournisseur")) return;
        //valider champs
        if (!m.ValiderTextField(new TextField[]{},new DatePicker[]{},new TextArea[]{},bmarque)) return;

        idm=m.returnIdParChamp("marque","nom-marque",bmarque.getSelectionModel().getSelectedItem());

        s=db.exécutionQuery("SELECT * FROM `fournisseur` WHERE `IDcf`="+tf.getSelectionModel().getSelectedItem().getIdf());
        s.next();

        db.exécutionUpdate("INSERT INTO `fournisseur` (`IDFournisseur`,`IDcf` , `IDMarque`, `nom`, `prenom`, `nomdutilisateur`, `motdepasse`, `ntelephone`, `email`, `adresse`, `fax`, `nregcomm`) VALUES(NULL,'"+s.getInt("IDcf")+"','"+idm+" ','" + s.getString("nom") + "', '" + s.getString("prenom") + "', '" + s.getString("nomdutilisateur") + "', '" + s.getString("motdepasse") + "', '" + s.getString("ntelephone") + "', '" + s.getString("email") + "', '" + s.getString("adresse") + "', '" + s.getString("fax") + "', '" + s.getString("nregcomm") + "')");
        list2.add(new tables.tmdf(bmarque.getSelectionModel().getSelectedItem()));

        viderC(event);

    }
    public void suppm(ActionEvent event) throws SQLException {
        if (m.OblierSelectione(tmdf, "une marque")) return;

        db.exécutionUpdate("UPDATE `fournisseur` SET `supprimerM`='1' WHERE `IDcf` ='" + tf.getSelectionModel().getSelectedItem().getIdf() + "' AND  `IDMarque`='"+m.returnIdParChamp("marque","nom-marque",tmdf.getSelectionModel().getSelectedItem().getMarque())+"'");
        list2.remove(tmdf.getSelectionModel().getSelectedIndex());

        viderC(event);
    }
    public void rechf(Event keyEvent) throws SQLException {
        list.clear();
        s=db.exécutionQuery("SELECT *  FROM `fournisseur` NATURAL JOIN `marque` WHERE (`nom` LIKE '%"+tfr.getText()+"%' OR `prenom` LIKE '%"+tfr.getText()+"%' OR `nregcomm` LIKE '%"+tfr.getText()+"%') AND `nom-marque` LIKE '%"+cmarque.getSelectionModel().getSelectedItem()+"%' AND `supprimerF`=0 ORDER BY `IDcf` ASC");
         while (s.next() ){
            if(s.getInt("IDcf")!=idcf1) {
                ss=db.exécutionQuery("SELECT *  FROM `fournisseur` NATURAL JOIN `marque` WHERE (`nom` LIKE '%"+tfr.getText()+"%' OR `prenom` LIKE '%"+tfr.getText()+"%' OR `nregcomm` LIKE '%"+tfr.getText()+"%') AND `nom-marque` LIKE '%"+cmarque.getSelectionModel().getSelectedItem()+"%' AND `IDcf`="+s.getInt("IDcf")+" AND `supprimerF`=0");
                while (ss.next()) {}
                    list.add(new tables.tf(s.getInt("IDcf"), s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("nregcomm"), s.getString("fax"), s.getString("nomdutilisateur"), s.getString("motdepasse")));
                }
            idcf1=s.getInt("IDcf");
         }
            idcf1=0;
    }
    public void modf(ActionEvent event) throws SQLException {
        if(modifier){
            if (!m.ValiderTextField(new TextField[]{nomf,prenomf,ntelf,nometuf,motpassef,nregf,emailf,adrf},new DatePicker[]{},new TextArea[]{})) return;
            if(!m.ConfirmationSupp("modifier","Confirmation","ce fournisseur?"))return;
            db.exécutionUpdate("UPDATE `fournisseur` SET `nom` = '"+nomf.getText()+"', `prenom` = '"+prenomf.getText()+"', `nomdutilisateur` = '"+nometuf.getText()+"', `motdepasse` = '"+motpassef.getText()+"', `ntelephone` = '"+ntelf.getText()+"', `email` = '"+emailf.getText()+"', `adresse` = '"+adrf.getText()+"', `fax` = '"+faxf.getText()+"', `nregcomm` = '"+nregf.getText()+"' WHERE `IDcf` ="+ id);
            modifier=false;
            int idcf1=0;
            btnmod.setText(" Modifier founisseur ");
            //re affichage le tableau aprer la mise a jour
            list.clear();
            s=db.exécutionQuery("SELECT * FROM `fournisseur` NATURAL JOIN `marque` WHERE `supprimerF`=0 ORDER BY `IDcf` ASC ");
            try {while (s.next() ){
                if(s.getInt("IDcf")!=idcf1) {
                    ss=db.exécutionQuery("SELECT * FROM `fournisseur` WHERE `IDcf`="+s.getInt("IDcf"));
                    while (ss.next()){}
                    list.add(new tables.tf(s.getInt("IDcf"), s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("nregcomm"), s.getString("fax"), s.getString("nomdutilisateur"), s.getString("motdepasse")));
                }
                idcf1=s.getInt("IDcf");
            }
            } catch (SQLException e) {}
            m.alert(Alert.AlertType.INFORMATION,"Modification","fournisseur modifié");
            viderC(event);
        }
        else {
            if (m.OblierSelectione(tf, "un fournisseur")) return;
            tmdf.setMouseTransparent(false);
            btnmod.setText("Enregistrer Modification");
            id = tf.getSelectionModel().getSelectedItem().getIdf();
            s = db.exécutionQuery("SELECT * FROM `fournisseur` NATURAL JOIN `marque` WHERE `IDcf`=" + tf.getSelectionModel().getSelectedItem().getIdf());
            if (s.next()) {
                m.remplirechamp(new String[]{s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("nregcomm"), s.getString("fax"), s.getString("email"), s.getString("nomdutilisateur"), s.getString("motdepasse"), s.getString("adresse")}, nomf, prenomf, ntelf, nregf, faxf, emailf, nometuf, motpassef, adrf);
            }
            modifier = true;
        }
    }
    public void suppf(ActionEvent event) throws SQLException {
        if (m.OblierSelectione(tf, "un fournisseur")) return;

        if(!m.ConfirmationSupp("supprimer","Confirmation","ce fournisseur?"))return;

        db.exécutionUpdate("UPDATE `fournisseur` SET `supprimerF`='1',`supprimerM`='1' WHERE `IDcf` ='" + tf.getSelectionModel().getSelectedItem().getIdf() + "'");
        list.remove(tf.getSelectionModel().getSelectedIndex());

        m.alert(Alert.AlertType.INFORMATION,"Suppression","fournisseur supprimé");

        viderC(event);
    }
    public void Back(ActionEvent event) throws IOException {
        m.openWindow(event,"/interfaces/accueil","Accueil",1366,768,true,true);
        data.openbyfournisseur=0;
    }
    public void openMarque(Event event) throws IOException {
        data.openbyfournisseur=1;
        m.openWindow(event,"/interfaces/Marque_Article","Marque/Article/Couleur",572, 570,false,false);
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{nomf,prenomf,ntelf,nometuf,motpassef,nregf,emailf,adrf},new DatePicker[]{},new TextArea[]{},bmarque);
        tf.setStyle("");
    }
    public void MAJMarqueDisponible(Event event) throws SQLException {
        bmarque.setItems(null);
        bmarque.setItems(m.returnMarqueSansFournisseur());
        bmarque.getSelectionModel().selectFirst();
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

        //change selection table fournisseur to change table marque
        tf.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                list2.clear();
                idcf=tf.getSelectionModel().getSelectedItem().getIdf();
                s=db.exécutionQuery("SELECT * FROM `fournisseur` NATURAL JOIN `marque` WHERE `IDcf`='"+idcf+"' AND `supprimerM`=0");
                try {while (s.next()) list2.add(new tables.tmdf(s.getString("nom-marque")));
                } catch (SQLException e) {e.printStackTrace();}
        });

        //intialisation de combo marque par les marque sans fournisseur
        try {bmarque.setItems(m.returnMarqueSansFournisseur());} catch (SQLException e) {e.printStackTrace();}
        bmarque.getSelectionModel().selectFirst();

        //intialisation de combobox marque de recherch
        m.InitialiserComboBox(list1,"nom-marque","marque",cmarque);

        //intialisation tableau fournisseur
        s=db.exécutionQuery("SELECT * FROM `fournisseur` NATURAL JOIN `marque` WHERE `supprimerF`=0 ORDER BY `IDcf` ASC ");
        try {while (s.next() ){
            if(s.getInt("IDcf")!=idcf1) {
                ss=db.exécutionQuery("SELECT * FROM `fournisseur` WHERE `IDcf`="+s.getInt("IDcf"));
                while (ss.next()){}
                list.add(new tables.tf(s.getInt("IDcf"), s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("nregcomm"), s.getString("fax"), s.getString("nomdutilisateur"), s.getString("motdepasse")));
            }
            idcf1=s.getInt("IDcf");
        }
        } catch (SQLException e) {}

        idf.setCellValueFactory(new PropertyValueFactory<tables.tf,Integer>("idf"));
        nom.setCellValueFactory(new PropertyValueFactory<tables.tf,String>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<tables.tf,String>("prenom"));
        ntel.setCellValueFactory(new PropertyValueFactory<tables.tf,String>("ntel"));
        nregcom.setCellValueFactory(new PropertyValueFactory<tables.tf,String>("nregcom"));
        fax.setCellValueFactory(new PropertyValueFactory<tables.tf,String>("fax"));
        nometu.setCellValueFactory(new PropertyValueFactory<tables.tf,String>("nometu"));
        mdp.setCellValueFactory(new PropertyValueFactory<tables.tf,String>("mdp"));
        tf.setItems(list);

        //initialisation tableau marque des fournisseurs
        marque.setCellValueFactory(new PropertyValueFactory<tables.tf,String>("marque"));
        tmdf.setItems(list2);

    }
}
