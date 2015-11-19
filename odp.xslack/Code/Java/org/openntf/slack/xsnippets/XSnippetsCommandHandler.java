package org.openntf.slack.xsnippets;

import javax.servlet.http.HttpServletResponse;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.NotesException;

import org.openntf.slack.core.AbstractCommandHandler;
import org.openntf.slack.core.SlackAttachment;
import org.openntf.slack.core.SlackMessage;
import org.openntf.slack.core.SlashCommand;
import org.openntf.slack.core.SlashCommandResponse;
import org.openntf.xsp.beans.Configuration;
import org.openntf.xsp.utils.Utils;

import com.ibm.commons.util.StringUtil;


public class XSnippetsCommandHandler extends AbstractCommandHandler {

	private static final int MAX_SEARCH_RESULT = 5;

	private static final String SHARE_PREFIX = "share";
	String[] SEARCH_FIELDS= {"Name","Body","Tags","Notes"};
	
	public XSnippetsCommandHandler(SlashCommand slashCommand, HttpServletResponse response) {
		super(slashCommand, response);
	}
	
	public Database getXsDatabase() throws NotesException {
		String dbPath = Configuration.get().getStringValue("XSnippetsDb");
		return getSession().getDatabase("", dbPath, false);
	}
	
	public String getBaseUrl() {
		return Configuration.get().getStringValue("XSnippetsBaseUrl");		
	}
	
	@Override
	public void run() {
		String commandDetails = slashCommand.getText();
		
		SlashCommandResponse response = new SlashCommandResponse();
		SlackMessage text = response.getFormattedText();
		
		String searchString;
		
		if(StringUtil.startsWithIgnoreCase(commandDetails, SHARE_PREFIX+" ")) {
			response.setRespondInChannel(true);
			searchString = commandDetails.substring(SHARE_PREFIX.length()+1);
		} else {
			searchString = commandDetails;
		}
		
		if(StringUtil.isEmpty(searchString)) {
			// Provide usage and do it private.
			response.setRespondInChannel(false);
			text.appendText("Welcome to the Slack Search for ");
			text.appendLink("http://openntf.org/xsnippets", "OpenNTF XSnippets");
			text.appendNewLine(2);
			text.appendText("Usage:\n");
			text.appendCode("/xsnippets [share] search-string");
		} else {
			searchXSnippets(response, searchString);
		}
		
		setContentType("application/json");
		sendBack(response.toJson());	
		
	}

	private void searchXSnippets(SlashCommandResponse response, String searchString) {
		
		SlackAttachment a = new SlackAttachment();
		SlackMessage text = response.getFormattedText();
		
		a.setColor("good");
		
		Database db=null;
		DocumentCollection docs = null;
		
		try {
			db = getXsDatabase();
			
			docs = db.FTSearch(toFtQuery(searchString));
			
			if(docs.getCount() == 0) {
				text.appendText("Ooops... The snippet you are looking for is not here...");
				text.appendItalic("(jedi hand gesture here)");
				response.setRespondInChannel(false);
				return;
			}

			a.setTitle("OpenNTF XSnippets");
			a.setTitleLink("http://openntf.org/xsnippets");
			
			int count=0;
			
			SlackMessage body=a.getText();

			text.appendText("We have found "+docs.getCount()+" results for search terms [_"+searchString.trim()+"_]");
			if(docs.getCount()>MAX_SEARCH_RESULT) {
				text.appendItalic("(Showing "+MAX_SEARCH_RESULT+")");
			}
			
			Document doc = docs.getFirstDocument(); 
			
			while (doc!=null && count<MAX_SEARCH_RESULT) {
				count++;
				
				String link = getBaseUrl() + doc.getItemValueString("id");
				String title = doc.getItemValueString("name");
				String author = doc.getItemValueString("Author");
				
				body.appendText(":small_orange_diamond: ");
				body.appendLink(link, title);
				body.appendText(" -");
				body.appendItalic(author);
				
				if(count<MAX_SEARCH_RESULT) body.appendNewLine();
				
				Document tempDoc = docs.getNextDocument(doc);
				Utils.recycleObjects(doc);
				doc = tempDoc;
			}
			
			response.addAttachment(a);
			
		} catch (NotesException e) {
			e.printStackTrace();
			text.appendItalic("Oops! Error: " + e.text);
			response.setRespondInChannel(false);
		}
		
	}

	private String toFtQuery(String searchString) {
		StringBuilder sb=new StringBuilder();
		
		if(StringUtil.isEmpty(searchString)) {
			return "";
		}
		
		for(String field: SEARCH_FIELDS) {
			if(sb.length()>0) {
				sb.append(" OR ");
			}
			
			sb.append("(["+field+"] CONTAINS "+searchString+")");
		}

		return "([FORM]=Snippet) AND ("+sb.toString()+")";
	}
	
}
