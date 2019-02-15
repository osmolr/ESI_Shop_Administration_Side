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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Created by olr on 13/03/2017.
 */
public class
Controller_client implements Initializable {
    @FXML TextField nomc,prenomc,ntelc,emailc,villec,cpc,adrc,rechc,cdf,time,noti;
    @FXML DatePicker DateDeNaissance;
    @FXML private TableView<tables.tc> tc;
    @FXML TableColumn<tables.tc, Integer> idclient;
    @FXML TableColumn<tables.tc, String> nom;
    @FXML TableColumn<tables.tc, String> prenom;
    @FXML TableColumn<tables.tc, String> ntel;
    @FXML TableColumn<tables.tc, String> email;
    @FXML TableColumn<tables.tc, String> ville;
    @FXML TableColumn<tables.tc, String> adr ;
    @FXML TableColumn<tables.tc, Integer> cp ;
    @FXML TableColumn<tables.tc, String> nometu ;
    @FXML TableColumn<tables.tc, String> date;
    @FXML TableColumn<tables.tc, String> CBc;
    @FXML TableColumn<tables.tc, Double> pointF;
    @FXML public ComboBox<String> sexe;
    @FXML public ComboBox<String> typeinscription;
    @FXML Button ajouter,vider,modifier,back;
    SimpleDateFormat sdf1 = new SimpleDateFormat("20YY:MM:dd--hh:mm:ss");
    ObservableList<tables.tc> list = FXCollections.observableArrayList();
    Magasinier m=new Magasinier();
    Calendar cal;
    SimpleDateFormat sdf = new SimpleDateFormat("YY:MM:dd");
    db_connection db=new db_connection();
    ResultSet s;
    boolean modification=false;
    int id;
    @FXML ImageView PhotoClient;
    Image image;

    public void addc(ActionEvent event) throws SQLException {
        //valides les champs
        if (!m.ValiderTextField(new TextField[]{nomc,prenomc,ntelc},new DatePicker[]{DateDeNaissance},new TextArea[]{},sexe)) return;
        cal = Calendar.getInstance();
        //l'insertion
        db.exécutionUpdate("INSERT INTO `client` (`nom`, `prenom`, `sexe`, `datedenaissance`, `email`, `adresse`, `codepostal`, `ville`, `ntelephone`, `datedecreation`, `codebarreCF`) VALUES ('"+nomc.getText()+"', '"+prenomc.getText()+"', '"+sexe.getSelectionModel().getSelectedItem()+"', '"+DateDeNaissance.getValue().toString()+"', "+(emailc.getText().isEmpty()?"NULL":"'"+emailc.getText()+"'")+", "+(adrc.getText().isEmpty()?"NULL":"'"+adrc.getText()+"'")+", "+(cpc.getText().isEmpty()?" NULL ":cpc.getText())+", "+(villec.getText().isEmpty()?"NULL":"'"+villec.getText()+"'")+", "+(ntelc.getText().isEmpty()?"NULL":"'"+ntelc.getText()+"'")+", '"+sdf.format(cal.getTime())+"',"+(cdf.getText().isEmpty()?"NULL":"'"+cdf.getText()+"'")+")");

        //mise a jour de l'affichage
        list.clear();
        s=db.exécutionQuery("SELECT * FROM `client` ORDER BY `IDClient` ASC");
        s.next();
        while (s.next()) {
                list.add(new tables.tc(s.getInt("IDClient"), s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("email"), s.getString("ville"), s.getString("adresse"), s.getInt("codepostal"), s.getString("nomdutilisateur"), s.getString("datedecreation"), s.getString("codebarreCF"), s.getDouble("point")));
        }
        m.alert(Alert.AlertType.INFORMATION,"","client ajouté");
        viderC(event);
    }
    public void modc(ActionEvent event) throws SQLException {
        //oblier de selectione un client
        if (m.OblierSelectione(tc,"un client")) return;

        if(!modification) {
            id = tc.getSelectionModel().getSelectedItem().getIdclient();
            s = db.exécutionQuery("SELECT * FROM `client` WHERE `IDClient`=" + id);
                if (s.next()) {
                    m.remplirechamp(new String[]{s.getString("nom"), s.getString("prenom"), s.getString("email"), s.getString("ville"), s.getString("adresse"), s.getString("codebarreCF")}, nomc, prenomc, emailc, villec, adrc, cdf);
                }
                cpc.setText(String.valueOf(s.getInt("codepostal")));
                ntelc.setText(String.valueOf(s.getInt("ntelephone")));
                DateDeNaissance.setValue(LocalDate.parse(s.getString("datedenaissance")));
                sexe.setValue(s.getString("sexe"));
            modification=true;
            modifier.setText("Enregistrer");
        }
        else {
            if(!m.ConfirmationSupp("modifier","Modification","ce client?"))return;

            db.exécutionUpdate("UPDATE `client` SET `nom` = '"+nomc.getText()+"', `prenom` = '"+prenomc.getText()+"', `email` = '"+emailc.getText()+"', `adresse` = '"+adrc.getText()+"', `codepostal` = '"+cpc.getText()+"', `ville` = '"+villec.getText()+"', `ntelephone` = '"+ntelc.getText()+"', `datedenaissance`='"+DateDeNaissance.getValue()+"', `codebarreCF`='"+cdf.getText()+"' WHERE `IDClient` ="+ id);
            modification=false;

            list.clear();
            s=db.exécutionQuery("SELECT * FROM `client` ORDER BY `IDClient` ASC");
            s.next();
            while (s.next()) {
                list.add(new tables.tc(s.getInt("IDClient"), s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("email"), s.getString("ville"), s.getString("adresse"), s.getInt("codepostal"), s.getString("nomdutilisateur"), s.getString("datedecreation"), s.getString("codebarreCF"), s.getDouble("point")));
            }
            m.alert(Alert.AlertType.INFORMATION,"Modification","client modifié");
            viderC(event);
        }

    }
    public void rech(Event event) throws SQLException {
        String sql="SELECT *  FROM `client` WHERE (`nom` LIKE '%"+rechc.getText()+"%' OR `prenom` LIKE '%"+rechc.getText()+"%') AND `nom`!='anonyme'";
        //recherche des client inscri en magazin
        if (typeinscription.getSelectionModel().getSelectedItem().equals("Magazin"))
        {sql+=" AND `nomdutilisateur` IS NULL";}
        //recherche des client inscri sur le web
        else if(typeinscription.getSelectionModel().getSelectedItem().equals("Web"))
        {sql+="AND `nomdutilisateur` IS NOT NULL";}
        s=db.exécutionQuery(sql);
        list.clear();
        while (s.next()) {
            list.add(new tables.tc(s.getInt("IDClient"), s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("email"), s.getString("ville"), s.getString("adresse"), s.getInt("codepostal"), s.getString("nomdutilisateur"), s.getString("datedecreation"),s.getString("codebarreCF"),s.getDouble("point")));
        }}
    public void viderC(ActionEvent event) {
        m.viderchamp(new TextField[] {nomc,prenomc,ntelc,emailc,villec,cpc,adrc,cdf});
        modification=false;
        sexe.getSelectionModel().selectFirst();
        typeinscription.getSelectionModel().selectFirst();
        DateDeNaissance.setValue(null);
        modifier.setText("Modifier");

    }
    public void Back(ActionEvent event) throws IOException {
        m.openWindow(event,"/interfaces/accueil","Accueil",1366,768,true,true);
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{nomc,prenomc,ntelc},new DatePicker[]{DateDeNaissance},new TextArea[]{},sexe);
        tc.setStyle("");
    }
    public void openespacevente(ActionEvent event) throws IOException {
        //oblier de sectione un produit
        if (m.OblierSelectione(tc,"un client")) return;

        data.openbyclient=1;
        data.idclient=tc.getSelectionModel().getSelectedItem().getIdclient();

        m.openWindow(event,"/interfaces/sample","Espace vente", 966, 670 ,true,true);
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

        //metre des champs de numerique
        m.MultiTextfieldNumiric(ntelc,cpc);

        //desactiver tout les composant si ouvrir par espace vente
        if (data.openbyespacevente==1){
            m.Desactive(new TextField[]{nomc,prenomc,ntelc,emailc,villec,cpc,adrc,cdf},new DatePicker[]{DateDeNaissance},new TextArea[]{},new Button[]{ajouter,vider,modifier,back},sexe);
        }

        //photo de client
        tc.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            s=db.exécutionQuery("SELECT * FROM `client` WHERE `IDClient`="+tc.getSelectionModel().getSelectedItem().getIdclient());
            try {
                s.next();
                image = new Image("http://127.0.0.1/esishop/ProfilPic/"+s.getString("photo"));
                PhotoClient.setImage(image);
            } catch (SQLException e) {e.printStackTrace();}
        });

        s=db.exécutionQuery("SELECT * FROM `client` ORDER BY `IDClient` ASC");
        try {
            s.next();
            while (s.next()) {
                list.add(new tables.tc(s.getInt("IDClient"), s.getString("nom"), s.getString("prenom"), s.getString("ntelephone"), s.getString("email"), s.getString("ville"), s.getString("adresse"), s.getInt("codepostal"), s.getString("nomdutilisateur"), s.getString("datedecreation"),s.getString("codebarreCF"),s.getDouble("point")));
            }
        } catch (SQLException e) {}

        idclient.setCellValueFactory(new PropertyValueFactory<tables.tc,Integer>("idclient"));
        nom.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("prenom"));
        ntel.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("ntel"));
        email.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("email"));
        ville.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("ville"));
        adr.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("adr"));
        cp.setCellValueFactory(new PropertyValueFactory<tables.tc,Integer>("cp"));
        nometu.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("nometu"));
        date.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("date"));
        CBc.setCellValueFactory(new PropertyValueFactory<tables.tc,String>("CBc"));
        pointF.setCellValueFactory(new PropertyValueFactory<tables.tc,Double>("pointF"));
        tc.setItems(list);
        typeinscription.getSelectionModel().selectFirst();
        sexe.getSelectionModel().selectFirst();
    }
}
