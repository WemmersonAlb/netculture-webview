package com.netculture.netculture.service;

import java.util.Base64;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ApiImgur {
    private static final String CLIENT_ID = "564003f581d2f29";
    private static final String CLIENT_SECRET = "09710e4029575f79c52741034e82d4ae67084d8a";

    public String uploadImage(MultipartFile file) throws Exception {
        // Converte o MultipartFile para Base64
        String imageBase64 = Base64.getEncoder().encodeToString(file.getBytes());

        // Cria um cliente HTTP
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Configura a requisição HTTP POST para a API do Imgur
            HttpPost uploadRequest = new HttpPost("https://api.imgur.com/3/image");
            uploadRequest.setHeader("Authorization", "Client-ID " + CLIENT_ID);

            // Corpo da requisição em JSON
            StringEntity entity = new StringEntity(
                "{\"image\":\"" + imageBase64 + "\"}",
                ContentType.APPLICATION_JSON
            );
            uploadRequest.setEntity(entity);

            // Envia a requisição
            String responseBody = httpClient.execute(uploadRequest, response -> 
                EntityUtils.toString(response.getEntity())
            );

            // Extraindo a URL da resposta JSON
            return new org.json.JSONObject(responseBody)
                    .getJSONObject("data")
                    .getString("link");
        }
    }
}
