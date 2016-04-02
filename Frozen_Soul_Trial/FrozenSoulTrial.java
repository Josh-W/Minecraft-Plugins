package com.joshweaver.frozensoul;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class FrozenSoulTrial extends JavaPlugin {

	@Override
	public void onEnable(){
		getLogger().info("FrozenSoulTrial: Waking up.");
		
		saveDefaultConfig();
		saveConfig();
		getLogger().info(getConfig().getString("config.message"));
		
		//Print the MOTD at the rate specified in the config
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this,  new Runnable() {
			@Override public void run(){
				getServer().broadcastMessage(getConfig().getString("config.message"));
			}
		} , 0L, getConfig().getLong("config.message_rate"));
		
		Bukkit.getServer().getScheduler().cancelTasks(this);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getLogger().info("task canceled");
		scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(this,  new Runnable() {
			@Override public void run(){
				getServer().broadcastMessage(getConfig().getString("config.message"));
			}
		} , 0L, getConfig().getLong("config.message_rate"));
		
		getLogger().info("FrozenSoulTrial: Lets start the day");
	}
	
	@Override
	public void onDisable(){
		getLogger().info("FrozenSoulTrial: Going to to bed.");
		
		Bukkit.getServer().getScheduler().cancelTasks(this);
		
		getLogger().info("FrozenSoulTrial: Going to sleep.");
	}
}
