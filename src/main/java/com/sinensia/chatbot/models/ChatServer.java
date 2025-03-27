package com.sinensia.chatbot.models;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import com.sinensia.chatbot.services.OpenaiService;

public class ChatServer {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ChatServer.class, args);
        OpenaiService aiService = context.getBean(OpenaiService.class);

        System.out.println("Servidor de chat");
        try (ServerSocket serverSocket = new ServerSocket(4999)) {

            System.out.println("Servidor en espera de conexiones...");

            // Esperar a que un cliente se conecte
            Socket clientSocket = serverSocket.accept();
            System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());

            // Flujo de entrada del cliente
            InputStream input = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];

            while (true) {
                // Leer el mensaje del cliente
                int bytesRead = input.read(buffer);
                if (bytesRead == -1) {
                    break; // El cliente cerró la conexión
                }
                String message = new String(buffer, 0, bytesRead);
                System.out.println("Cliente: " + message);

                // Enviar una respuesta al cliente (puedes personalizar esto)
                String aiResponse = aiService.enviarMensaje(message);
                clientSocket.getOutputStream().write(("OpenAI: " + aiResponse).getBytes());

                // String response = "Servidor: Gracias por tu mensaje.";
                // clientSocket.getOutputStream().write(response.getBytes());
            }

            // Cerrar la conexión
            clientSocket.close();
            System.out.println("Cliente desconectado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
