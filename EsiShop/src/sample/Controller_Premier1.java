package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by olr on 12/05/2017.
 */
public class Controller_Premier1 implements Initializable {
    @FXML TextField userBdd,adresseip,port,nombdd,mdpbdd;
    @FXML Label tx;
    @FXML Button suivant;
    Magasinier m=new Magasinier();

    public void check(ActionEvent event){
        if (!m.ValiderTextField(new TextField[]{adresseip,port,nombdd,userBdd},new DatePicker[]{},new TextArea[]{})) return ;

        db_connection.Host=adresseip.getText();
        db_connection.Port=port.getText();
        db_connection.NomBdd=nombdd.getText();
        db_connection.username=userBdd.getText();
        db_connection.password=mdpbdd.getText();

        if(m.CheckDb(event,tx)) suivant.setDisable(false);
    }
    public void Suivant(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        Statement statement;
        statement =db_connection.db_connectionSQLITE();
        statement.executeUpdate ("UPDATE info SET IPAdresse = '"+adresseip.getText()+"', PORT ='"+port.getText()+"' , DBName ='"+nombdd.getText()+"' , DBPWD ='"+mdpbdd.getText()+"' , DBUserName ='"+userBdd.getText()+"', First=0 " );

        m.openWindow(event,"/interfaces/premier2","Etape 2",600,600,true,false);
    }
    public void defaultTheme(Event event){
        m.RestaurerStyleDesChampsOblier(new TextField[]{adresseip,port,nombdd,mdpbdd,userBdd},new DatePicker[]{},new TextArea[]{});
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        suivant.setDisable(true);
    }
}
