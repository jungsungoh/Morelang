package com.morelang.config;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.bis5.mattermost.client4.hook.IncomingWebhookClient;
import net.bis5.mattermost.model.IncomingWebhookRequest;
import net.bis5.mattermost.model.SlackAttachment;

public class Webhook {

	private final static String _WEBHOOK_URL = "https://meeting.ssafy.com/hooks/xhgq8imty7gmjy5obybx57wese";

	public static void send(HttpServletRequest request, String location, Exception e) {
		IncomingWebhookClient client = new IncomingWebhookClient(_WEBHOOK_URL);
		IncomingWebhookRequest payload = new IncomingWebhookRequest();
		payload.setText("@jyp5826                      👆\n");
		payload.setUsername("Override Username");
		SlackAttachment sa = new SlackAttachment();
		sa.setColor("#FF0000");
		sa.setText(request.getRequestURL() + "?" + request.getQueryString() + "\n" + location + "\n" + e.toString());
		sa.setAuthorName("Backend Exception!");
		sa.setAuthorIcon("https://t1.daumcdn.net/cfile/tistory/99DA853359891D8A09");
		List<SlackAttachment> li = new ArrayList<>();
		li.add(sa);
		payload.setAttachments(li);
		Map<String, String> props = new HashMap<>();
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		props.put("card", (sw.toString().length() < 6000 ? sw.toString() : sw.toString().substring(0, 6000) + "..."));
		payload.setProps(props);
		client.postByIncomingWebhook(payload);
	}
}
