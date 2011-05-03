db.sessions.drop()

db.users.drop();
db.chatroom_TEST.drop()
// pwd == abc
db.users.insert({"pseudo":"ysy","email":"yann@epyx.ch","fullName":"Yann Savary","sha1":"Mftkglbk9d2KY3GOLWq0shuAte4="});
//pwd == 123
db.users.insert({"pseudo":"dsi","email":"david@epyx.ch","fullName":"David Saradini","sha1":"T+ST7rQ2j53lLHDuFCVxrfUbYUY="});
//pwd == 456
db.users.insert({"pseudo":"ctt","email":"cedric@epyx.ch","fullName":"Cédric Tallichet","sha1":"fk4O+ED9KBt8c809UQgYDtmRR3U="});
