package com.morelang.util;
import java.io.FileInputStream;
import java.util.List;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.language.v1beta2.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1beta2.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1beta2.Document;
import com.google.cloud.language.v1beta2.EncodingType;
import com.google.cloud.language.v1beta2.LanguageServiceClient;
import com.google.cloud.language.v1beta2.LanguageServiceSettings;
import com.google.cloud.language.v1beta2.Token;

public class NLAnalyze {
	private static NLAnalyze instance = new NLAnalyze();
    private static LanguageServiceClient languageServiceClient;
    private static CredentialsProvider credentialsProvider;
    public static NLAnalyze getInstance() {
        try {
        	credentialsProvider= FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(new FileInputStream("GoogleMorelang.json")));
            LanguageServiceSettings.Builder languageServiceSettingsBuilder= LanguageServiceSettings.newBuilder();
            LanguageServiceSettings languageServiceSettings = languageServiceSettingsBuilder.setCredentialsProvider(credentialsProvider).build();
            languageServiceClient = LanguageServiceClient.create(languageServiceSettings);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return instance;
    }
    public List<Token> analyzeSyntax(String text) {
        try {
            Document document = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();

            AnalyzeSyntaxRequest request = AnalyzeSyntaxRequest.newBuilder()
                    .setDocument(document)
                    .setEncodingType(EncodingType.UTF16)
                    .build();

            AnalyzeSyntaxResponse response = languageServiceClient.analyzeSyntax(request);

            return response.getTokensList();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
