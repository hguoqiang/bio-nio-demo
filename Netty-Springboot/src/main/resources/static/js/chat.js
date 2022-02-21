$(function () {
    //这里需要注意的是，prompt有两个参数，前面是提示的话，后面是当对话框出来后，在对话框里的默认值
    var username = "";
    while (true) {
        //弹出一个输入框，输入一段文字，可以提交
        username = prompt("请输入您的名字", ""); //将输入的内容赋给变量 name ，
        if (username.trim() === "")//如果返回的有内容
        {
            alert("名称不能输入空")
        } else {
            $("#username").text(username);
            break;
        }
    }

    //创建websocket
    let webSocket = new WebSocket("ws://localhost:8081/chat");

    //打开websocket连接
    webSocket.onopen = function () {
        console.log("连接ws成功");//会触发 通道就绪事件，注意服务端日志
    }


    //接收服务器发来的消息
    webSocket.onmessage = function (evt) {
        showMessage(evt.data);
    }

    webSocket.onclose = function () {
        console.log("连接关闭");
    }
    webSocket.onerror = function () {
        console.log("连接异常");
    }

    function showMessage(message) {
        //张三:你好
        let str = message.split(":");
        $("#msg_list").append(`<li class="active"}>
                                  <div class="main">
                                    <img class="avatar" width="30" height="30" src="/img/user.png">
                                    <div>
                                        <div class="user_name">${str[0]}</div>
                                        <div class="text">${str[1]}</div>
                                    </div>                       
                                   </div>
                              </li>`);
        // 置底
        setBottom();
    }

    $('#my_test').bind({
        focus: function (event) {
            event.stopPropagation()
            $('#my_test').val('');
            $('.arrow_box').hide()
        },
        keydown: function (event) {
            event.stopPropagation()
            if (event.keyCode === 13) {
                if ($('#my_test').val().trim() === '') {
                    this.blur()
                    $('.arrow_box').show()
                    setTimeout(() => {
                        this.focus()
                    }, 1000)
                } else {
                    $('.arrow_box').hide()
                    //发送消息
                    sendMsg();
                    this.blur()
                    setTimeout(() => {
                        this.focus()
                    })
                }
            }
        }
    });
    $('#send').on('click', function (event) {
        event.stopPropagation()
        if ($('#my_test').val().trim() === '') {
            $('.arrow_box').show()
        } else {
            sendMsg();
        }
    })

    function sendMsg() {
        var message = $("#my_test").val();
        $("#msg_list").append(`<li class="active"}>
                                  <div class="main self">
                                      <div class="text">` + message + `</div>
                                  </div>
                              </li>`);
        $("#my_test").val('');

        //发送消息，会触发通道的读就绪事件，看服务端日志
        webSocket.send(username + ":" + message);


        // 置底
        setBottom();
    }

    // 置底
    function setBottom() {
        // 发送消息后滚动到底部
        const container = $('.m-message')
        const scroll = $('#msg_list')
        container.animate({
            scrollTop: scroll[0].scrollHeight - container[0].clientHeight + container.scrollTop() + 100
        });
    }
});