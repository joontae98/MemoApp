var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var memoSchema = new Schema(
    {
        user_id: [{type: String, required: true}],
        title: String,
        content: String,
        create_date: {type: Date, default: Date.now}
    }
);

module.exports = mongoose.model('memo', memoSchema);