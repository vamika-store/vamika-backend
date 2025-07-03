package com.vamikastore.vamika.auth.test;

import java.net.Socket;

public class SMTPTest {
    public static void main(String[] args) {
        try (Socket socket = new Socket("smtp.gmail.com", 587)) {
            System.out.println("✅ Successfully connected to smtp.gmail.com:587");
        } catch (Exception e) {
            System.err.println("❌ Connection failed: " + e);
        }
    }
}
