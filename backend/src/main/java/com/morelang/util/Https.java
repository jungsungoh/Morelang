package com.morelang.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Https {
	public static String get(URL url) throws Exception {
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
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

	public static String get(URL url, Map<String, String> props) throws Exception {
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		for (String key : props.keySet()) {
			con.setRequestProperty(key, props.get(key));
		}

		if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
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
