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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
/**
 * Created by olr on 26/03/2017.
 */
public class Controller_historique_des_ventes implements Initializable {
    @FXML DatePicker from,to;
    @FXML Button btnFiltre;
    @FXML RadioButton CeJour,CetteSemain,CeMois,ParDate;
    @FXML private TableView<tables.thv> thv;
    @FXML TableColumn<tables.thv, Integer> idcf;
    @FXML TableColumn<tables.thv, String> client;
    @FXML TableColumn<tables.thv, String> date;
    @FXML TableColumn<tables.thv, Double> total1;
    ObservableList<tables.thv> list = FXCollections.observableArrayList();
    @FXML private TableView<tables.thvad> thvad;
    @FXML TableColumn<tables.thvad, Integer> idproduit;
    @FXML TableColumn<tables.thvad, String> produit;
    @FXML TableColumn<tables.thvad, Integer> qt;
    @FXML TableColumn<tables.thvad, Double> total2;
    ObservableList<tables.thvad> list1 = FXCollections.observableArrayList();
    db_connection db=new db_connection();
    ResultSet s,ss;
    int idcf1=-1;
    double total3=0;
    Magasinier m=new Magasinier();
    Calendar cal=new GregorianCalendar(),cal1;
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY:MM:dd");
    @FXML Label Recette;


    public void Back(Event event) throws IOException {
        m.openWindow(event,"/interfaces/accueil","Accueil",1366,768,true,true);
        }
    public void filtrer(ActionEvent event){
        if (!m.ValiderTextField(new TextField[]{},new DatePicker[]{from,to},new TextArea[]{})) return;

        //valides les champs
        if (!m.ValiderTextField(new TextField[]{},new DatePicker[]{from,to},new TextArea[]{})) return;

        list.clear();
        list1.clear();
        m.FiltrerFacture(idcf1,total3,list,s,ss,from.getValue().toString(),to.getValue().toString(),true);

        Recette.setText(String.valueOf(m.CalculerTotalFacture(list)));
    }
    public void RadioSelect(ActionEvent event){
        if (CeJour.isSelected() || CeMois.isSelected() || CetteSemain.isSelected()) {
            from.setDisable(true);
            to.setDisable(true);
            btnFiltre.setDisable(true);
        }
        if(ParDate.isSelected()){
            from.setDisable(false);
            to.setDisable(false);
            btnFiltre.setDisable(false);
            list.clear();
            m.FiltrerFacture(idcf1,total3,list,s,ss,null,null,false);
            thv.getSelectionModel().selectFirst();
        }
        cal1= Calendar.getInstance();

        if (CeJour.isSelected()){
            list.clear();
            list1.clear();
            m.FiltrerFacture(idcf1,total3,list,s,ss,sdf.format(cal1.getTime()),sdf.format(cal1.getTime()),true);
            thv.getSelectionModel().selectFirst();
        }else if (CetteSemain.isSelected()){
            list.clear();
            list1.clear();
            cal=new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, -7);
            Date DaysAgo = cal.getTime();
            m.FiltrerFacture(idcf1,total3,list,s,ss,sdf.format(DaysAgo),sdf.format(cal1.getTime()),true);
            thv.getSelectionModel().selectFirst();
            cal=null;

        }else if (CeMois.isSelected()){
            list.clear();
            list1.clear();
            cal=new GregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            Date DaysAgo = cal.getTime();
            m.FiltrerFacture(idcf1,total3,list,s,ss,sdf.format(DaysAgo),sdf.format(cal1.getTime()),true);
            cal=null;
            thv.getSelectionModel().selectFirst();
        }
        Recette.setText(String.valueOf(m.CalculerTotalFacture(list)));
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{},new DatePicker[]{from,to},new TextArea[]{});
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ParDate.setSelected(true);

        m.FiltrerFacture(idcf1,total3,list,s,ss,null,null,false);

        Recette.setText(String.valueOf(m.CalculerTotalFacture(list)));

        thv.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                list1.clear();
                s=db.exécutionQuery("SELECT * FROM `facture`,`produit`,`marque`,`article` WHERE `facture`.`IDProduit`=`produit`.`IDProduit` AND `produit`.`IDMarque`=`marque`.`IDMarque` AND `produit`.`IDArticle`=`article`.`IDArticle` AND `idcf`="+thv.getSelectionModel().getSelectedItem().getIdcf());
                try {
                    while (s.next()){
                        list1.add(new tables.thvad(s.getInt("IDProduit"),s.getString("type-article")+" "+s.getString("nom-marque")+" "+s.getString("description"),s.getInt("quantite"),s.getDouble("prixtotal")));
                    }
                } catch (SQLException e) {}
            }
        });

        idcf.setCellValueFactory(new PropertyValueFactory<tables.thv,Integer>("idcf"));
        client.setCellValueFactory(new PropertyValueFactory<tables.thv,String>("client"));
        date.setCellValueFactory(new PropertyValueFactory<tables.thv,String>("date"));
        total1.setCellValueFactory(new PropertyValueFactory<tables.thv,Double>("total1"));
        thv.setItems(list);

        idproduit.setCellValueFactory(new PropertyValueFactory<tables.thvad,Integer>("idproduit"));
        produit.setCellValueFactory(new PropertyValueFactory<tables.thvad,String>("produit"));
        qt.setCellValueFactory(new PropertyValueFactory<tables.thvad,Integer>("qt"));
        total2.setCellValueFactory(new PropertyValueFactory<tables.thvad,Double>("total2"));
        thvad.setItems(list1);
        thv.getSelectionModel().selectFirst();
    }
}
