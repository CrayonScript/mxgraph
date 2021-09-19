# Graph Editor
Run GraphEditor main to start the servlet container


# install node
npm install  
npm install -g grunt-cli  
npm install -g http-server

# running grunt
grunt --gruntfile etc\build\Gruntfile.js --base .

# running node http-server
cd javascript  
http-server  
browse to  
http://localhost:8080/examples/grapheditor/www/index.html

# modifying js files
grunt --gruntfile etc\build\Gruntfile.js --base .  
reload the page


# luaparse.js
The luaparse.js is built independent of the ace editor library  
The lib path is $ACE_HOME/lib/ace/mode/lua/luaparse.js  
Any update to the ace library would require a corresponding luaparse.js update 

# Building ace
Read the CRAYONSCRIPT.readme.md in the ace github repo

https://github.com/CrayonScript/ace

on steps to build the ace project
