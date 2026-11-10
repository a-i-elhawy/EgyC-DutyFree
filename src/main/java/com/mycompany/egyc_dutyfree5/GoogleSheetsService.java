package com.mycompany.egyc_dutyfree5;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.InputStream;
import java.util.Collections;

public class GoogleSheetsService {
    private static final String APPLICATION_NAME = "EgyC Duty Free System";
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    
    // اسم الملف الخاص بك
    private static final String CREDENTIALS_FILE_PATH = "/egyc-dutyfree-sync-2f55819bb42e.json";

    public static Sheets getSheetsService() throws Exception {
        // 1. تحميل الملف من الموارد
        InputStream in = GoogleSheetsService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new Exception("لم يتم العثور على ملف الجيسون في المسار: " + CREDENTIALS_FILE_PATH);
        }

        // 2. استخدام GoogleCredentials بدلاً من Flow (أفضل لحسابات الخدمة)
        GoogleCredentials credentials = GoogleCredentials.fromStream(in)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

        // 3. بناء الخدمة مباشرة
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), 
                JSON_FACTORY, 
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}