package com.ekstemicraft.plugin.ecpolice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.TagAPI;

public final class ECPolice extends JavaPlugin{

	@Override
	public void onEnable() {
		//Enable message
		getLogger().info("ECPolice has been enabled!");
		
		//Enabling/registering Tags Listener
		PluginManager pm = Bukkit.getServer().getPluginManager();
				pm.registerEvents(new TagsListener(this), this);
				
		//Making the config if it doesnt exist
		File file = new File(getDataFolder() + File.separator + "config.yml");
		if (!file.exists()){
			this.getConfig().options().copyDefaults(true);
			this.getLogger().info("Generating config file to store cop data");
			List<String> exampleplayers = new ArrayList<String>();
			exampleplayers.add("Username1-these-can-be-removed");
			exampleplayers.add("Username2-these-can-be-removed");
			this.getConfig().addDefault("users",exampleplayers);
			this.saveConfig();					
		}		
	}
	
	@Override
	public void onDisable() {
		//Disable message
		getLogger().info("ECPolice has been disabled!");
		//Reloading config for safety
		reloadConfig();
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("policeman")) { //Player or console executes command
//reloading config before any execution to be on the safe side
	reloadConfig();
	
			ArrayList<String> list = (ArrayList<String>) this.getConfig().getStringList("users");
			String player = sender.getName();
			//Is alredy a cop?
			if(list.contains(player)){
				//Is a cop already code execution
				//Removing player from permishun group
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "perm player removegroup " + player + " police");
				
				//Remove player from config.yml and set the new values (Without the player ofcourse)
				list.remove(player);
				this.getConfig().set("users", list);
				//Send message to player to thank him for serving ECPD (Totally obvious)
				sender.sendMessage(ChatColor.BLUE + "[ECPD] Thank you for serving ECPD!");
				//Sending message to whole server about player leaving the police force (Totally obvious?)
				Bukkit.broadcastMessage(ChatColor.BLUE + "[ECPD] " + player + " left the police force!");
				
				//TagAPI remove tag colouring code here!
				Player tagremoveplayer = (Player) sender;
				TagAPI.refreshPlayer(tagremoveplayer);

				//Saving config and reloading
				saveConfig();
				reloadConfig();
				//Returning value back to Bukkit!
				return true;
			}
			//Is not cop
			else{
				//Execution to add player to police force!
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "perm player addgroup " + player + " police");
			
			//Spawning in a stick renamed "Police Baton"
			ItemStack stick = new ItemStack(Material.STICK, 1);
			ItemMeta im = stick.getItemMeta();
			im.setDisplayName("Police Baton");
			stick.setItemMeta(im);
			Player invplayer = (Player) sender;
			PlayerInventory pi = invplayer.getInventory();
			pi.addItem(stick);
			//Refreshing players tag
			Player tagnewplayer = (Player) sender;
			TagAPI.refreshPlayer(tagnewplayer);
			
			//Sending player a message about joining the ECPD (Totally obvious?)
			sender.sendMessage(ChatColor.BLUE + "[ECPD] Welcome " + ChatColor.WHITE + player + ChatColor.BLUE + " , you are now one of our officers!");
			//Sending to whole server message about player joining the ECPD (Obvious?)
			Bukkit.broadcastMessage(ChatColor.BLUE + "[ECPD] " + player + " joined the police force!");
			
			//Sets user into cop config.yml for future notice
		     list.add(sender.getName());
		     //Taking a copy of the current cop data (config.yml) and overwriting the old data with new
		    this.getConfig().set("users", list);
                    
			//Saving and reloading config!
			saveConfig();
			reloadConfig();
			return true;
			}
		}
return true;		
   }	
}	