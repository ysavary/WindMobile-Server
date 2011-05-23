db.sessions.drop()

db.users.drop();
db.chatroom_TEST.drop()
// pwd == abc
db.users.insert({"pseudo":"Yann","email":"yann@epyx.ch","fullName":"Yann Savary","sha1":"Mftkglbk9d2KY3GOLWq0shuAte4="});
//pwd == 123
db.users.insert({"pseudo":"David","email":"david@epyx.ch","fullName":"David Saradini","sha1":"T+ST7rQ2j53lLHDuFCVxrfUbYUY="});
//pwd == 456
db.users.insert({"pseudo":"Cédric","email":"cedric@epyx.ch","fullName":"Cédric Tallichet","sha1":"fk4O+ED9KBt8c809UQgYDtmRR3U="});
//pwd == 123
db.users.insert({"pseudo":"Vincent","email":"vincent@epyx.ch","fullName":"Vincent Fournié","sha1":"WmqwczS01lxRldW4dznGz7jpIlA="});
//pwd == 123
db.users.insert({"pseudo":"Stefan","email":"stefan@epyx.ch","fullName":"Stefan Paychère","sha1":"qy+saGQxhVjIYmNttcDDVkTUoQ8="});
//pwd == 123
db.users.insert({"pseudo":"Nicolas","email":"nicolas@epyx.ch","fullName":"Nicolas Fulpius","sha1":"MPXNTo83MF5Ockex84yheWnxtl8="});
