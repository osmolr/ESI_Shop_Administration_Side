package sample;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
public class db_connection {

    static Connection conn;
    Connection connection;
    Statement statement;
    String SQL;
   
    String url;
    static String username;
    static String password;
    static String Host;
    static String NomBdd;
    static String Port;

    public db_connection() {
        this.url = "jdbc:mysql://"+Host+":"+Port+"/"+NomBdd;
        try {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (Exception e){System.out.println(e.getMessage());}
    }

    public static Statement db_connectionSQLITE() throws ClassNotFoundException, SQLException {

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:idb.sqlite");
            return conn.createStatement();

    }
    public Connection closeconnexion() {
        try {
            connection.close();
        } catch (Exception e) {System.err.println(e);//
        }
        return connection;
    }
    public int exécutionUpdate(String sql) throws SQLException {
           return statement.executeUpdate(sql);
    }
    public Boolean isclosed() throws SQLException {
        return connection.isClosed();
    }
    public ResultSet exécutionQuery(String sql) {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            System.out.println(sql);
        } catch (SQLException ex) {System.err.println(ex);//
        }
        return resultSet;
    }
    public ResultSet SelectAll(String champ,String table){
        ResultSet s=exécutionQuery("SELECT `"+champ+"` FROM `"+table+"`");
        return s;
    }
    public int uploadImageToServer(String fileLocation) throws IOException {
        // l'URL ou je poster la photo
        String postReceiverUrl = "http://127.0.0.1/esishop/upload.php";
        // créé un HttpClient
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(postReceiverUrl);
        //créé un fichier
        File file = new File(fileLocation);
        FileBody fileBody = new FileBody(file);
        //installer le HTTP post
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("fileToUpload", fileBody);
        httpPost.setEntity(reqEntity);
        // executé la requette HTTP post
        HttpResponse response=null;
        response= httpClient.execute(httpPost);
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            String responseStr = EntityUtils.toString(resEntity).trim();
            // you can add an if statement here and do other actions based on the response
            System.out.println(responseStr);
            System.out.println(response.getStatusLine());
        }
        return 0;
    }



    //Fungsi untuk eksekusi query select semua kolom
    public ResultSet querySelectAll(String nomTable) {
        SQL = "SELECT * FROM " + nomTable;
        System.out.println(SQL);
        return this.exécutionQuery(SQL);
    }
/*
//Fungsi untuk eksekusi query select dengan kolom spesifik
    public ResultSet querySelect(String[] nomColonne, String nomTable) {
        int i;
        SQL = "SELECT ";

        for (i = 0; i <= nomColonne.length - 1; i++) {
            SQL += nomColonne[i];
            if (i < nomColonne.length - 1) {
                SQL += ",";
            }
        }
        SQL += " FROM " + nomTable;
        return this.exécutionQuery(SQL);
    }

    public void queryInsert(String nomTable, String[] contenuTableau) {
        int i;
        SQL = "INSERT INTO " + nomTable + " VALUES(";

        for (i = 0; i <= contenuTableau.length - 1; i++) {
            SQL += "'" + contenuTableau[i] + "'";
            if (i < contenuTableau.length - 1) {
                SQL += ",";
            }
        }
        SQL += ")";
        this.exécutionUpdate(SQL);

    }


//Fungsi eksekusi query insert

    public void queryInsert(String nomTable, String[] nomColonne, String[] contenuTableau) {
        int i;
        SQL = "INSERT INTO " + nomTable + "(";
        for (i = 0; i <= nomColonne.length - 1; i++) {
            SQL += nomColonne[i];
            if (i < nomColonne.length - 1) {
                SQL += ",";
            }
        }
        SQL += ") VALUES(";
        for (i = 0; i <= contenuTableau.length - 1; i++) {
            SQL += "'" + contenuTableau[i] + "'";
            if (i < contenuTableau.length - 1) {
                SQL += ",";
            }
        }
        SQL += ")";
        this.exécutionUpdate(SQL);
    }

//Fungsi eksekusi query delete
    public void queryDelete(String nomtable) {
        SQL = "DELETE FROM " + nomtable;
        this.exécutionUpdate(SQL);
    }

//Overload fungsi eksekusi query delete dengan where
    public void queryDelete(String nomTable, String état) {
        SQL = "DELETE FROM " + nomTable + " WHERE " + état;
        this.exécutionUpdate(SQL);
    }*/
}
