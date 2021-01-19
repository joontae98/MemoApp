module.exports = (app, Memo) => {

    // 1. 인스턴스 생성
    // 2. 인스턴스에 값 put
    // 3. 인스턴스 저장

    app.post('/memo', (req, res) => {
        var date = new Date();
        date = date + 1000*60*60*9;
        var memo = new Memo({"title": req.body.title, "content": req.body.content, "create_date": date});
        memo.save((err) => {
            if (err) {
                console.error(err);
                res.json({result: 0});
                return;
            }
            res.json({result: 1});
            console.log(memo);
        })
    });

    app.use((req, res) => {
        console.log('미들웨어 호출됨');
        var approve = {'approve_id': 'NO', 'approve_pw': 'NO'};

        var paramId = req.body.id;
        var paramPassword = req.body.password;
        console.log('id : ' + paramId + '  pw : ' + paramPassword);

        // db에서 데이터를 가져오면 됨
        if (paramId == 'test01') approve.approve_id = 'OK';
        if (paramPassword == '1088') approve.approve_pw = 'OK';

        res.send(approve);
    });
}