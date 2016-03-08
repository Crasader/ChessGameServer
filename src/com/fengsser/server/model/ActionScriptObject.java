package com.fengsser.server.model;
import flex.messaging.io.amf.ASObject;

public class ActionScriptObject {
	private final ASObject ao ;
	
	public ActionScriptObject(){
		ao = new ASObject();
	}
	
	public ActionScriptObject(ASObject ao){
		this.ao = ao;
	}
	
	public void putInt(String key,int value){
		this.put(key, value);
	}
	
	public void putBool(String key,boolean value){
		this.put(key,value);
	}
	
	public void putString(String key,String value){
		this.put(key, value);
	}
	
	public Integer getInt(String key){
		return (Integer)ao.get(key);
	}
	
	public String getString(String key){
		return (String)ao.get(key);
	}
	
	public ActionScriptObject getObject(String key){
		return new ActionScriptObject((ASObject)ao.get(key));
	}
	
	@SuppressWarnings("unchecked")
	private void put(String key,Object value){
		ao.put(key, value);
	}
	
	public ASObject getObject(){
		return ao;
	}
	
	public void putObj(String key,ASObject value){
		this.put(key, value);
	}
	
	public Boolean getBoolean(String key){
		return (Boolean)ao.get(key);
	}
}
