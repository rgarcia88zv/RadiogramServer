/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package radiogramserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RadiogramServer {

  private ServerSocket servicio;
  
    private boolean run = true;
    private List<ServerThread> serverThreads = new ArrayList<>();

    public RadiogramServer(int port) {
        
          try{
            
             servicio = new ServerSocket(port);
      
            
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        
    }
    
        public void startService(){
        
        Thread hebraprincipal = new Thread(){
            @Override
            public void run() {
                Socket servidor;
                ServerThread svthread;

                while(run){
                    try {
                       
                        servidor = servicio.accept();         
                        svthread = new ServerThread(RadiogramServer.this, servidor);                        
                        serverThreads.add(svthread);
                        svthread.setId(serverThreads.indexOf(svthread));
                        svthread.start();
       
                     
                        
                    } catch (IOException ex) {
                        Logger.getLogger(RadiogramServer.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
            
        };
        
        hebraprincipal.start();
        
    }
    
    
    public static void main(String[] args) {
          RadiogramServer chatServer = new RadiogramServer(5000);
        chatServer.startService();
    }
    
           public void broadcast(String text){
               
        for (ServerThread client: serverThreads) {
            client.send(text);
        }
    }
           
         public void broadCastPrivate(String mensaje, String nombre){
             for (int i = 0; i < serverThreads.size(); i++) {
                 if(serverThreads.get(i).getNombre().equals(nombre)){
                 
                 serverThreads.get(i).send(mensaje);
                 
                 }
                 
             }
             
         
         }  
         
         
         


    
    
}
