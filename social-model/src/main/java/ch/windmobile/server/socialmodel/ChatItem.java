package ch.windmobile.server.socialmodel;

import java.util.Date;

/**
 * Interface for chat element
 * @author David Saradini ( epyx.ch )
 *
 */
public interface ChatItem{
	Date getTime();
	String getComment();
	String getUserPseudo();
}
