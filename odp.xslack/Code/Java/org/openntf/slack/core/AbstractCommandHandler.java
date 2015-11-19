package org.openntf.slack.core;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import lotus.domino.Session;

import com.ibm.xsp.extlib.util.ExtLibUtil;




public abstract class AbstractCommandHandler {

	protected final SlashCommand slashCommand;
	protected final HttpServletResponse response;
	
	public AbstractCommandHandler(SlashCommand slashCommand, HttpServletResponse response) {
		this.slashCommand = slashCommand;
		this.response = response;
	}

	public SlashCommand getSlashCommand() {
		return slashCommand;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	protected void addResponseHeader(String name, String value) {
		response.addHeader(name, value);
	}
	
	protected void setContentType(String type) {
		addResponseHeader("Content-Type", type);
	}
	
	protected void sendBack(String content) {
		PrintWriter writer;

		try {
			writer = response.getWriter();
			writer.append(content);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public abstract void run();

	// This is a dependency!
	protected Session getSession() {
		return ExtLibUtil.getCurrentSession();
	}
	
}
