package com.morelang.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.morelang.dto.Caption;
import com.morelang.dto.Channel;
import com.morelang.dto.Playlist;
import com.morelang.dto.PlaylistItem;
import com.morelang.util.Https;

@Service
public class ChannelServiceImpl implements ChannelService {

	@Override
	public Channel getInfo(String id) throws Exception {
		Channel channel = new Channel();
		URL url = new URL(
				"https://www.googleapis.com/youtube/v3/channels?part=snippet,contentDetails&key=AIzaSyDQ9s4NbYiufJwA6Q2BIT0lhguBoG49pWI&id="
						+ id);
		JSONObject obj = new JSONObject(Https.get(url)).getJSONArray("items").getJSONObject(0);

		channel.setId(obj.getString("id"));
		channel.setUploads(obj.getJSONObject("contentDetails").getJSONObject("relatedPlaylists").getString("uploads"));
		obj = obj.getJSONObject("snippet");
		channel.setTitle(obj.getString("title"));
		channel.setDescription(obj.getString("description"));
		channel.setPublishedAt(obj.getString("publishedAt"));
		channel.setImgUrl(obj.getJSONObject("thumbnails").getJSONObject("high").getString("url"));
		if (obj.keySet().contains("defaultLanguage"))
			channel.setDefaultLanguage(obj.getString("defaultLanguage"));
		if (obj.keySet().contains("country"))
			channel.setCountry(obj.getString("country"));
		channel.setChannelUrl("https://www.youtube.com/channel/" + id);

		return channel;
	}

	@Override
	public Playlist getItems(String id, String token) throws Exception {
		Playlist playlist = new Playlist();
		URL url = new URL(
				"https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&key=AIzaSyDQ9s4NbYiufJwA6Q2BIT0lhguBoG49pWI&playlistId="
						+ id + "&pageToken=" + (token == null ? "" : token));
		JSONObject obj = new JSONObject(Https.get(url));

		if (obj.keySet().contains("nextPageToken"))
			playlist.setToken(obj.getString("nextPageToken"));

		List<PlaylistItem> items = new ArrayList<>();
		JSONArray arr = obj.getJSONArray("items");

		for (int i = 0; i < arr.length(); i++) {
			JSONObject j = arr.getJSONObject(i).getJSONObject("snippet");
			PlaylistItem item = new PlaylistItem();
			item.setId(j.getJSONObject("resourceId").getString("videoId"));
			item.setTitle(j.getString("title"));
			item.setDescription(j.getString("description"));
			item.setPublishedAt(j.getString("publishedAt"));
			item.setImgUrl("https://i.ytimg.com/vi/" + item.getId() + "/mqdefault.jpg");
			item.setVideoUrl("https://www.youtube.com/watch?v=" + item.getId());

			List<Caption> captions = new ArrayList<>();
			try {
				Document d = Jsoup.connect("http://video.google.com/timedtext?type=list&v=" + item.getId()).userAgent("Mozilla/5.0").get();
				Elements tracks = d.getElementsByTag("track");
				for (Element track : tracks) {
					Caption c = new Caption();
					c.setId(track.attr("id"));
					c.setLang_code(track.attr("lang_code"));
					c.setLang_original(track.attr("lang_original"));
					c.setLang_translated(track.attr("lang_translated"));
					captions.add(c);
				}
			} catch (Exception e) {
			}
			item.setCaptions(captions);
			items.add(item);
		}
		playlist.setItems(items);

		return playlist;
	}

}
