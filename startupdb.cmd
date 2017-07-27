@echo off

set MONGO_HOME=D:\Develop\40_databases\mongodb-win32-x86_64-2008plus-ssl-3.2.14

cls

echo mongo --port 27017 -u "admin" -p "admin" --authenticationDatabase "admin"
cd %MONGO_HOME%\bin
mongod --auth --port 27017 --dbpath %MONGO_HOME%\db

goto end

use admin
db.createUser(
  {
    user: "admin",
    pwd: "admin",
    roles: [ { role: "root", db: "admin" } ]
  }
);
exit;

use poc
db.createUser(
	{
	  user: "admin",
	  pwd: "admin",
	  roles: [ { role: "readWrite", db:"poc" } ]
	}
)

:end
