db.system.js.save({_id: "counter", value: 
	function counter(name) {
    	var ret = db.counters.findAndModify({query:{_id:name}, update:{$inc : {next:1}}, "new":true, upsert:true});
    	// ret == { "_id" : "users", "next" : 1 }
    	return ret.next;
	}
});