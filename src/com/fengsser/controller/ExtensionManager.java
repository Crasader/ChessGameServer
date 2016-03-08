package com.fengsser.controller;

import java.util.HashMap;
import java.util.Map;

import com.fengsser.extension.ChatExtension;
import com.fengsser.extension.Extension;
import com.fengsser.extension.GameExtension;
import com.fengsser.extension.SystemExtension;

public class ExtensionManager {

	private final Map<String,Extension> extensions;

	private static ExtensionManager instance = null;
	
	private ExtensionManager(){
		if(instance != null){
			throw new Error("ExtensionManager can't new");
		}
		System.out.println("！！！ExtensionManager Ready！！！");
		extensions = new HashMap<String,Extension>();
		registerExtension();
	}
	
	public static ExtensionManager getInstance(){
		if(instance == null){
			instance = new ExtensionManager();
		}
		return instance;
	}

	public Extension getExtension(String name){
		return extensions.get(name);
	}
	
	private void registerExtension(){
		this.extensions.put("Sys", new SystemExtension());
		this.extensions.put("Chat", new ChatExtension());
		this.extensions.put("Game", new GameExtension());
	}
}
