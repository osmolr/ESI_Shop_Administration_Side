package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by olr on 11/03/2017.
 */
public class Magasinier {
    db_connection db=new db_connection();
    ResultSet s;

    //action view
    public void viderchamp(javafx.scene.control.TextField... t) {
        for (int i = 0; i < t.length; i++) {
            t[i].setText("");
        }
    }
    public void remplirechamp(String[] tab,javafx.scene.control.TextField... t) {
        for (int i = 0; i < t.length; i++) {
            t[i].setText(tab[i]);
        }
    }
    public void openWindow(javafx.event.Event event, String window, String title, int width, int height, boolean hide,boolean Maximazed) throws IOException {
        if (hide) ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(window+".fxml"));
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(Maximazed);
        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();
    }
    public boolean ValiderTextField(TextField[] tf,DatePicker[] dp,TextArea[] ta,ComboBox... cb){
        for(int i=0;i<tf.length;i++)
            if (tf[i].getText().isEmpty()) {
                alertOblierChamp();
                tf[i].setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                tf[i].requestFocus();
                return false;
            }
        for(int i=0;i<dp.length;i++)
            if (dp[i].getValue()==null) {
                alertOblierChamp();

                dp[i].setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                dp[i].requestFocus();
                return false;
            }
        for(int i=0;i<ta.length;i++)
            if (ta[i].getText().isEmpty()) {
                alertOblierChamp();

                ta[i].setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                ta[i].requestFocus();
                return false;
            }
        for(int i=0;i<cb.length;i++)
            if (cb[i].getSelectionModel().getSelectedItem().equals("")) {
                alertOblierChamp();

                cb[i].setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
                cb[i].requestFocus();
                return false;
            }


        return true;
    }
    private void NumiricTextfield(TextField tf){
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,10}([\\.]\\d{0,4})?")) {
                    tf.setText(oldValue);
                }
            }
        });
    }
    public void MultiTextfieldNumiric(TextField... tf){
        for (int i=0;i<tf.length;i++) {
            NumiricTextfield(tf[i]);

        }}
    public void Desactive(TextField[] tf,DatePicker[] dp,TextArea[] ta,Button[] bt,ComboBox... cb){
        for(int i=0;i<tf.length;i++){
            tf[i].setDisable(true);
        }
        for(int i=0;i<dp.length;i++){
            dp[i].setDisable(true);
        }
        for(int i=0;i<ta.length;i++){
            ta[i].setDisable(true);
        }
        for(int i=0;i<cb.length;i++){
            cb[i].setDisable(true);
        }
        for(int i=0;i<bt.length;i++){
            bt[i].setDisable(true);
        }
    }
    public void DesactiveTableau(TableView... tv){
        for(int i=0;i<tv.length;i++){
            tv[i].setDisable(true);
        }
    }
    public boolean OblierSelectione(TableView tv,String critereoblier){
        if(tv.getSelectionModel().getSelectedItem()!=null) return false;
        else {
            alert(Alert.AlertType.ERROR,"Sélection","vous n'avez pas sélectionné "+critereoblier);
            tv.requestFocus();
            tv.setStyle("-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%);");
            return true;
        }

    }
    public boolean ConfirmationSupp(String typeoperation,String titre,String critere){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/interfaces/sample.css").toExternalForm());
        dialogPane.getStyleClass().add("sample");
        dialogPane.setHeaderText(null);
        dialogPane.setContentText("Voulez vous vraiment "+typeoperation+" "+critere);
        Optional<ButtonType> answer =alert.showAndWait();

        if (answer.get()==ButtonType.OK) return true;else return false;
    }


    public boolean CheckDb(ActionEvent event,Label tx) {
        boolean cheked =false;

        try {
            db_connection db=new db_connection();
            if (!db.isclosed()){ tx.setText("CONNECTE");
                                    cheked=true;}

        } catch (Exception e) {
            tx.setText("ERREUR");
        }
        return cheked;
    }
    public void ModifierQuantite(Event event,String type,TableView<tables.tvp> tvp, ObservableList<tables.tvp> list1, javafx.scene.control.TextField taux, javafx.scene.control.TextField quantite){
        int index =tvp.getSelectionModel().getSelectedIndex();
        int N=tvp.getSelectionModel().getSelectedItem().getN();
        String P=tvp.getSelectionModel().getSelectedItem().getProduit();
        int Q;
        Double pr=tvp.getSelectionModel().getSelectedItem().getPrix();
        Double R=tvp.getSelectionModel().getSelectedItem().getRemise();
        Double T;
        if (type.equals("plus")){
            Q=tvp.getSelectionModel().getSelectedItem().getQt()+Integer.valueOf(quantite.getText());
            T=tvp.getSelectionModel().getSelectedItem().getTotal()+pr*Integer.valueOf(quantite.getText());
            taux.setText(String.valueOf(Double.valueOf(taux.getText())+Integer.valueOf(quantite.getText())*pr));}
        else{
            Q=tvp.getSelectionModel().getSelectedItem().getQt()-Integer.valueOf(quantite.getText());
            T=tvp.getSelectionModel().getSelectedItem().getTotal()-pr*Integer.valueOf(quantite.getText());
            taux.setText(String.valueOf(Double.valueOf(taux.getText())-Integer.valueOf(quantite.getText())*pr));
        }
        list1.set(index,new tables.tvp(N,P,Q,pr,R,T));
        quantite.setText("1");
    }
    public boolean Sauver_restaurer(ActionEvent event, boolean sauver, ArrayList<tables.tvp> tab, ObservableList<tables.tvp> list1, javafx.scene.control.TextField taux, javafx.scene.control.TextField recu, javafx.scene.control.TextField remisev,int idc,Label cs) throws SQLException {
        if (!sauver) {
            for (int i = 0; i < list1.size(); i++) {
                tab.add(list1.get(i));
            }
            list1.clear();
            viderchamp(taux,recu,remisev);
            cs.setText("non séléctioné");
            return true;
        }else{
            list1.clear();
            for (int i=0;i<tab.size();i++) list1.add(tab.get(i));
            taux.setText(String.valueOf(CalculerTotalVente(list1)));
            tab.clear();
            s=db.exécutionQuery("SELECT * FROM `client` WHERE `IDClient`="+idc);
            s.next();
            cs.setText(s.getString("nom")+" "+s.getString("prenom"));

            return  false;
        }
    }
    public double CalculerTotalVente(ObservableList<tables.tvp> list1){
        double total=0;
        for (tables.tvp i:list1){
            total=total+i.getTotal();
        }
        return total;
    }
    public double CalculerTotalFacture(ObservableList<tables.thv> list1){
        double total=0;
        for (tables.thv i:list1){
            total=total+i.getTotal1();
        }
        return total;
    }
    public void InitialiserComboBox(ObservableList<String> list,String champ,String table,ComboBox<String>... combo){
        ResultSet s=db.SelectAll(champ,table);
        list.add("");
        try {
            while (s.next())
            {
                list.add(s.getString(champ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i=0;i<combo.length;i++){
        combo[i].setItems(list);
        combo[i].getSelectionModel().selectFirst();}
    }
    public void time(SimpleDateFormat sdf, javafx.scene.control.TextField text){
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Calendar cal = Calendar.getInstance();
                        text.setText(sdf.format(cal.getTime()));
                    }
                }, 0, 1000);
    }
    public void ConsulterNotification(javafx.scene.control.TextField noti,String type){
        ResultSet s=null;
        if (type.equals("nouveau")) s=db.exécutionQuery("SELECT * FROM `notification` WHERE `vu`=0");
        else if (type.equals("ancient")) s=db.SelectAll("*","notification");

        int i=0;
        try {while (s.next()) i++;} catch (SQLException e) {}
        if (type.equals("nouveau")) noti.setText(i+" Nouveau Notification");
        else if (type.equals("ancient")) noti.setText(i+" Notification");
    }
    public int RecupererLeDernierChaqueId(String tableau,String champ) throws SQLException {
        ResultSet s;
        int Cid=0;
        s=db.exécutionQuery("SELECT * FROM `"+tableau+"` ORDER BY `"+champ+"` DESC");
        if (s.next()){Cid=s.getInt(champ)+1;}
        return Cid;
    }
    public void alert(Alert.AlertType type,String titre,String contenue){
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/interfaces/sample.css").toExternalForm());
        dialogPane.getStyleClass().add("sample");
        alert.setContentText(contenue);
        alert.showAndWait();
    }
    public ObservableList<String> returnMarqueSansFournisseur() throws SQLException {

        HashSet<Integer> l=new HashSet<Integer>();
        ObservableList<String> l1=FXCollections.observableArrayList();
        l1.add("");
        ResultSet s;
        s=db.exécutionQuery("SELECT * FROM `fournisseur` NATURAL JOIN `marque` WHERE `supprimerF`='0' AND `supprimerM`='0'");
        while(s.next()){
            l.add(s.getInt("IDMarque"));
        }
        s=db.exécutionQuery("SELECT * FROM `marque`");
        while (s.next()){
            if (!l.contains(s.getInt("IDMarque"))) l1.add(s.getString("nom-marque"));
        }
        return l1;
    }
    public int returnIdParChamp(String tableau,String champ,String nomdechamp) throws SQLException {
        ResultSet s;
        s = db.exécutionQuery("SELECT * FROM `"+tableau+"` WHERE `"+champ+"`='" + nomdechamp + "'");
        if (s.next());
        return s.getInt(1);
    }
    public void InsererImage() throws SQLException, IOException {
        s=db.exécutionQuery("SELECT `IDProduit`,`IDcp` FROM `produit` ORDER BY `IDProduit` DESC");
        s.next();
        File f=new File("a.jpg");
        File f1=new File(s.getInt("IDcp")+"_"+s.getInt("IDProduit")+".jpg");
        f.renameTo(f1);
        db.uploadImageToServer(s.getInt("IDcp")+"_"+s.getInt("IDProduit")+".jpg");
        db.exécutionUpdate("UPDATE `produit` SET `img`='"+s.getInt("IDcp")+"_"+s.getInt("IDProduit")+".jpg' WHERE `IDProduit`="+s.getInt("IDProduit"));
        f1.delete();
        f.delete();
    }
    public void FiltrerFacture(int idcf1,double total3,ObservableList list,ResultSet s,ResultSet ss,String from ,String to,Boolean withFilter){
        String sql=null;
        if (withFilter) sql="SELECT * FROM `facture` NATURAL JOIN `client` WHERE `date`  BETWEEN '"+from+"' AND '"+to+"' ORDER BY `date` DESC";
        else sql="SELECT * FROM `facture` NATURAL JOIN `client` ORDER BY `date` DESC";
        s=db.exécutionQuery(sql);
        try {
            while (s.next() ){
                if(s.getInt("idcf")!=idcf1) {
                    ss=db.exécutionQuery("SELECT * FROM `facture` WHERE `idcf`="+s.getInt("idcf"));
                    while (ss.next()){total3+=ss.getDouble("prixtotal");}
                    list.add(new tables.thv(s.getInt("idcf"), s.getString("nom") + " " + s.getString("prenom"), s.getString("date"), total3));
                    total3=0;
                }
                idcf1=s.getInt("idcf");
            }
        } catch (SQLException e) {}
    }
    public void alertOblierChamp() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("champs vide");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/interfaces/sample.css").toExternalForm());
        dialogPane.getStyleClass().add("sample");
        alert.setContentText("veuillez remplir tous les champs obligatoires");
        alert.showAndWait();
    }
    public void RestaurerStyleDesChampsOblier(TextField[] tf,DatePicker[] dp,TextArea[] ta,ComboBox... cb){
        for(int i=0;i<tf.length;i++){
            tf[i].setStyle("");
        }
        for(int i=0;i<dp.length;i++){
            dp[i].setStyle("");
        }
        for(int i=0;i<ta.length;i++){
            ta[i].setStyle("");
        }
        for(int i=0;i<cb.length;i++){
            cb[i].setStyle("");
        }
    }
    public void Noti(TextField noti){
        //acctualisation des notification
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {//new notification
                        ConsulterNotification(noti,"nouveau");
                    }
                }, 0, 10000);
    }

}




