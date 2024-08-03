package com.example.teacher_panel_application.Access;

import android.util.Log;

import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import com.google.auth.oauth2.GoogleCredentials;
public class AccessToken {

    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";
    public String getAccessToken(){

        try {

            String jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"teacherpanelapp\",\n" +
                    "  \"private_key_id\": \"0be3a81306210ed49d90341de88343c6861456d5\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCXXMRNKeWzLx9q\\nEFkIa4w74XIsUIJtVtB7HMWERT6VPMm3WJgnRAmOiAFykiW5LGbQ2eBtHNH9m5uc\\n69JBRK4XOeW5+xdqJRlqnUvPaxtWQfUNFb3Wt/P4QLfoYTp7GQkqSFYc0pv6u7+Y\\nE8ffRXMW1nFtjwWDOk8thqaZ7heSQGbkZ36sYRevk042FkpEuTqfBnFBXokwBqMz\\nUx3BYIe7p13DZY/Oa2t15wymEoD1VlwQ38v0IjNtZC9AGFIuBMfi54OC8yBkOcpC\\neuy8GPDQuzVpsB0I+YTqXszX2j39SSr1P6DearzEiZAxs9XRzxRr2PsbAzsJdHfV\\njfgS7+qDAgMBAAECggEABhmNDnNs5QrPnPvtEpuyI/gJNiosCQSYINxlq1ejcFE2\\nuEBTDwLWncG//GJRlqpO6tJbXhpTIU5j9+/a+81gQXuQVS1+YcUxk1Kk3pMfuWfg\\n3JBu1st4AtFDROA/ApbDzOPrluLEuj1WhcyHEzZ4Ly66LuLPvPYDGqZHNx0Mu+4m\\n0BSSpflFVTjcqoIk433R5OjKW00qiOU+u01+gYhnIWsYJ5RnWSit5AxdOm2w7A1S\\nG/hp1tkGOxxQXWRmF3AyF6MpZW+79BZYSDLahY4ycWcSPV/2erctzfV8C2Hp+Cla\\nj2J03c8n5SpOd04cdDsFtf1wgfyEe8kv5+JqTTQ4vQKBgQDM2PiIE3GllA4tjfID\\nMPqPp22xPKd2UVXpg/5C4DheUSfB2ITNOO5Njea9RQaainwPDdNeL3V6U3zAr944\\nBPq5oGvn6lDfScsyldj0OLFYIEJuI/EuE+5WbcMQpzwsM9g1mkVJnoat9toKz7hm\\nzfSqQHKtrBkmauwTua0o/LOSfwKBgQC9KLeZbKf6pwpjGs7fnvugyWlOdPKG0giH\\n7St8FvifGwOXMxWrzKr9F8+k5okgudObVOc0/ac9KfryNlGg5e052HnIQu0Lws7d\\nmsjY3XDEH5cVh1jrdcyGSBpQlQpOL5fB0vSUCYpZbWLdxX3z8xYyWz+tJgBvc46i\\nVKDTxsBd/QKBgALKjhBkm27TJqJORdbvSuq8ME1y8yE0mIQXDVbh8UKvvmfwyFZ3\\niR+7aEJ5904RxwD5gkDNSr+A9bk2bEO9JaFz4ySE19dDDSbCTfN99kSqI92WVcn3\\n8Y5x7m+Cp9o+VprcYEdRhqpGK9Br5ipYchYDzMw7BRi16kOWixp83wGHAoGADZyc\\neaHkBJl4meEX+eeK2+sidM7dlO4QRxe+BVPx2CEthPfcG1zDAStxecBNJdm5bHgT\\nmzCVlEUtFbjq7tUc2QjG2q5WOH5wPGojAdgNgU90o8v1jhwP96nAmwPelfm+xefU\\nScLV/dFtR+AqTB+1/TGrx13wshRysyO73wFyXlECgYBLgKkPOUS9M76AUFkuB9r+\\noepf9K/OC1VLSJWfjwzpUGyyoVlfd9jyakTifLNgOXjUMLrfArE32bpe58uLaoZX\\n07tFNQmlj8zp6Tu/c1YQi/mPwx9gwVXfebLAimjJzc0pWhaZQX8Tj54bwcxzxZaG\\nXJvnZ5bng+hX+D/LQY1tJw==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-2nydb@teacherpanelapp.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"100205206366398826145\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-2nydb%40teacherpanelapp.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";
            InputStream stream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentiats = GoogleCredentials.fromStream(stream).createScoped(Lists.newArrayList(firebaseMessagingScope));
            googleCredentiats.refresh();
            return googleCredentiats.getAccessToken().getTokenValue();
        }
        catch (IOException e){
            Log.d("MyApp","token error "+e.getLocalizedMessage());
            e.getLocalizedMessage();
            return null;
        }
    }
}
