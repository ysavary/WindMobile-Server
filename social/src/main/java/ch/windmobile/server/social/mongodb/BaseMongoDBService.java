package ch.windmobile.server.social.mongodb;

import com.mongodb.DB;

public class BaseMongoDBService {
	protected final DB database;
	
	public BaseMongoDBService(DB database) {
		if  (database != null ) {
			this.database = database;
		} else {
			throw new IllegalArgumentException("database cannot be null");
		}
	}

}
