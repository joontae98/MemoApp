module.exports = (app, User) => {

    app.post('/adduser', (req, res) => {
        var date = new Date();
        date = date + 1000 * 60 * 60 * 9;
        var user = new User({
            "name": req.body.name,
            "email": req.body.email,
            "password": req.body.password,
            "create_date": date
        });
        user.save((err) => {
            if (err) {
                console.error(err);
                res.json({result: 0});
                return;
            }
            res.json({result: 1});
            console.log("user Data: " + user);
        })
    });

    app.post('/', (req, res) => {
        console.log('미들웨어 호출됨');
        var approve = {'approve_id': 'NO', 'approve_pw': 'NO'};
        var paramEmail = req.body.email;
        var paramPassword = req.body.password;
        console.log('id : ' + paramEmail + '  pw : ' + paramPassword);

        // db에서 데이터를 가져오면 됨
        User.findOne({email: paramEmail}).exec((err, user) => {
            // 등록된 사용자가 없는 경우
            if (!user) {
                console.log('계정이 일치하지 않음.');
                res.writeHead('200', {'Content-Type': 'text/html;charset=utf8'});
                res.write('false_1');
                res.end();
                return;
            }

            // 비밀번호 비교하여 맞지 않는 경우
            if (user.password != paramPassword) {
                console.log('비밀번호 일치하지 않음.');
                res.writeHead('200', {'Content-Type': 'text/html;charset=utf8'});
                res.write('false_2');
                res.end();
                return;
            }
            approve.approve_id = 'OK';
            approve.approve_pw = 'OK';
            res.send(approve);
        })
    });
}