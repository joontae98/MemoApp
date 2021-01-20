module.exports = (app, Memo, User) => {

    // 1. 인스턴스 생성
    // 2. 인스턴스에 값 put
    // 3. 인스턴스 저장

    //* req.body.${val} = val 는 전달하는 사람의 key 값
    //ex) android.put("name","park")  =>  req.body.name

    app.post('/memo', (req, res) => {
        var date = new Date();
        date = date + 1000 * 60 * 60 * 9;
        var memo = new Memo({"title": req.body.title, "content": req.body.content, "create_date": date});
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
}