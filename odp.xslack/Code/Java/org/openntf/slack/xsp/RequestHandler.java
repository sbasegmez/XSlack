package org.openntf.slack.xsp;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.openntf.slack.core.AbstractCommandHandler;
import org.openntf.slack.core.CommandHandlerFactory;
import org.openntf.slack.core.SlashCommand;

/**
 * This is the entry point from XAgents. We'll take whatever needs to be done and route 
 * into appropriate classes.
 * 
 * This is an isolated area, because other classes needs to be independent of basic XSP.
 * Maybe we'll move them into servlets in the future.
 * 
 * @author sbasegmez
 *
 */

public class RequestHandler {

	/**
	 * This is to handle SlashCommands coming from Slack.
	 * 
	 * It will construct the command content and will route into a CommandHandler.
	 * 
	 */
	public static void handleCommand() {
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		SlashCommand slashCommand = new SlashCommand(fc.getExternalContext().getRequestParameterMap());
		HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

		AbstractCommandHandler handler = CommandHandlerFactory.findHandler(slashCommand, response);
	
		if(handler == null) {
			// maybe we should return an error.
			return;
		}
		
		handler.run();
		
	}
	
	
}
