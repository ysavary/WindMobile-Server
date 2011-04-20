package ch.windmobile.server.socialmodel;

/**
 * Wind-mobile user
 *
 */
public interface User {
	/**
	 * Return pseudo or chat
	 */
	String getPseudo();
	
	/**
	 * Return the full name of the user
	 */
	String getFullName();
	
	/**
	 * Return the email address of the user
	 */
	String getEmail();
}
