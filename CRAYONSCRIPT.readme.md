# install node
npm install
npm install -g grunt-cli
npm install -g http-server

# running grunt
grunt --gruntfile etc\build\Gruntfile.js --base .

# running node http-server
cd javascript
http-server
# browse to http://localhost:8080/examples/grapheditor/www/index.html

# modifying js files
grunt --gruntfile etc\build\Gruntfile.js --base .
# reload the page
