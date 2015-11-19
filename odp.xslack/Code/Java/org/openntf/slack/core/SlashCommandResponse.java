package org.openntf.slack.core;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.io.json.util.JsonWriter;

public class SlashCommandResponse implements Serializable {

	private static final long serialVersionUID = 1L;
		
	private boolean respondInChannel = false;
	private SlackMessage formattedText = new SlackMessage();
	private List<SlackAttachment> attachments = new ArrayList<SlackAttachment>();
	
	public SlashCommandResponse() {
	
	}

	public boolean isRespondInChannel() {
		return respondInChannel;
	}

	public void setRespondInChannel(boolean respondInChannel) {
		this.respondInChannel = respondInChannel;
	}

	public SlackMessage getFormattedText() {
		return formattedText;
	}

	public void setFormattedText(SlackMessage formattedText) {
		this.formattedText = formattedText;
	}

	public List<SlackAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<SlackAttachment> attachments) {
		this.attachments = attachments;
	}
	
	public void addAttachment(SlackAttachment attachment) {
		this.attachments.add(attachment);
	}
	
	public String toJson() {
		StringWriter out = new StringWriter();
		JsonWriter writer = new JsonWriter(out, false);
		 
		try {
			writer.startObject();

			if(respondInChannel) {
				writer.outStringProperty("response_type", "in_channel");
			}

			writer.outStringProperty("text", formattedText.toString());

			if(! attachments.isEmpty()) {
				writer.startProperty("attachments");
				writer.startArray();
				
				for(SlackAttachment a: attachments) {
					writer.startArrayItem();
					writer.out(a.toJson());
					writer.endArrayItem();
				}
				
				writer.endArray();
				writer.endProperty();
			}
			
			writer.endObject();
			writer.flush();
			
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		return "{}";
	}

	
	
}
