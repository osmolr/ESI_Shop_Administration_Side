package sample;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by olr on 13/03/2017.
 */
public class tables {
    public static class tac{
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty produit;
        private final SimpleStringProperty article;
        private final SimpleStringProperty marque;
        private final SimpleStringProperty couleur;
        private final SimpleStringProperty taille;
        private final SimpleDoubleProperty prix;
        private final SimpleIntegerProperty qt;
        private final SimpleDoubleProperty total;
        private final SimpleStringProperty Fournisseur;
        private final SimpleStringProperty d;
        private final SimpleStringProperty etat;

        public tac(int id,String produit,String article,String marque,String couleur,String taille,double prix,int qt,double total,String Fournisseur,String d,String etat) {
            this.id =new SimpleIntegerProperty(id);
            this.produit =new SimpleStringProperty(produit);
            this.article =new SimpleStringProperty(article);
            this.marque =new SimpleStringProperty(marque);
            this.couleur =new SimpleStringProperty(couleur);
            this.taille =new SimpleStringProperty(taille);
            this.prix =new SimpleDoubleProperty(prix);
            this.qt =new SimpleIntegerProperty(qt);
            this.total =new SimpleDoubleProperty(total);
            this.Fournisseur =new SimpleStringProperty(Fournisseur);
            this.d =new SimpleStringProperty(d);
            this.etat =new SimpleStringProperty(etat);
        }
        public int getId() {return id.get();}
        public String getProduit() {
            return produit.get();
        }
        public String getArticle() {
            return article.get();
        }
        public String getMarque() {
            return marque.get();
        }
        public String getCouleur() {
            return couleur.get();
        }
        public String getTaille() {
            return taille.get();
        }
        public double getPrix() {
            return prix.get();
        }
        public int getQt() {
            return qt.get();
        }
        public double getTotal() {
            return total.get();
        }
        public String getFournisseur() {
            return Fournisseur.get();
        }
        public String getD() {
            return d.get();
        }
        public String getEtat() {
            return etat.get();
        }}
    public static class tvp{
        private  SimpleIntegerProperty n;
        private final SimpleStringProperty produit;
        private final SimpleIntegerProperty Qt;
        private final SimpleDoubleProperty prix;
        private final SimpleDoubleProperty remise;
        private final SimpleDoubleProperty total;

        public tvp(int n,String produit,int qt,double prix,double remise,double total) {
            this.n =new SimpleIntegerProperty(n);
            this.produit =new SimpleStringProperty(produit);
            this.Qt =new SimpleIntegerProperty(qt);
            this.prix =new SimpleDoubleProperty(prix);
            this.remise =new SimpleDoubleProperty(remise);
            this.total=new SimpleDoubleProperty(total);
        }
        public int getN() {return n.get();}
        public String getProduit() {
            return produit.get();
        }
        public int getQt() {
            return Qt.get();
        }
        public double getPrix() {
            return prix.get();
        }
        public double getRemise() {
            return remise.get();
        }
        public double getTotal() {
            return total.get();
        }

    }
    public static class tm{
        private  final  SimpleIntegerProperty idmarque;
        private final SimpleStringProperty marque;

        public tm(int idmarque,String marque) {
            this.marque =new SimpleStringProperty(marque);
            this.idmarque =new SimpleIntegerProperty(idmarque);
            }
        public int getIdmarque() {
            return idmarque.get();
        }
        public String getMarque() {
            return marque.get();
        }
    }
    public static class tmdf{
        private final SimpleStringProperty marque;

        public tmdf(String marque) {
            this.marque =new SimpleStringProperty(marque);
        }
        public String getMarque() {
            return marque.get();
        }
    }
    public static class ta{
        private  final  SimpleIntegerProperty idarticle;
        private final SimpleStringProperty article;

