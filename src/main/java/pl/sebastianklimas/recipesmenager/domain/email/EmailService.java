package pl.sebastianklimas.recipesmenager.domain.email;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    @Value(value = "${mail-trap.mail-from}")
    private String emailFrom;

    public void sendEmail(String emailTo, String subject, String text) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"from\":{\"email\":\"" + emailFrom + "\",\"name\":\"Mailtrap Test\"},\"to\":[{\"email\":\"" + emailTo + "\"}],\"subject\":\"" + subject + "\",\"text\":\"" + text + "\"}");
        Request request = new Request.Builder()
                .url("https://send.api.mailtrap.io/api/send")
                .method("POST", body)
                .addHeader("Authorization", "Bearer TOKEN")
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
