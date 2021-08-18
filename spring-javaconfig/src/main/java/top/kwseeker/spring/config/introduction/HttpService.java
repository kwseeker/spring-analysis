package top.kwseeker.spring.config.introduction;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HttpService {

    @Autowired
    private OkHttpClient client;

    public void requestBaidu() throws IOException {
        Request request = new Request.Builder()
                .url("https://www.baidu.com/")
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            throw new IOException("Unexpected code " + response);
        System.out.println(response.body().string());
    }
}
