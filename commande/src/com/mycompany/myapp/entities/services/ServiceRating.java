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
import com.mycompany.myapp.entities.Rating;
import com.mycompany.myapp.utils.Statics;
import java.io.ByteArrayInputStream;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
//import javax.swing.text.Document;


/**
 *
 * @author Andrew
 */
public class ServiceRating {
    ConnectionRequest req;
    public ArrayList<Rating> Ratingss;
    
    public boolean resultOk;
    //2  creer un attribut de type de la classe en question (static)
    public static ServiceRating instance = null;
    
    
    //Singleton => Design Pattern qui permet de creer une seule instance d'un objet 
    
    
    //1 rendre le constructeur private
    private ServiceRating() {
        req = new ConnectionRequest();
    }
    
    
    //3 la methode qu'elle va ramplacer le constructeur 
    public static ServiceRating getinstance(){
        if(instance == null){
            instance = new ServiceRating();    
        }
        return instance;
    }
    


    
        public boolean updateRatings(Rating e){     
        String url = Statics.URL+"commande/mobile/updaterating?idcommande="+e.getIdcommande()+"&iduser="+e.getIduser()+"&rating="+e.getRating();
        
        
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
    
    
    
    //methode d'ajout
    public boolean addRatings(Rating e){     
        String url = Statics.URL+"commande/mobile/rating?idcommande="+e.getIdcommande()+"&iduser="+e.getIduser()+"&rating="+e.getRating();
        
        
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
    
 
        public ArrayList<Rating> parseallIDRatings(String jsonText){
                try {

                    System.out.println(jsonText);
            Ratingss=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> tasksListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            java.util.List<Map<String,Object>> list = (java.util.List<Map<String,Object>>)tasksListJson.get("root");
            for(Map<String,Object> obj : list){
                Rating a = new Rating();
                  
                
                
                if (obj.get("idcommande").toString() != null) {
                   a.setIdcommande((int)Float.parseFloat(obj.get("idcommande").toString()));
                }
                
                
                        if (obj.get("rating").toString() != null) {
                   a.setRating((int)Float.parseFloat(obj.get("rating").toString()));
                }
                        
                                                if (obj.get("iduser").toString() != null) {
                   a.setIduser((int)Float.parseFloat(obj.get("iduser").toString()));
                }


             
                Ratingss.add(a);
            }
        } catch (IOException ex) {
            
        }
        return Ratingss;
    }
    public ArrayList<Rating> getAllidRatings(int idUser){
    String url = Statics.URL + "commande/mobile/check_rating?iduser=" + idUser;
        req.setUrl(url);
        req.addResponseListener(new com.codename1.ui.events.ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                     if (req.getResponseData() != null) {
                Ratingss = parseallIDRatings(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
            }
        });
        com.codename1.io.NetworkManager.getInstance().addToQueueAndWait(req);
        
            if (Ratingss == null) {
        Ratingss = new ArrayList<>();
    }
        return Ratingss;
    }
}
