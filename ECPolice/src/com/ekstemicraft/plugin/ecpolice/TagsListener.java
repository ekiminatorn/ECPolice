package com.ekstemicraft.plugin.ecpolice;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class TagsListener implements Listener{
	//This makes possible to get config in this class!
	ECPolice plugin;
	
	
	public TagsListener(ECPolice instance) {
		//This makes possible to get config in this class!
		plugin = instance;		
	}
	
	@EventHandler
	public void onNameTag(PlayerReceiveNameTagEvent event) {
		ArrayList<String> list = (ArrayList<String>) plugin.getConfig().getStringList("users");
		if(list.contains(event.getNamedPlayer().getName())){
              //If true, sets users tag to blue.
			event.setTag(ChatColor.BLUE + event.getNamedPlayer().getName());	
		}	
	}
}
