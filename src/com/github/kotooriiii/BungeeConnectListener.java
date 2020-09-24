package com.github.kotooriiii;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class BungeeConnectListener implements Listener {

    private HashSet<UUID> playersConnecting;

    public BungeeConnectListener()
    {
        playersConnecting = new HashSet<>();
    }

    @EventHandler
    public void onConnect(ServerConnectEvent event)
    {

        if((event.getTarget().equals(ServerType.MAIN) || event.getTarget().equals(ServerType.TUTORIAL))
                && event.getReason() != ServerConnectEvent.Reason.PLUGIN)
        {
            if(!playersConnecting.contains(event.getPlayer().getUniqueId()))
                authenticate(event.getPlayer().getUniqueId());

            event.getPlayer().sendMessage(new ComponentBuilder("Verifying the tutorial has been complete...").color(ChatColor.RED).create());
            event.setCancelled(true);
        }
    }

    private void authenticate(UUID uuid)
    {
        playersConnecting.add(uuid);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(uuid.toString());
        ServerType.MAIN.getServerInfo().sendData("BungeeCord->LostShard:Authenticate".toLowerCase(), out.toByteArray());
    }

    @EventHandler
    public void onConnect(ServerConnectedEvent event)
    {
        playersConnecting.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event)
    {
        playersConnecting.remove(event.getPlayer().getUniqueId());
    }
}
