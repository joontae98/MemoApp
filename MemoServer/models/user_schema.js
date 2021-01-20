var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var userSchema = new Schema(
    {
        name: String,
        email: {type: String, unique: true},
        password: String,
        create_date: {type: Date, default: Date.now}
    }
);

module.exports = mongoose.model('user', userSchema);