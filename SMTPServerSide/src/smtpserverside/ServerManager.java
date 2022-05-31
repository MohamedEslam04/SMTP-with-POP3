/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpserverside;

import java.io.File;

/**
 *
 * @author moham
 */
public class ServerManager {
    
    public void setConnection() {
        
    }
    
    public void createMainFolder() {
      File theDir = new File("/path/directory");
      if (!theDir.exists()){
        theDir.mkdirs();
      }
    }
}
