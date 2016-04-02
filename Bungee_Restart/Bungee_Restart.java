package com.joshweaver.frozensoul;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class Bungee_Restart extends Plugin {
	
	Restart restart = new Restart();
	
	public class Reload_Config extends Command{
		public Reload_Config(){
			super("reload_config");
		}

		@Override
		public void execute(CommandSender arg0, String[] arg1) {
			getProxy().getScheduler().cancel(Bungee_Restart.this);
			getProxy().getScheduler().runAsync(Bungee_Restart.this , restart.Restart_Service(Bungee_Restart.this, true)); 
		}
	}
	
	public class Restart_Bungee extends Command{
		public Restart_Bungee(){
			super("restart_bungee");
		}

		@Override
		public void execute(CommandSender arg0, String[] arg1) {
			getProxy().getScheduler().cancel(Bungee_Restart.this);
			getProxy().getScheduler().runAsync(Bungee_Restart.this , restart.Restart_Service(Bungee_Restart.this, false)); 
		}
	}

    @Override
    public void onEnable() {
    	getLogger().info("BungeeRestart by Josh Weaver");

        getProxy().getPluginManager().registerCommand(this, new Reload_Config());
        getProxy().getPluginManager().registerCommand(this, new Restart_Bungee());
        
        getProxy().getScheduler().runAsync(this, restart.Restart_Service(this, true));
        
        /* ***Debugging***
        ScheduledTask test = getProxy().getScheduler().runAsync(this, restart.Restart_Service(this, true)); 
        
        
        int id = test.getId();

        getLogger().info("made the first task");
        
        getLogger().info(String.valueOf(id));
        getProxy().getScheduler().cancel(id);
        
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        getLogger().info("canceled the first task");
        
        test = getProxy().getScheduler().runAsync(this, restart.Restart_Service(this, true)); 
        id = test.getId();
        getLogger().info(String.valueOf(id));
        getLogger().info("made the second task");
        */
    }
}