package org.openntf.xsp.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.Session;
import lotus.domino.View;

import org.openntf.xsp.utils.Utils;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.DataObject;


public class Configuration implements Serializable, DataObject {

	private static final long serialVersionUID = 1L;

	private static final String BEAN_NAME = "config";

	public static final Configuration get() {
		return (Configuration)ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), BEAN_NAME);
	}
	
	private boolean loaded=false;
	private Calendar dateLoaded;

	private Map<String, String> fields;
	
	public Configuration() {
		if(!loadConfig()) {
			System.out.println("Error loading Slack Configuration!");
		}
	}

	private boolean loadConfig() {
		
		fields=new HashMap<String, String>();

		Session session = ExtLibUtil.getCurrentSessionAsSigner();
		
		Database db=null;
		View configView=null;
		Document configDoc=null;
		
		try {
			db = session.getCurrentDatabase(); 
			String serverName = db.getServer();
			configView = db.getView("Configuration");
			configDoc = configView.getDocumentByKey(serverName, true);
			
			if(configDoc==null) {
				configDoc=db.createDocument();
				configDoc.replaceItemValue("Form", "Configuration");
				configDoc.replaceItemValue("Server", serverName);
				configDoc.computeWithForm(false, false);
				configDoc.save();
			}
			
			for(Object itemObj: configDoc.getItems()) {
				if(itemObj instanceof Item) {
					Item item=(Item) itemObj;
					if(!item.getName().startsWith("$")) {
						fields.put(item.getName(), item.getText());
					}
					Utils.recycleObjects(item);
				}
			}
			
			setLoaded(true);

			System.out.println("Slack Configuration loaded successfully...");
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			Utils.recycleObjects(configView, configDoc);
		}
	}
		
	public boolean isLoaded() {
		return loaded;
	}

	private void setLoaded(boolean loaded) {
		this.loaded = loaded;
		this.dateLoaded=(loaded)?Calendar.getInstance():null;
	}

	public Calendar getDateLoaded() {
		return dateLoaded;
	}

	public boolean reload() {
		return loadConfig();
	}
	
	public Class<?> getType(Object arg0) {
		return Configuration.class;
	}

	public Object getValue(Object key) {
		if(key==null) return "";

		String result=fields.get(key);
		return (null==result)?"":result;
	}

	public boolean isReadOnly(Object arg0) {
		return true;
	}

	public void setValue(Object arg0, Object arg1) {
		// do nothing
	}

	public String getStringValue(String key) {
		return String.valueOf(getValue(key));
	}

}
