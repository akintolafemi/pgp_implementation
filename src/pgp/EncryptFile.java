/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgp;

/**
 *
 * @author softmac
 */
import com.didisoft.pgp.PGPLib;
import java.io.FileInputStream;
import java.io.InputStream;

public class EncryptFile {
    
    public static void main(String[] args) throws Exception{
        PGPLib pgp = new PGPLib();
//        boolean asciiArmor = false;
//        boolean withIntegrityCheck = false;
//        
        String message = "hello world";
        
        //String publicEncryptionKeyFile = "/Users/softmac/Documents/Netbeans Projects/PGP/src/pgp/publickey.asc";        
        String publicEncryptionKeyFile = "/Users/softmac/Documents/Netbeans Projects/PGP/src/pgp/mypublickey.asc"; 
        String publicDecryptionKeyFile = "/Users/softmac/Documents/Netbeans Projects/PGP/src/pgp/myprivatekey.asc"; 
        String pwd = "Plmoknijb1@";
        InputStream publicEncryptionKeyStream = null;
        try {
            publicEncryptionKeyStream = new FileInputStream(publicEncryptionKeyFile);

            // encrypt
            String encryptedString = pgp.encryptString(message, publicEncryptionKeyStream);
            System.out.println("encryptedString: " + encryptedString);
            
            String decryptedString = pgp.decryptString(encryptedString, publicDecryptionKeyFile, pwd);
            System.out.println("decryptedString: " + decryptedString);
           } 
        finally {
            if (publicEncryptionKeyStream != null)
               publicEncryptionKeyStream.close();
        }  
    }
    
}