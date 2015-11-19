package org.openntf.slack.core;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public class SlashCommand implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final String token;
	private final String teamId;
	private final String teamDomain;
	private final String channelId;
	private final String channelName;
	private final String userId;
	private final String userName;
	private final String command;
	private final String text;
	private final String responseUrl;
	
	
	public SlashCommand(Map<?,?> requestMap) {
		this.token = readString(requestMap, "token");
		this.teamId = readString(requestMap, "team_id");
		this.teamDomain = readString(requestMap, "team_domain");
		this.channelId = readString(requestMap, "channel_id");
		this.channelName = readString(requestMap, "channel_name");
		this.userId = readString(requestMap, "user_id");
		this.userName = readString(requestMap, "user_name");
		this.command = readString(requestMap, "command");
		this.text = readString(requestMap, "text");
		this.responseUrl = readString(requestMap, "response_url");
	}

	public String getToken() {
		return token;
	}

	public String getTeamId() {
		return teamId;
	}

	public String getTeamDomain() {
		return teamDomain;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getCommand() {
		return command;
	}

	public String getText() {
		return text;
	}
	
	public String getResponseUrl() {
		return responseUrl;
	}

	private static String readString(Map<?,?> map, String label) {
		Object value = map.get(label);
		
		if(value!=null) {
			return String.valueOf(value);
		}
		
		for(Object key:map.keySet()) {
			if(key.toString().toLowerCase(Locale.ENGLISH).equals(label.toLowerCase(Locale.ENGLISH))) {
				return String.valueOf(map.get(key));
			}
		}
		
		return "";
	}
	
}
