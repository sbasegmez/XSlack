package org.openntf.slack.core;

import java.io.Serializable;

import com.ibm.commons.util.StringUtil;

public class SlackMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private StringBuilder textBody = new StringBuilder();

	public SlackMessage() {
	}
	
	// Handle Escaping
	public void appendText(String text) {
		appendText(text, true);
	}

	public void appendText(String text, boolean unescape) {
		textBody.append(unescape ? unescapeText(text) : text);
	}
	
	public void appendNewLine() {
		appendNewLine(1);
	}
	
	public void appendNewLine(int n) {
		textBody.append(StringUtil.repeat("\n", n));
	}

	public void appendLink(String link) {
		appendLink(link, null);
	}
	
	public void appendLink(String link, String title) {
		
		textBody.append("<"+link);
		
		if(StringUtil.isNotEmpty(title)) {
			textBody.append("|"+unescapeText(title));
		}
		
		textBody.append(">");
	}
	
	public void appendBold(String text) {
		appendText(" *"+text+"*");
	}

	public void appendItalic(String text) {
		appendText(" _"+text+"_");
	}

	public void appendStriked(String text) {
		appendText(" ~"+text+"~");
	}

	public void appendCode(String text) {
		appendText(" `"+text+"`", false);
	}

	public void appendPreformatted(String text) {
		appendText("\n```"+text+"```", false);
	}
	
	public void appendQuote(String text) {
		appendText("\n> "+unescapeText(text)+"\n", false);
	}
	
	public static String unescapeText(String text) {
		text.replace("&", "&amp;");
		text.replace("<", "&lt;");
		text.replace(">", "&gt;");
		
		return text;
	}

	@Override
	public String toString() {
		return textBody.toString();
	}
	
}
