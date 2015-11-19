package org.openntf.slack.core;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.util.JsonWriter;

public class SlackAttachment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String fallback;
	private String color;
	private String pretext;
	
	private String authorName; // author_name
	private String authorLink; // author_link
    private String authorIcon; // author_icon

    private String title;
    private String titleLink; // title_link
    
    private String imageUrl; // image_url
    private String thumbUrl; // thumb_url

    private SlackMessage text;
    private boolean fieldsShort=true;
    private Map<String, String> fields; // field values by title;

    private boolean mdText =true;
    private boolean mdPretext=true;
    
    public SlackAttachment() {
    	this.text = new SlackMessage();
    	this.fields = new LinkedHashMap<String, String>();
	}
    
    /**
     * (From Slack API Docs)
     * A plain-text summary of the attachment. This text will be used in clients that don't show formatted text 
     * (eg. IRC, mobile notifications) and should not contain any markup. 
     * 
     * @param fallback
     */
	public void setFallback(String fallback) {
		this.fallback = fallback;
	}

	/**
     * (From Slack API Docs)
	 * An optional value that can either be one of good, warning, danger, or any hex color code (eg. #439FE0).
	 * This value is used to color the border along the left side of the message attachment.
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
     * (From Slack API Docs)
	 * This is optional text that appears above the message attachment block.
	 *  
	 * @param pretext
	 */
	public void setPretext(String pretext) {
		this.pretext = pretext;
	}
	
	/**
	 * (From Slack API Docs)
	 * Small text used to display the author's name.
	 *  	
	 * @param authorName
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	/**
	 * (From Slack API Docs)
	 * A valid URL that will hyperlink the author_name text mentioned above. Will only work if author_name is present.
	 * 
	 * @param authorLink
	 */
	public void setAuthorLink(String authorLink) {
		this.authorLink = authorLink;
	}
	
	/**
	 * (From Slack API Docs)
	 * A valid URL that displays a small 16x16px image to the left of the author_name text. 
	 * Will only work if author_name is present.
	 *  
	 * @param authorIcon
	 */
	public void setAuthorIcon(String authorIcon) {
		this.authorIcon = authorIcon;
	}
	
	/**
	 * (From Slack API Docs)
	 * The title is displayed as larger, bold text near the top of a message attachment.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * (From Slack API Docs)
	 * By passing a valid URL in the title_link parameter (optional), the title text will be hyperlinked.
	 * 
	 * @param titleLink
	 */
	public void setTitleLink(String titleLink) {
		this.titleLink = titleLink;
	}
	
	
	/**
	 * (From Slack API Docs)
	 * A valid URL to an image file that will be displayed inside a message attachment. 
	 * We currently support the following formats: GIF, JPEG, PNG, and BMP. Large images will be resized to a 
	 * maximum width of 400px or a maximum height of 500px, while still maintaining the original aspect ratio.
	 * 
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	/**
	 * (From Slack API Docs)
	 * A valid URL to an image file that will be displayed as a thumbnail on the right side of a message attachment. 
	 * We currently support the following formats: GIF, JPEG, PNG, and BMP. The thumbnail's longest dimension will be 
	 * scaled down to 75px while maintaining the aspect ratio of the image. The filesize of the image must also be less than 500 KB.
	 * 
	 * @param thumbUrl
	 */
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	/**
	 * (From Slack API Docs)
	 * An optional flag indicating whether the value is short enough to be displayed side-by-side with other values.
	 * 
	 * @param fieldsShort
	 */
	public void setFieldsShort(boolean fieldsShort) {
		this.fieldsShort = fieldsShort;
	}
	
	public void setMdText(boolean mdText) {
		this.mdText = mdText;
	}
	
	public void setMdPretext(boolean mdPretext) {
		this.mdPretext = mdPretext;
	}
	
	public SlackMessage getText() {
		return text;
	}
	
	public void addField(String title, String value) {
		fields.put(title, value);
	}
		
	private static void appendToJson(JsonWriter writer, String name, String value) throws IOException {
		if(StringUtil.isNotEmpty(value)) {
			writer.outStringProperty(name, value);
		}
	}
	
	public String toJson() {
		StringWriter out = new StringWriter();
		JsonWriter writer = new JsonWriter(out, false);
		 
		try {
			writer.startObject();

			appendToJson(writer, "fallback", fallback);
			appendToJson(writer, "color", color);
			appendToJson(writer, "pretext", pretext);
			appendToJson(writer, "author_name", authorName);
			appendToJson(writer, "author_link", authorLink);
			appendToJson(writer, "author_icon", authorIcon);
			appendToJson(writer, "title", title);
			appendToJson(writer, "title_link", titleLink);
			appendToJson(writer, "text", text.toString());
			appendToJson(writer, "image_url", imageUrl);
			appendToJson(writer, "thumb_url", thumbUrl);

			if(mdText || mdPretext) {
				List<String> md = new ArrayList<String>();
				if(mdText) md.add("text");
				if(mdPretext) md.add("pretext");
				
				writer.outArrayProperty("mrkdwn_in", md);
			}
			
			if(! fields.isEmpty()) {
				writer.startProperty("fields");
				writer.startArray();
				
				for(Map.Entry<String, String> entry:fields.entrySet()) {
					writer.startArrayItem();
					writer.startObject();
					appendToJson(writer, "title", entry.getKey());
					appendToJson(writer, "value", entry.getValue());
					writer.outBooleanProperty("short", fieldsShort);
					writer.endObject();
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
		} catch (JsonException e) {
			e.printStackTrace();
		}
		 
		return "{}";
	}

    
}

