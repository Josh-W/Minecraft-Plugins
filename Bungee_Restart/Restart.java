package com.joshweaver.frozensoul;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Restart extends Plugin{
	
	public Runnable Restart_Service(Plugin plugin, boolean automatic) {
		Runnable restart_service = new Runnable(){

			String time;
			String message;
			ArrayList<Integer> intervals = new ArrayList<Integer>();
			
			//Starts the countdown based on if it is an automatic, or a manual restart
			//If the config couldn't be loaded, it runs with the defaults (5 Minutes)
			@Override
			public void run() {
				try {
					Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(loadResource(plugin, "config.yml"));
					message = config.getString("config.message");
					if (automatic)
						time = config.getString("config.automatic_restart");
					else
						time = config.getString("config.manual_restart");
					
					String[] times = time.split(",");
					if (automatic)
						intervals.add((int) config.getLong("config.restart_interval"));
					
					for(String item : times){
						intervals.add(Integer.parseInt(item));
					}
					
					Restart_Bungee(intervals, message, plugin);
				} catch (IOException e) {
					plugin.getLogger().info("Could not load config, using defaults.");
					message = "FrozenSoul will restart in (time_value) (time)";
					intervals.add(300);
					intervals.add(60);
					intervals.add(20);
					intervals.add(10);
					intervals.add(5);
					intervals.add(4);
					intervals.add(3);
					intervals.add(2);
					intervals.add(1);
					Restart_Bungee(intervals, message, plugin);
					e.printStackTrace();
				}
			}
			
		};
		return restart_service;
	}
	
	//Loads the config - Acts like bukkit's config loading
	public static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
	
	
	//Prints out the given message in the config with the time remaining before restart
	//This is printed at the given intervals in the configs.
	//At the end of the countdown it shuts down bungee
	//The given times are in seconds. 1 day in seconds: 86400
	public void Restart_Bungee(ArrayList<Integer> intervals, String message, Plugin plugin){
		int start = intervals.get(0);
		intervals.remove(0);
		plugin.getLogger().info(String.format("Restarting in %d minutes.", start / 60));
		for (int i = start; i > 0; i--){
			if (i == intervals.get(0)){
				plugin.getLogger().info(String.format("Restarting in %d seconds.", i));
				int time = intervals.remove(0);
				boolean is_minute = false;
				if (time > 60){
					time = time / 60;
					is_minute = true;
				}
				if (time > 1 && is_minute)
					plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), "alert "+message
							.replaceAll("(time_value)", String.valueOf(time))
							.replaceAll("(time)", "Minutes"));
				else if (time == 1 && is_minute)
					plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), "alert "+message
							.replaceAll("(time_value)", String.valueOf(time))
							.replaceAll("(time)", "Minute"));
				else if (time == 1 && !is_minute)
					plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), "alert "+message
							.replaceAll("(time_value)", String.valueOf(i))
							.replaceAll("(time)", "Second"));
				else if (time > 60 && !is_minute)
					plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), "alert "+message
							.replaceAll("(time_value)", String.valueOf(i))
							.replaceAll("(time)", "Seconds"));
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), "end");
	}
}
