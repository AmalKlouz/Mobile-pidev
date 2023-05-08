/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myapp.entities.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.Storage;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.List;
import com.codename1.ui.events.ActionListener;
import com.codename1.util.Base64;
import com.mycompany.myapp.entities.Commande;
import com.mycompany.myapp.utils.Statics;
import java.io.ByteArrayInputStream;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
//import javax.swing.text.Document;


/**
 *
 * @author Andrew
 */
public class ServiceCommande {
    ArrayList<Commande> Commandes ;
    ConnectionRequest req;
    
    public boolean resultOk;
    //2  creer un attribut de type de la classe en question (static)
    public static ServiceCommande instance = null;
    
    
    //Singleton => Design Pattern qui permet de creer une seule instance d'un objet 
    
    
    //1 rendre le constructeur private
    private ServiceCommande() {
        req = new ConnectionRequest();
    }
    
    
    //3 la methode qu'elle va ramplacer le constructeur 
    public static ServiceCommande getinstance(){
        if(instance == null){
            instance = new ServiceCommande();    
        }
        return instance;
    }
    
    
    
    
    
    
    
    
    
    
    //methode d'ajout
    public boolean addCommande(Commande e){
        String nom=e.getNom();
        String type=e.getType();
        String editeur=e.getEditeur();
        
        String url = Statics.URL+"commande/mobile/create?nom=" + nom + "&type=" + type + "&editeur=" + editeur;
        
        
        req.setUrl(url);
        //GET =>
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOk = req.getResponseCode() == 200; //si le code return 200 
                //
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOk;
        
        
        
    }
    
 public boolean UpdateCommande(Commande e){
        String nom=e.getNom();
        String type=e.getType();
        String editeur=e.getEditeur();
        int id = e.getId();
        
        String url = Statics.URL+"commande/mobile/update/"+id+"?nom=" + nom + "&type=" + type + "&editeur=" + editeur;
        
        
        req.setUrl(url);
        //GET =>
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOk = req.getResponseCode() == 200; //si le code return 200 
                //
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOk;
        
        
        
    }
    
    
    public boolean deleteCommande(int id) {
    String url =  Statics.URL+"commande/mobile/delete/" + id;
    ConnectionRequest request = new ConnectionRequest(url);
    request.setHttpMethod("DELETE");

    request.addResponseListener(e -> {
      
        resultOk = request.getResponseCode() == 200;
        
       
    });

    NetworkManager.getInstance().addToQueue(request);
            return resultOk;

}
    
    public ArrayList<Commande> parseCommandes(String jsonText) {
        try {
            Commandes = new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String, Object> CommandesListJson
                    = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));

            java.util.List<Map<String, Object>> list = (java.util.List<Map<String, Object>>) CommandesListJson.get("root");
            for (Map<String, Object> obj : list) {
                Commande t = new Commande();
                float id = Float.parseFloat(obj.get("id").toString());
                t.setId((int) id);
               // t.setStatus(((int) Float.parseFloat(obj.get("status").toString())));
                if (obj.get("nomdocument") == null) {
                    t.setNom("null");
                } else {
                    t.setNom(obj.get("nomdocument").toString());
                }
                  if (obj.get("type") == null) {
                    t.setType("null");
                } else {
                    t.setType(obj.get("type").toString());
                }
                    if (obj.get("editeur") == null) {
                    t.setEditeur("null");
                } else {
                    t.setEditeur(obj.get("editeur").toString());
                }
                
                Commandes.add(t);
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return Commandes;
    }
    
    
    
    //methode d'affichage
     public ArrayList<Commande> getAllCommandes(){
          String url = Statics.URL+"commande/AllCommandes";
          req.setUrl(url);
          req.setPost(false);
          req.addResponseListener(new ActionListener<NetworkEvent>() {
              @Override
              public void actionPerformed(NetworkEvent evt) {
                  Commandes = parseCommandes(new String(req.getResponseData()));
                  req.removeResponseListener(this);
              }
          });
          NetworkManager.getInstance().addToQueueAndWait(req);
         
         
         return Commandes;
     }
    
}
