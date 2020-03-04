/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgp;

import org.json.JSONObject;
import cgate.ussd.request.CgateUssdRequest;
import java.util.Scanner;


/**
 *
 * @author softmac
 */
public class Main {
    
    public static void main(String[] args) throws Exception{
        CgateUssdRequest cgate = new CgateUssdRequest();
        
        String userName = "ATMUSER";
        String password = "P@ssD12345678";
        String terminalId = "5057AT01";
        String amount = "5070.00";
        String merchantId = "5057AT010000001";
        String transactionType = "30";
        String targetURL = "http://204.8.207.16:8080/ussd-interchange/api/ussd/ussdreference";
        String methodType = "POST";
        String contentType = "text/plain";
        JSONObject obj = new JSONObject();
        JSONObject userInfo = new JSONObject();
        JSONObject posRequest = new JSONObject();
        userInfo.put("userName", userName);
        userInfo.put("password", password);
        posRequest.put("terminalId", terminalId);
        posRequest.put("amount", amount);
        posRequest.put("merchantId", merchantId);
        posRequest.put("transactionType", transactionType);
        obj.put("userInfo", userInfo);
        obj.put("posRequest", posRequest);
        String publicEncryptionKeyFile = "/Users/softmac/Documents/Netbeans Projects/PGP/src/pgp/publickey.asc"; 
        String privateDecryptionKeyFile = "/Users/softmac/Documents/Netbeans Projects/PGP/src/pgp/myprivatekey.asc"; 
        String pwd = "Plmoknijb1@";
        
        String response = cgate.executeUSSDReferenceRequest(userName, password, terminalId, amount, merchantId, transactionType, targetURL, methodType, contentType, publicEncryptionKeyFile, privateDecryptionKeyFile, pwd);
        System.out.println("response:: " + response);
        JSONObject responseObj = new JSONObject(response);
        String tnx = "";
        if (responseObj.getString("responseCode").equals("00")) {
            String reference = responseObj.getString("reference");
            System.out.println("Reference No: " + reference);
            System.out.println("TNX: " + tnx);
            tnx = responseObj.getString("tnx");
            System.out.println("TNX: " + tnx);
            
            System.out.println("Continue to Verify?....Y/N");
            Scanner sc = new Scanner(System.in);
            String in = sc.nextLine();
            if (in.equals("Y")) {
                targetURL = "http://204.8.207.16:8080/coralpay-payment/api/ussd/verifypostedtransaction";
                String confirmResponse = cgate.executeUSSDConfirmRequest(userName, password, terminalId, amount, tnx, merchantId, targetURL, methodType, contentType, publicEncryptionKeyFile, privateDecryptionKeyFile, pwd);
                System.out.println("confirmResponse: " + confirmResponse);
            }else {
                targetURL = "http://204.8.207.16:8080/coralpay-payment/api/ussd/reverseTransaction";
                String reverseTnx = cgate.reverseTransactionRequest(userName, password, terminalId, amount, reference, merchantId, tnx, targetURL, methodType, contentType, publicEncryptionKeyFile, privateDecryptionKeyFile, pwd);
                System.out.println("reverseTnx: " + reverseTnx);
                
                JSONObject newj = new JSONObject(reverseTnx);
            }
        }   
    }
    
}
