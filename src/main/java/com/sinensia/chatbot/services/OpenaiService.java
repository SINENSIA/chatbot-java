package com.sinensia.chatbot.services;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.JSONArray;

@Service
public class OpenaiService {

    private final String apiKey;
    private final String apiUrl;

    public OpenaiService(@Value("${openai.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.apiUrl = "https://api.openai.com/v1/chat/completions";
    }

    public String enviarMensaje(String mensajeUsuario) {
        try {
            URL url = URI.create(apiUrl).toURL();
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);

            String body = """
                    {
                      "model": "gpt-4o",
                      "messages": [{"role": "user", "content": "%s"}],
                      "temperature": 0.7
                    }
                    """.formatted(mensajeUsuario.replace("\"", "\\\""));

            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes());
            }

            InputStream responseStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
            StringBuilder responseBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            // Usar JSON para extraer el contenido
            JSONObject json = new JSONObject(responseBuilder.toString());
            JSONArray choices = json.getJSONArray("choices");
            if (!choices.isEmpty()) {
                return choices.getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                        .trim();
            }

            return "[Respuesta vacía]";
        } catch (Exception e) {
            e.printStackTrace();
            return "[Error al conectar con OpenAI]";
        }
    }
}