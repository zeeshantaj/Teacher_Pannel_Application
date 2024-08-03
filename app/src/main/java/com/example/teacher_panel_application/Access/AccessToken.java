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
                    "  \"private_key_id\": \"244bd41ea958023eadd097445c7dfa27e3b1132e\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDDtLIC9+XDUCVq\\nrtxVPMFXMjsMCC8XFIgF7NbanP4gjT2yqVS3fosyPMG+BRYhszjNRUGCo/L0AZOq\\nqJ+mxKHxHgVBQ8r8Jy7THQ4S6UQoOxI44lgbUHJTXlZQ36ivEl2PGhsGz9ds0C6j\\nC5wdfhhItVkRLU6e3igVF4iv+JttEG3QQsqz4IFQBcM/xXeOeOmyZ5TDuZdEvR9V\\nj1pN5XNWIdET2JSpJ9OZNSzfFJ3AKB5U0GNIbRMD0yDDt7NdH2nwa+DsVghViqyj\\nCxcCbozwWJYN9GeTtM7Tz/4a0FHPxSqIXWtSlVW1FMQUuj/0WJOu79F8KAOYpcwP\\nyYOgDVM7AgMBAAECggEAGljeSkYDebr1fppR8Wu/f7S0fFcxt8K82UdcWYwfLlS1\\nhLMFQ7rWlInXb3SR2TGeqQv02NinobAAqE4RSEtanfJztKii0DWaOTPFLEiZM21Y\\nc64NPIglDBI8PqENvZwUWmqiTtV2ZCDW1n5+ov3R2SAJAKX/2qV/8e78KOyoSBNc\\n3YuOfRNPRqO+0WcYKgyA/g1u2l6CHHZhsrmps9iyGOFzyaBmux+QFRIZqLdbiGLm\\nSNHUMlp9oqj/LFYHEuxYzBdgB8Y0eLyV4OKK1jR2SkRnS5Z5WUjKqY99LMvT/WiZ\\nxmK++BvWhawobNuYIkHdbNbYw9k9an3Awo8GVhF9WQKBgQDkZHnwIAaatGzjXFzm\\n8UHgk50vTfsW30H1ObKWQaC6YqywC/V97Md1K7gU+VCH+wzuFkqRGzIzPGN61KTy\\n5C51q8az4CAWGWFfBk6k1y5gynE2pYgDWMOiCQ8PLE+gQal19LUFNpYq9LVJXswB\\ndyMK36cPyMYYPtrXek4MxqYu9wKBgQDbXL4Wl2WZI5cXY8197JwfeYmUUzSqf4W4\\nqCPIRPPNEhxLY/m1z25KRdlINoOxgFPzlPSSMXI8N3lYE6fKVsc9FVEOx9gpkYWj\\n6x9lPE2Itsjy1XCSUC1TI8R7GF8SQWd8x/xjIAGk3HCYa/EPDcMjHtXtLJQMudPp\\n7SSgDEV43QKBgQDf93PoOM+hJ+tbrfhgdBeA7eCHQfZHQJoCqxjo5eSCwj7LyH5t\\nhSM3UK8MykvhfRv54hwvXbcFrWN0UVCL651vwQpy3Ac7BJqKv7mhRm5xdjQ9PWg6\\nt/HoVO/SlkZjReCzDkvxl8z8xMl98JsnmLfhct71yJIRtpmOEp61Qpw2kwKBgQDD\\nYGgB8RhRfCx/banB5XLsabCLBkzbKrdsT32HSzqmROIkl8f6EeWWi33KXpuPBdBA\\nHER0zUuErJgMRX1lr0LHeev3MTGrXGxeIfWVkk8wXDTAbDBWcMjdyvad+If813oX\\n3ntBwmkUcC5s438RvF04M6ewFctLRlujd07uqd9alQKBgQDOujZZeMK2F3l+1NPw\\nY0r/AcXvjCvobOaUdigf4dQGVl+n4LHBIFXsVFsJTuJHMSgdvxa02u7DYyVFvkKP\\nfnqU+mGaZhS4Y6vmA8VDYjgjB5rEZj4b6iEWWwXE05IwF2rjt+1I2yYeEO+MAPHF\\n0c3MQ3gb/400Wo/3wla9+DvVsw==\\n-----END PRIVATE KEY-----\\n\",\n" +
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
