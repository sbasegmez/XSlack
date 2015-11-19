package org.openntf.slack.core;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.openntf.slack.xsnippets.XSnippetsCommandHandler;

public class CommandHandlerFactory {

	public static AbstractCommandHandler findHandler(SlashCommand slashCommand, HttpServletResponse response) {
		String command = slashCommand.getCommand().toLowerCase(Locale.ENGLISH);
		
		if("/xsnippets".equals(command)) {
			return new XSnippetsCommandHandler(slashCommand, response);
		}
		
		return null;
	}
	
}
