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
	 */
	<S> S getService(Class<S> serviceType);
	
	
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
