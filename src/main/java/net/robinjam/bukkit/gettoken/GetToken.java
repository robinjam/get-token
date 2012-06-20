package net.robinjam.bukkit.gettoken;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main plugin class.
 * 
 * @author robinjam
 */
public class GetToken extends JavaPlugin {
	
	private String salt;
	
	@Override
	public void onEnable() {
		// Load config file, copying it from the .jar if it does not exist
		getConfig().options().copyDefaults(true);
		saveConfig();
		salt = getConfig().getString("salt");
		
		// Register commands
		getCommand("get-token").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			sender.sendMessage(ChatColor.AQUA + "Your token is " + ChatColor.GREEN + getToken(sender.getName(), salt) + ChatColor.AQUA + ". Please keep this token secret.");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Unable to generate token: " + e.getMessage());
		}
		
		return true;
	}
	
	/**
	 * Gets the token for the given user.
	 * 
	 * @param username The name of the user.
	 * @param salt A salt which is applied to the user's name before it is hashed.
	 * @return The user's token.
	 * @throws NoSuchAlgorithmException If the SHA-512 hash is not available in the current execution environment.
	 */
	public static String getToken(String username, String salt) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] digest = md.digest((username + salt).getBytes());
		return String.format("%0128x", new BigInteger(1, digest)).substring(0, 5);
	}
	
}
