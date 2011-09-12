db.users.drop();

// pwd == 123
db.users.insert({"pseudo":"Yann","email":"yann.savary@epyx.ch","fullName":"Yann Savary","sha1":"RZvCXeFdiYFed1b+H3yztLq47Tg=","role":"admin"});
// pwd == 123
db.users.insert({"pseudo":"David","email":"david.saradini@epyx.ch","fullName":"David Saradini","sha1":"8ew51sCZPfHKBw4EEg60kbKInIg="});
// pwd == 123
db.users.insert({"pseudo":"Cédric","email":"cedric.tallichet@epyx.ch","fullName":"Cédric Tallichet","sha1":"i/Vp34ljLBBoLqYsQ6SIW5hX4cw="});
// pwd == 123
db.users.insert({"pseudo":"Vincent","email":"vincent.fournie@epyx.ch","fullName":"Vincent Fournié","sha1":"9WEyqIj+E1ko0c/rTM7H5OaSxlY="});
// pwd == 123
db.users.insert({"pseudo":"Stefan","email":"stefan.paychere@epyx.ch","fullName":"Stefan Paychère","sha1":"TUVhIzBXAn5XRQb5cZFArQwnbVk="});

db.chatrooms.insert({"_id":"jdc:1001", "refuseAnonymous":true});
