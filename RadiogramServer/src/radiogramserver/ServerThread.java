
package radiogramserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerThread extends Thread{
    
    private int id;
    private final Socket servidor;
    private DataInputStream flujoE;
    private DataOutputStream flujoS;
    private boolean run = true;
    private String nombre ="";
    private RadiogramServer server;

    public ServerThread(RadiogramServer server, Socket servidor) {
        this.servidor = servidor;
        this.server = server;
        
        try {
            flujoE = new DataInputStream(servidor.getInputStream());
            flujoS = new DataOutputStream(servidor.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        String text;
        
        while(run){
            try {
                text = flujoE.readUTF();
                    
                if(nombre.equals("")){
                    setNombre(text);
                    server.broadcast(nombre + " entró al chat \n");
                }else{
                
                    if(text.substring(0,2).equals("/p")){
    
                    String name = obtenerNombre(text);      
                    String mensaje = obtenerMessage(text);               
                        server.broadCastPrivate(nombre + " (Priv): " + mensaje + "\n", name);
                    }else{                
                    server.broadcast(nombre + ": " + text+ "\n");
                        
                    }
                    
                    
                }
              
            } catch (IOException ex) {
                try {
                    server.broadcast(nombre + " salió \n");
                    run=false;
                    flujoE.close();
                    flujoS.close();
                    servidor.close();
                } catch (IOException ex1) {
                   
                }
                
             
            }
            

            
        }
    }
    
    public String obtenerNombre(String message){
    String nombreN="";
    int espacios=0;
        for (int i = 0; i < message.length(); i++) {
            if(message.charAt(i)==' '){
            espacios++;
            
            }
            if(espacios==2){
            
               nombreN = message.substring(3,i);
                return nombreN;
            }
            
        }
  
        return "null";
    }
    
    
       public String obtenerMessage(String message){
            String m="";
            int espacios = 0;
            for (int i = 0; i < message.length(); i++) {
               if(message.charAt(i)==' '){
               espacios++;
               }
               if(espacios==2){
               m = message.substring(i+1,message.length());
               return m;
               }
               
           }
            
            return "error";
        }
    
 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
        
    public void send(String text){
        try {
                flujoS.writeUTF(text);
                flujoS.flush();//siempre despues de un write para limpiar el buffer
            } catch (IOException ex) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
           public void setId(int id){
        this.id = id;
    }


  
    
}
