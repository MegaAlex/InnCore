package me.megaalex.inncore.command.handlers;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.megaalex.inncore.command.InnCoreCommand;

public interface InnCoreHandler {

    public String getName();
    public void handle(InnCoreCommand cmd, CommandSender sender, String[] args);
    public List<String> getCmds(CommandSender sender);
}
