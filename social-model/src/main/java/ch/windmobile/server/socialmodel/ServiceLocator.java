/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ch.windmobile.server.socialmodel;

/**
 * Base service locator to handle connection with the underling datastore
 *
 */
public interface ServiceLocator {
	
	/**
	 * Connect to the service datastore
	 * After the connection a new service locator may be returned, client must discard the one used to connect.
	 * @param url URL to the server, comma separated list of url. null to use localhost
	 * @Return a service locator to use
	 */
	ServiceLocator connect(String url) throws ServiceLocatorException;
	
	/**
	 * Disconnect from the service datastore
	 */
	void disconnect();
	
	/**
	 * Return a state-less epyx service
	 * May throw an exception if service cannot be found
	 */
	<S> S getService(Class<S> serviceType) throws ServiceLocatorException;
	
	
	static class ServiceLocatorException extends Exception {
		private static final long serialVersionUID = 1L;
		
		public ServiceLocatorException(String message) {
			super( message );
		}
		
		public ServiceLocatorException(String message,Exception ex) {
			super( message,ex );
		}
		
		public ServiceLocatorException( Exception ex) {
			super( ex );
		}
	}
}