        public ta(int idarticle,String article) {
            this.article =new SimpleStringProperty(article);
            this.idarticle =new SimpleIntegerProperty(idarticle);
        }
        public String getArticle() {
            return article.get();
        }
        public int getIdarticle() {
            return idarticle.get();
        }
    }
    public static class tco{
        private  final  SimpleIntegerProperty idcouleur;
        private final SimpleStringProperty couleur;

        public tco(int idcouleur,String couleur) {
            this.idcouleur =new SimpleIntegerProperty(idcouleur);
            this.couleur =new SimpleStringProperty(couleur);
        }
        public int getIdcouleur() {
            return idcouleur.get();
        }
        public String getCouleur() {
            return couleur.get();
        }
    }
    public static class tc{
        private  final  SimpleIntegerProperty idclient;
        private final SimpleStringProperty nom;
        private final SimpleStringProperty prenom;
        private final SimpleStringProperty ntel;
        private final SimpleStringProperty email;
        private final SimpleStringProperty ville;
        private final SimpleStringProperty adr;
        private final SimpleIntegerProperty cp;
        private final SimpleStringProperty nometu;
        private final SimpleStringProperty date;
        private final SimpleStringProperty CBc;
        private final SimpleDoubleProperty pointF;

        public tc(int idclient,String nom,String prenom,String ntel,String email,String ville,String adr,int cp,String nometu,String date,String CBc,double pointF)
        {
            this.idclient=new SimpleIntegerProperty(idclient);
            this.nom=new SimpleStringProperty(nom);
            this.prenom=new SimpleStringProperty(prenom);
            this.ntel=new SimpleStringProperty(ntel);
            this.email=new SimpleStringProperty(email);
            this.ville=new SimpleStringProperty(ville);
            this.adr=new SimpleStringProperty(adr);
            this.cp=new SimpleIntegerProperty(cp);
            this.nometu=new SimpleStringProperty(nometu);
            this.date=new SimpleStringProperty(date);
            this.CBc=new SimpleStringProperty(CBc);
            this.pointF=new SimpleDoubleProperty(pointF);
        }
        public int getIdclient() {return idclient.get();}
        public String getNom() {return nom.get();}
        public String getPrenom() {return prenom.get();}
        public String getNtel() {return ntel.get();}
        public String getEmail() {return email.get();}
        public String getVille() {return ville.get();}
        public String getAdr() {return adr.get();}
        public int getCp() {return cp.get();}
        public String getNometu() {return nometu.get();}
        public String getDate() {return date.get();}
        public String getCBc() {return CBc.get();}
        public double getPointF() {return pointF.get();}

    }
    public static class tp{
        private  final  SimpleIntegerProperty idproduit;
        private final SimpleStringProperty cb;
        private final SimpleStringProperty article;
        private final SimpleIntegerProperty qt;
        private final SimpleStringProperty marque;
        private final SimpleStringProperty couleur;
        private final SimpleStringProperty taille;
        private final SimpleDoubleProperty pa;
        private  final  SimpleDoubleProperty pv;
        private  final  SimpleDoubleProperty remise;
        private final SimpleDoubleProperty tva;
        private final SimpleStringProperty img;

        public tp(int idproduit, String cb, String article, int qt, String marque, String couleur, String taille, Double pa, Double pv,Double remise, Double tva, String img) {
            this.idproduit = new SimpleIntegerProperty(idproduit);
            this.cb =new SimpleStringProperty(cb);
            this.article =new SimpleStringProperty( article);
            this.qt =new SimpleIntegerProperty(qt);
            this.marque =new SimpleStringProperty( marque);
            this.couleur =new SimpleStringProperty( couleur);
            this.taille =new SimpleStringProperty( taille);
            this.pa =new SimpleDoubleProperty(pa);
            this.pv =new SimpleDoubleProperty(pv);
            this.remise =new SimpleDoubleProperty(remise);
            this.tva =new SimpleDoubleProperty(tva);
            this.img =new SimpleStringProperty( img);
        }

