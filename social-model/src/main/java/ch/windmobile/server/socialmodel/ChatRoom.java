package ch.windmobile.server.socialmodel;

/**
 * Abstract representation of a char room
 *
 */
public interface ChatRoom {
	
	/**
	 * Unique identifier for a char room accross all zones
	 */
	String getChatRoomId();
	
	/**
	 * Zone Identifier on what the room belong.  
	 */
	String getChatZoneId();
}
