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

    app.post('/home', (req, res) => {
        Memo.find({'user_id': req.body.userId}).exec((err, memos) => {
            res.send(memos);
        });
    });

    app.post('/home/remove', (req, res) => {
        Memo.deleteOne({'_id': req.body.memoId}).exec((err) => {
            res.end();
        });
    });

    app.post('/home/update', (req, res) => {
        Memo.findById(req.body.userId).exec((err, result )=>{
            result.title = req.body.title;
            result.content = req.body.content;
            result.save((err) =>{

            })
            res.end();
        });
    });
    app.post('/addId',(req, res) => {
        Memo.findById(req.body.id).exec((err, res1) => {
            // res1.user_id += req.body.user;// 문자열 그대로 합쳐지는 오류 발생

            res1.user_id = [res1.user_id, req.body.user];
            res1.save(err1 => {
            });
            res.send(res1);
        })
    })

}