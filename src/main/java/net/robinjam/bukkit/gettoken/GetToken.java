package net.robinjam.bukkit.gettoken;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
			sender.sendMessage(ChatColor.AQUA + "Your token is " + ChatColor.GREEN + getToken(sender.getName(), salt) + ChatColor.AQUA + ".");
			sender.sendMessage(ChatColor.AQUA + "This token must be kept secret, and will expire at midnight UTC.");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Unable to generate token: " + e.getMessage());
		}
		
		return true;
	}
	
	/**
	 * Gets the token for the given user using the current date as the datestamp.
	 * 
	 * @param username The name of the user.
	 * @param salt A salt which is applied to the user's name before it is hashed.
	 * @return The user's token.
	 * @throws NoSuchAlgorithmException If the SHA-512 hash is not available in the current execution environment.
	 */
	public static String getToken(String username, String salt) throws NoSuchAlgorithmException
	{
		return getToken(username, salt, getDatestamp());
	}
	
	/**
	 * Gets the token for the given user.
	 * 
	 * @param username The name of the user.
	 * @param salt A salt which is applied to the user's name before it is hashed.
	 * @param datestamp A datestamp which is appended to the salt.
	 * @return The user's token.
	 * @throws NoSuchAlgorithmException If the SHA-512 hash is not available in the current execution environment.
	 */
	public static String getToken(String username, String salt, String datestamp) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] digest = md.digest((username + salt + datestamp).getBytes());
		return base62_encode(new BigInteger(1, digest)).substring(0, 7);
	}
	
	/**
	 * Base 62 encodes the given integer.
	 * 
	 * @param num The number to encode. Negative numbers are supported.
	 * @return The encoded number.
	 */
	public static String base62_encode(BigInteger num) {
		char[] primitives = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
		BigInteger base = BigInteger.valueOf(primitives.length);
		boolean neg = num.compareTo(BigInteger.ZERO) < 0;
		num = num.abs();
		String result = (num.compareTo(BigInteger.ZERO) == 0) ? "0" : "";
		while (num.compareTo(BigInteger.ZERO) > 0) {
			result = primitives[num.divideAndRemainder(base)[1].intValue()] + result;
			num = num.divide(base);
		}
		return neg ? "-" + result : result;
	}
	
	/**
	 * @return The current UTC date in yyyyMMdd format
	 */
	public static String getDatestamp() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		return df.format(new Date());
	}
	
}
