package com.morelang.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Http {
	public static String get(URL url) throws Exception {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();

			return sb.toString();

		} else {
			System.out.println(con.getResponseMessage());
		}

		return null;
	}
}
