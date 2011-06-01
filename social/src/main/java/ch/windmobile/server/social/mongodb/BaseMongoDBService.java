package ch.windmobile.server.social.mongodb;

import com.mongodb.DB;

public class BaseMongoDBService {
	protected final DB db;
	
	public BaseMongoDBService(DB db) {
		if  (db != null ) {
			this.db = db;
		} else {
			throw new IllegalArgumentException("database cannot be null");
		}
	}

}
