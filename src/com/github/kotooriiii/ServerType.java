package com.github.kotooriiii;

import com.google.gson.internal.$Gson$Preconditions;
import net.md_5.bungee.api.config.ServerInfo;

public enum ServerType {
    //todo
    MAIN("lostshard"),
    TUTORIAL("tutorial");

    private String serverName;

    private ServerType(String serverName)
    {
        this.serverName = serverName;
    }

    public ServerInfo getServerInfo() {
        return BungeeLostShardPlugin.getInstance().getProxy().getServerInfo(serverName);
    }
}