        public int getIdproduit() {return idproduit.get();}
        public String getCb() {return cb.get();}
        public String getArticle() {return article.get();}
        public int getQt() {return qt.get();}
        public String getMarque() {return marque.get();}
        public String getCouleur() {return couleur.get();}
        public String getTaille() {return taille.get();}
        public double getPa() {return pa.get();}
        public double getPv() {return pv.get();}
        public double getRemise() {return remise.get();}
        public double getTva() {return tva.get();}
        public String getImg() {return img.get();}
    }
    public static class tf{
        private final SimpleIntegerProperty idf;
        private final SimpleStringProperty nom;
        private final SimpleStringProperty prenom;
        private final SimpleStringProperty ntel;
        private final SimpleStringProperty nregcom;
        private final SimpleStringProperty fax;
        private final SimpleStringProperty nometu;
        private final SimpleStringProperty mdp;


        public tf(int idf, String nom, String prenom, String ntel, String nregcom, String fax, String nometu, String mdp) {
            this.idf =new SimpleIntegerProperty(idf);
            this.nom =new SimpleStringProperty(nom);
            this.prenom =new SimpleStringProperty(prenom);
            this.ntel =new SimpleStringProperty(ntel);
            this.nregcom =new SimpleStringProperty(nregcom);
            this.fax =new SimpleStringProperty(fax);
            this.nometu =new SimpleStringProperty(nometu);
            this.mdp =new SimpleStringProperty(mdp);
        }
        public int getIdf() {return idf.get();}
        public String getNom() {return nom.get();}
        public String getPrenom() {return prenom.get();}
        public String getNtel() {return ntel.get();}
        public String getNregcom() {return nregcom.get();}
        public String getFax() {return fax.get();}
        public String getNometu() {return nometu.get();}
        public String getMdp() {return mdp.get();}
    }
    public static class tn{
        private  final  SimpleIntegerProperty idnotification;
        private final SimpleStringProperty fournisseur;
        private final SimpleStringProperty produit;
        private final SimpleStringProperty titre;
        private final SimpleStringProperty description;

        public tn(int idnotification,String fournisseur,String produit,String titre,String description) {
            this.idnotification =new SimpleIntegerProperty(idnotification);
            this.fournisseur =new SimpleStringProperty(fournisseur);
            this.produit =new SimpleStringProperty(produit);
            this.titre =new SimpleStringProperty(titre);
            this.description =new SimpleStringProperty(description);
        }
        public int getIdnotification() {return idnotification.get();}
        public String getFournisseur() {return fournisseur.get();}
        public String getProduit() {return produit.get();}
        public String getTitre() {return titre.get();}
        public String getDescription() {return description.get();}
    }
    public static class thv{
        private final SimpleIntegerProperty idcf;
        private final SimpleStringProperty client;
        private final SimpleStringProperty date;
        private final SimpleDoubleProperty total1;

        public thv(int idcf,String client,String date,double total1) {
            this.idcf =new SimpleIntegerProperty(idcf);
            this.client =new SimpleStringProperty(client);
            this.date =new SimpleStringProperty(date);
            this.total1=new SimpleDoubleProperty(total1);
        }
        public int getIdcf() {return idcf.get();}
        public String getClient() {return client.get();}
        public String getDate() {return date.get();}
        public double getTotal1() {
            return total1.get();
        }

    }
    public static class thvad{
        private final SimpleIntegerProperty idproduit;
        private final SimpleStringProperty produit;
        private final SimpleIntegerProperty qt;
        private final SimpleDoubleProperty total2;

        public thvad(int idproduit,String client,int qt,double total2) {
            this.idproduit =new SimpleIntegerProperty(idproduit);
            this.produit =new SimpleStringProperty(client);
            this.qt =new SimpleIntegerProperty(qt);
            this.total2=new SimpleDoubleProperty(total2);
        }
        public int getIdproduit() {return idproduit.get();}
        public String getProduit() {return produit.get();}
        public int getQt() {return qt.get();}
        public double getTotal2() {
            return total2.get();
        }

    }

}
