package com.github.kotooriiii;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class BungeeReceiveListener implements Listener {
    @EventHandler
    public void onReceiveAuthentication(PluginMessageEvent event) {
        if (!event.getTag().equals("LostShard->BungeeCord:Authenticate".toLowerCase()))
            return;
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String uuidString = in.readUTF(); // Read the UUID
            UUID playerUUID = UUID.fromString(uuidString);

            String isTutorialCompleteString = in.readUTF();
            boolean isTutorialComplete = Boolean.valueOf(isTutorialCompleteString);

            ProxiedPlayer player = BungeeLostShardPlugin.getInstance().getProxy().getPlayer(playerUUID);
            if (player == null || !player.isConnected())
                return;

            if (isTutorialComplete)
                BungeeLostShardPlugin.getInstance().connect(player, ServerType.MAIN);
            else
                BungeeLostShardPlugin.getInstance().connect(player, ServerType.TUTORIAL);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onReceiveComplete(PluginMessageEvent event) {
        if (!event.getTag().equals("TutorialLostShard->BungeeCord:Complete".toLowerCase()))
            return;
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String uuidString = in.readUTF(); // Read the UUID
            UUID playerUUID = UUID.fromString(uuidString);

            String isAuthenticString = in.readUTF();

            ProxiedPlayer player = BungeeLostShardPlugin.getInstance().getProxy().getPlayer(playerUUID);
            if (player == null || !player.isConnected())
                return;

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(uuidString);
            out.writeUTF(isAuthenticString);
            ServerType.MAIN.getServerInfo().sendData("BungeeCord->LostShard:Complete".toLowerCase(), out.toByteArray());
            BungeeLostShardPlugin.getInstance().connect(player, ServerType.MAIN);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
