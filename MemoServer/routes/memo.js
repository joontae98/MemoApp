module.exports = (app, Memo) => {

    // 1. 인스턴스 생성
    // 2. 인스턴스에 값 put
    // 3. 인스턴스 저장

    //* req.body.${val} = val 는 전달하는 사람의 key 값
    //ex) android.put("name","park")  =>  req.body.name

    app.post('/home/create', (req, res) => {
        var date = new Date();
        date = date + 1000 * 60 * 60 * 9;
        var memo = new Memo({
            "user_id": req.body.userId,
            "title": req.body.title,
            "content": req.body.content,
            "create_date": date
        });
        memo.save((err) => {
            if (err) {
                console.error(err);
                res.json({result: 0});
                return;
            }
            res.json({result: 1});
            console.log("memo data: " + memo);
        })
    });

    //get으로 할려면 간편 url을 사용하여 값을 전달받아 사용하면됨
    // 하지만 post로 하는 이유는 url 이 노출 되기때문에 post로 보안성을 높임
    app.post('/home', (req, res) => {
        Memo.find({'user_id': req.body.userId}).exec((err, memos) => {
            console.log(memos);
            res.send(memos);
        });
    })

    app.post('/home/remove', (req, res) => {
        Memo.remove({'_id': req.body.memoId}).exec((err) => {
            res.end();
        });
    })

}