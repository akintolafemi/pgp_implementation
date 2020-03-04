/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.json.JSONObject;
/**
 *
 * @author softmac
 */
public class PGP {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException {
        String stringToEncrypt = "the quick brown fox jumps"; 
        
        String publicKeyFilePath = "/Users/softmac/Documents/Netbeans Projects/PGP/src/pgp/publickey.asc";
        
        String mes = "hQEMAz9XWYaCS7O6AQf+MHhaBbtYIpWuDKoJHhKeJfi20aoTG3RoqEBuKbV/m2r6\n" +
"LSIWZUeodS/SWtYKekMevT5F5BVD4fWHT7ZdORfiagzXYzMaeDFKjg2MRdlH3SMn\n" +
"0Sh+YpJ1sksBIqinhfRazcCVpij9gFINB4YJtlU6JXWr3FBOrzyEOybp1bKeTOu9\n" +
"oDsVcp0WYl87+XHY4M0GgWQKw2yxPF1CkRiqkiP3a8NOye1Bt1kcNAvniOIM/y39\n" +
"Bb+CRkJ0wmwgiu2KRRtCz0EDhK9ELHt63LfVn69FROEQkFJNHSDlbGUqR9SaiWT9\n" +
"8d26rQZQDWXJUYiYC+Ua64OLR1y6F4kGw2Df+Qi3GsmyjXviI99+RJ4rqMKMFQmo\n" +
"vrPjXRDTrUIW5ivhcO2pv2KRKbUkK8SHUjVMdqf/bnq/4w04xx7LkXUJo87WuKyt\n" +
"3WWCFktEGToxnZusHi+yN9y5tqdflspp9XM+TFqO3cowSnFsxQZwiMvGuUQ09TLF\n" +
"g8+Yy6H0I5tnKZzgFUo+H3XGXJi3H+vjSYq3CXJvn7zMUxdj7Uf3SU1XOyzQpQ0q\n" +
"0+l0kohLt4K1cLYFhCcpwekrmQ==\n" +
"=Gu8K";
        
        byte [] mess = mes.getBytes();
        System.out.println(bytesToHex(mess));
        String hex = "85010c03504c32d7780977450107fe2b5f6564907f648b62d3fa9bbc90c3ae81c1d6bdeae1a9d98db7131157bde2f21ea86b7d9e56e4014975a70d720ff6d1722c013c86a664f128a64c3f3b300172411a03e58ed919cf8ebae8bfcff018ff74a6b073338314413149b6866f90f702a8e22ff349716fcf11449cf962db2fda4834116046c6f04fcf7566d9dec9b4055b4079a89bcc6da8a47dd5a526db9444c1d2dcf20d98600ade6e3181a982434fecf7a910b6aec1ded2faa603c6daa4c64848c798624455aaa30946c0df6bf2135d35747f6076da2fd76807f532d03a921fd61545b9f38e11bba2968a4b1d54c457d7f3f913ec4fba30dbe386886faf41c3237cb7316798fbca9793df7c52ff91c94f92448c7540893eab2a36a6dd075c869e26e33239d466e551ccf3e278fb8d748055fee3151a7120bb6d322cfb00ca2abbff65bec22d5ff484159a4b37be8cfc30d3c7b99c6bbb66537a4ac39c70adc0";
        
        byte [] bytes = hexStringToByteArray(hex);
        System.out.println(bytes);
        String ss = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("Output : " + ss);
    }
    
    private static String bytesToHex(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public String executeRequest(String userName, String password, String terminalId, String amount, String merchantId, String transactionType, String targetURL, String methodType, String contentType) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(methodType);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoOutput(true);
            
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
            
            
            String objToString = obj.toString();
            byte [] objToByte = objToString.getBytes();
            
            String hexed = bytesToHex(objToByte);
            System.out.println("dataToSend: " + hexed);
            try (OutputStream wr = connection.getOutputStream()){
                byte[] in = hexed.getBytes("utf-8");
                wr.write(in, 0, in.length);
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder myResponse = new StringBuilder();
            String my_response;
            while ((my_response = rd.readLine()) != null) {
                myResponse.append(my_response);
            }
            return myResponse.toString();
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
}
