var http = require('http');
var express = require('express')
    , bodyParser = require('body-parser')
    , mongoose = require('mongoose')
    , Memo = require('./models/memo_schema')
    , User = require('./models/user_schema')

var app = express();

mongoose.connect('mongodb://localhost/memo', {useNewUrlParser: true, useUnifiedTopology: true});

app.set('port', 3000);
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

var routeUser = require('./routes/user')(app, User);
var routeMemo = require('./routes/memo')(app, Memo);

var server = http.createServer(app).listen(app.get('port'), () => {
    console.log("Express server has started on port " + app.get('port'))
});
