package sample;

import
        javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Controller implements Initializable  {
    public Controller() throws SQLException {}
    @FXML TextField rech,recu,remisev,taux,time,noti,quantite,cbcf;
    @FXML private TableView<tables.tvp> tvp;
    @FXML TableColumn<tables.tvp,Integer> n;
    @FXML TableColumn<tables.tvp,String> produit;
    @FXML TableColumn<tables.tvp,Integer> qt;
    @FXML TableColumn<tables.tvp,Double> prix;
    @FXML TableColumn<tables.tvp,Double> remise;
    @FXML TableColumn<tables.tvp,Double> total;
    @FXML Button btnnoti,back;
    @FXML Label clientSelectioné;
    ObservableList<tables.tvp> list1= FXCollections.observableArrayList();
    ArrayList<tables.tvp> tab=new ArrayList<tables.tvp>();
    SimpleDateFormat sdf = new SimpleDateFormat("YY:MM:dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("20YY:MM:dd--hh:mm:ss");
    db_connection db=new db_connection();
    Magasinier m=new Magasinier();
    ResultSet s,ss;
    Calendar cal;
    boolean plusquantite,sauver=false;
    String t,r;
    int idf,idc,ii,idcps;


    public void validev(ActionEvent event) throws SQLException {
        //recuperer le dernier id de chaque facture
        int idcf=m.RecupererLeDernierChaqueId("facture","IDcf");

        //inserer le tableau de produit dans facture
        cal = Calendar.getInstance();
        for (int i=0;i<list1.size();i++) {
            db.exécutionUpdate("INSERT INTO `facture` (`IDFacture`, `IDcf`, `IDClient`, `IDProduit`, `quantite`, `date`, `prixtotal`, `Etat`) VALUES (NULL, '" + idcf + "', '"+idc+"', '"+list1.get(i).getN()+"', '"+list1.get(i).getQt()+"', '"+sdf.format(cal.getTime())+"', '"+list1.get(i).getQt()*list1.get(i).getPrix()+"', 'valide')");
            s=db.exécutionQuery("SELECT * FROM `produit` WHERE `IDProduit`="+list1.get(i).getN());
            s.next();
            db.exécutionUpdate("UPDATE `produit` SET `quantite`="+String.valueOf(s.getInt("quantite")-list1.get(i).getQt())+" WHERE `IDProduit`="+list1.get(i).getN());
        }

        //mise a jour des points de fidelité
        s=db.exécutionQuery("SELECT * FROM `client` WHERE `IDClient`="+data.idclient);
        s.next();
        db.exécutionUpdate("UPDATE `client` SET `point`="+String.valueOf(Float.valueOf(taux.getText().toString())/1000+s.getFloat("point"))+" WHERE `IDClient`="+idc);

        //add notification
        s=db.exécutionQuery("SELECT * FROM `produit`");
        while (s.next())
        {
            if(s.getDouble("quantite")<s.getDouble("quantitemin") && s.getInt("notifiable")==1){
                ss=db.exécutionQuery("SELECT * FROM `fournisseur`,`produit`,`marque` WHERE `produit`.`IDMarque`=`marque`.`IDMarque` AND `marque`.`IDMarque`=`fournisseur`.`IDMarque` AND `IDProduit`="+s.getInt("IDProduit"));
                if(ss.next()){idf=ss.getInt("IDFournisseur");}
                db.exécutionUpdate("INSERT INTO `notification` (`IDNotification`, `IDFournisseur`, `IDProduit`, `titre`, `date`, `description`, `vu`) VALUES (NULL, '"+idf+"', '"+s.getInt("IDProduit")+"', 'stock min', '"+sdf.format(cal.getTime())+"', '"+s.getString("description")+" attendre le minimum"+"', '0');");
            }
        }

        //update textfields and table
        list1.clear();
        viderC(event);
      //  remise.setText(String.valueOf(Double.valueOf(recu.getText())-Double.valueOf(taux.getText())));
    }
    public void qtp(ActionEvent event) {
        ii=tvp.getSelectionModel().getSelectedIndex();
        if (m.OblierSelectione(tvp,"un produit")) return;
        m.ModifierQuantite(event,"plus",tvp,list1,taux,quantite);
        tvp.getSelectionModel().select(ii);
    }
    public void qtm(Event event) {
        ii=tvp.getSelectionModel().getSelectedIndex();
        if (m.OblierSelectione(tvp,"un produit")) return;
        m.ModifierQuantite(event,"moin",tvp,list1,taux,quantite);
        tvp.getSelectionModel().select(ii);
    }
    public void viderC(ActionEvent event) {
        m.viderchamp(recu,remisev,taux,rech,cbcf);
        list1.clear();
        clientSelectioné.setText("non sélectionné");
        data.openbyclient=0;
        data.openbyproduit=0;
        data.idclient=1;
        data.idproduit=0;
    }
    public void sauvres(ActionEvent event) throws SQLException {
        data.idclient=0;
        sauver=m.Sauver_restaurer(event,sauver,tab,list1,taux,recu,remisev,idc,clientSelectioné);
    }
    public void suppproduit(ActionEvent event) {
        //oblier de sectione un produit
        if (m.OblierSelectione(tvp,"un produit")) return;
        //supprimer de tableau
        list1.remove(tvp.getSelectionModel().getSelectedIndex());
        //mise a jour du total
        taux.setText(String.valueOf(m.CalculerTotalVente(list1)));
    }
    public void add(KeyEvent keyEvent) throws SQLException {
        if (keyEvent.getCode()==KeyCode.ENTER){
            ResultSet s=db.exécutionQuery("SELECT * FROM `produit` NATURAL JOIN `marque` NATURAL JOIN `article` WHERE `codebarre`='"+Integer.valueOf(rech.getText())+"' AND `supprimer`='0'");
            if (s.next())
            {
                //si le nouveau produit existe deja dans le tableau -> +quantite
                for (int i=0;i<list1.size();i++)
                    {
                        if (list1.get(i).getN()==s.getInt("IDProduit"))
                        {
                            int q=list1.get(i).getQt();
                            list1.remove(i);
                            list1.add(i,new tables.tvp(s.getInt("IDProduit"),s.getString("type-article")+" "+ s.getString("nom-marque")+" "+s.getString("description"), q+1, s.getDouble("prixvente"), s.getDouble("remise"),Double.valueOf(s.getDouble("prixvente"))*(q+1)));
                            plusquantite=true;//pour ne pas encore ajouter un nouveau produit s'il exist deja
                        }
                    }
                //si n'existe pas on ajout
                 if (!plusquantite)
                 {
                     list1.add(new tables.tvp(s.getInt("IDProduit"),s.getString("type-article")+" "+ s.getString("nom-marque")+" "+s.getString("description"), 1, s.getDouble("prixvente"), s.getDouble("remise"),s.getDouble("prixvente")));
                    plusquantite=false;
                 }
                 plusquantite=false;
            }
            //actualiser le total
            taux.setText(String.valueOf(m.CalculerTotalVente(list1)));
            m.viderchamp(rech);
        }
    }
    public void Back(Event event) throws IOException {
      m.openWindow(event,"/interfaces/accueil","Accueil",1366,768,true,true);
      data.openbyclient=0;
      data.openbyproduit=0;
      data.idclient=1;
      data.idproduit=0;
      data.openbyespacevente=0;
    }
    public void opennoti(ActionEvent event) throws IOException, SQLException {
        db.exécutionUpdate("UPDATE `notification` SET `vu` = '1' WHERE `notification`.`vu` =0 ");
        m.openWindow(event,"/interfaces/Notification","Notification",600,560,false,false);
    }
    public void choisieProduit(ActionEvent event) throws IOException {
        //enregistrer le tableau des produit dans un autre tableau
        data.tvps.addAll(list1);
        list1.clear();
        data.total=taux.getText();
        data.openbyespacevente=1;
        m.openWindow(event,"/interfaces/gestion_produit","Gestion de produit",873,645,true,true);
    }
    public void defaultTheme(Event event){
        tvp.setStyle("");
    }
    public void openclient(ActionEvent event) throws IOException {
        //enregistrer le tableau des produit dans un autre tableau
        data.tvps.addAll(list1);
        data.total=taux.getText();
        data.openbyespacevente=1;

        m.openWindow(event,"/interfaces/gestion_client","Gestion client", 882, 595 ,true,true);
    }
    public void ClientParCarte(KeyEvent event) throws SQLException {
        if(event.getCode().equals(KeyCode.ENTER)){
            s=db.exécutionQuery("SELECT * FROM `client` WHERE `codebarreCF`="+cbcf.getText());
            try {s.next();
                clientSelectioné.setText(s.getString("nom")+" "+s.getString("prenom"));
                data.idclient=s.getInt("IDClient");
            } catch (SQLException e) {e.printStackTrace();}
        }
    }
    public void CalculerRendu(KeyEvent event){
        if (Double.valueOf(taux.getText()) <= Double.valueOf(recu.getText())){
            remisev.setText(String.valueOf(Double.valueOf(recu.getText())-Double.valueOf(taux.getText())));
        }
        else remisev.setText("");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        m.MultiTextfieldNumiric(recu);

        //mettre des champs numerique
        m.MultiTextfieldNumiric(quantite,recu);

        //ouvrir apres choisie le client
        if(data.openbyclient==1) {
            s=db.exécutionQuery("SELECT * FROM `client` WHERE `IDClient`="+data.idclient);
            try {s.next();
                clientSelectioné.setText(s.getString("nom")+" "+s.getString("prenom"));
            } catch (SQLException e) {e.printStackTrace();}
            list1.addAll(data.tvps);
            taux.setText(String.valueOf(data.total));
            data.tvps.clear();
            idc=data.idclient;
            data.openbyclient=0;
        }
        //ouvrir apres choisie le produit
        else if(data.openbyproduit==1) {
            list1.addAll(data.tvps);
            taux.setText(String.valueOf(data.total));
            data.tvps.clear();
            data.openbyproduit=0;
        }else idc=1;
        //l'heure
        m.time(sdf1,time);
        //acctualisation des notification
        m.Noti(noti);

        n.setCellValueFactory(new PropertyValueFactory<tables.tvp,Integer>("n"));
        produit.setCellValueFactory(new PropertyValueFactory<tables.tvp,String>("produit"));
        qt.setCellValueFactory(new PropertyValueFactory<tables.tvp,Integer>("qt"));
        prix.setCellValueFactory(new PropertyValueFactory<tables.tvp,Double>("prix"));
        remise.setCellValueFactory(new PropertyValueFactory<tables.tvp,Double>("remise"));
        total.setCellValueFactory(new PropertyValueFactory<tables.tvp,Double>("total"));
        tvp.setItems(list1);
        }
}



