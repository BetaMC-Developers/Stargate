package net.TheDgtl.Stargate;

import org.bukkit.plugin.Plugin;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Method.MethodAccount;
import com.nijikokun.register.payment.Methods;

/**
 * iConomyHandler.java
 * @author Steven "Drakia" Scott
 */

public class iConomyHandler {
	public static String pName = "Stargate";
	public static boolean useiConomy = false;
	private static Methods methods = new Methods();
	public static Method method;
	
	public static int useCost = 0;
	public static int createCost = 0;
	public static int destroyCost = 0;
	public static boolean toOwner = false;
	public static boolean chargeFreeDestination = true;
	public static boolean freeGatesGreen = false;
	
	public static double getBalance(String player) {
		if (useiConomy && method != null) {
			MethodAccount acc = method.getAccount(player);
			if (acc == null) {
				Stargate.debug("ich::getBalance", "Error fetching Register account for " + player);
				return 0;
			}
			return acc.balance();
		}
		return 0;
	}
	
	public static boolean chargePlayer(String player, String target, double amount) {
		if (useiConomy && method != null) {
			// No point going from a player to themself
			if (player.equals(target)) return true;
			
			MethodAccount acc = method.getAccount(player);
			if (acc == null) {
				Stargate.debug("ich::chargePlayer", "Error fetching Register account for " + player);
				return false;
			}
			
			if (!acc.hasEnough(amount)) return false;
			acc.subtract(amount);
			
			if (target != null) {
				MethodAccount tAcc = method.getAccount(target);
				if (tAcc != null) {
					tAcc.add(amount);
				}
			}
			return true;
		}
		return true;
	}
	
	public static boolean useiConomy() {
		return useiConomy && method != null;
	}
	
	public static String format(int amt) {
		return method.format(amt);
	}
	
	public static boolean setupiConomy(Plugin p) {
		if (!useiConomy) return false;
		if (methods.setMethod(p)) {
			method = methods.getMethod();
			return true;
		}
		return false;
	}
	
	public static boolean checkLost(Plugin p) {
		return methods.hasMethod() && methods.checkDisabled(p);
	}
}
