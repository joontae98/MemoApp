var http = require('http');
var express = require('express')
    ,bodyParser = require('body-parser')
    ,mongoose = require('mongoose')
    ,Memo = require('./models/memo')

var app = express();

mongoose.connect('mongodb://localhost/memo',{useNewUrlParser: true, useUnifiedTopology: true});

app.set('port', 3000);
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

var route = require('./routes/index')(app, Memo);


var server = http.createServer(app).listen(app.get('port'), () => {
    console.log("Express server has started on port " + app.get('port'))
});
