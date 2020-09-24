package com.github.kotooriiii;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeLostShardPlugin extends Plugin {

    private static BungeeLostShardPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        this.getProxy().getPluginManager().registerListener(this, new BungeeReceiveListener());
        this.getProxy().getPluginManager().registerListener(this, new BungeeConnectListener());

        this.getProxy().registerChannel("BungeeCord->LostShard:Authenticate".toLowerCase());
        this.getProxy().registerChannel("BungeeCord->LostShard:Complete".toLowerCase());
    }

    public static BungeeLostShardPlugin getInstance()
    {
        return instance;
    }

    public void connect(ProxiedPlayer player, ServerType type)
    {
        if(player.getServer() != null && player.getServer().getInfo().equals(type.getServerInfo())) {
            player.sendMessage(new ComponentBuilder("You are already connected to this server.").color(ChatColor.RED).create());
            return;
        }

        player.connect(type.getServerInfo());
    }
}