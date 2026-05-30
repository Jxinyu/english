/**
 * 查询单词
 * @param condition
 */
function queryWord(condition) {
    $.ajax({
        url: '/english/queryWord/',
        method: 'get',
        data: {
            condition: condition
        },
        success: function (res) {
            if (res.code === 200) {
                if (res.data === null) {
                    layer.msg('查无此词')
                    return
                }
                s = ''
                let rec = []
                // 拼接html
                for (let i = 0; i < res.data.length; i++) {
                    if (isExist(rec, res.data[i].wEn)) {
                        continue
                    }
                    rec.push(res.data[i].wEn)
                    s +=
                        '<div style="text-align: center">' +
                        '            <div style="background-color: #8e8888">' +
                        '                <table class="layui-table tab" lay-skin="nob" style="cursor: pointer;touch-action: pan-y pinch-zoom;' +
                        '                        display: inline-block;padding: 3px 6px;text-align: right;width: 100%;vertical-align: top;">' +
                        '                    <tbody style="min-height: 250px;">'
                    s += '                   <tr>' +
                        '                        <td style="font-size: 16px;width: 144px;">英文：</td>' +
                        '                        <td style="color: #ff0000;font-size: 16px;text-align: left;">' +
                        '                            <p class="wEn">' + res.data[i].wEn + '</p>' +
                        '                            <p class="wPhonetic" style="font-size: 12px">' + res.data[i].wPhonetic + '</p>' +
                        '                        </td>' +
                        '                    </tr>' +
                        '                    <tr>' +
                        '                        <td style="font-size: 16px;width: 144px;">中文：</td>' +
                        '                        <td style="color: #18a84a;font-size: 14px;text-align: left;">' + res.data[i].wZh + '</td>' +
                        '                    </tr>' +
                        '                    <tr>' +
                        '                        <td style="font-size: 16px;width: 144px;">记忆技巧：</td>' +
                        '                        <td style="color: #0022ff;font-size: 12px;text-align: left;">' + res.data[i].wMemoSkill + '</td>' +
                        '                    </tr>' +
                        '                    <tr>' +
                        '                        <td style="font-size: 16px;width: 144px;">单元：</td>' +
                        '                        <td style="color: #0022ff;font-size: 12px;text-align: left;">' + res.data[i].wUnit + '</td>' +
                        '                    </tr>' +
                        '                    <tr>' +
                        '                        <td style="font-size: 16px;width: 144px;">短语：</td>' +
                        '                        <td style="color: #336d0f;font-size: 14px;text-align: left;">'
                    for (let j = 0; j < res.data[i].phrases.length; j++) {
                        s += '<p>' + res.data[i].phrases[j].p_en + '   ' + res.data[i].phrases[j].p_zn + '</p>'
                    }
                    s += '</td>' +
                        '</tr>' +
                        '                    <tr>' +
                        '                        <td style="font-size: 16px;width: 144px;">读音：</td>' +
                        '                        <td style=""><button class="wAudioq layui-btn layui-btn-sm layui-btn-fluid" name="' +
                        res.data[i].wAudio + '">读音</button></td>' +
                        '                    </tr>' +
                        '</tbody>' +
                        '</table>' +
                        '</div>' +
                        '</div>'
                }
                layer.open({
                    content: s  //提示内容  可以为html
                    , area: ['auto', '440px']   // 大小
                    , skin: 'layui-layer-demo' //样式类名
                    , anim: 1 //动画
                    , shadeClose: true //开启遮罩关闭
                    , btn: ['关闭']
                    , yes: function (index, layero) {
                        layer.close(index); // 关闭弹层
                    }
                });
            } else {
                layer.msg(res.msg)
            }
        }
    })
}

/**
 * 判断str 在不在数组arr中
 * @param arr
 * @param str
 * @returns {boolean}
 */
function isExist(arr, str) {
    for (var k = 0; k < arr.length; k++) {
        if (arr[k].indexOf(str) > -1) {
            return true
        }
    }
    return false
}

/**
 * 通过弹窗输入条件查询单词
 */
function searchWordByPop() {
    layer.prompt({title: '条件', formType: 0}, function (content, index) {
        //content:输入的内容
        layer.close(index)
        queryWord(content)
    })
}


var t = null  // 记录当前的主题，以便确认换肤按钮，使用户选择的
changeTheme(null);
function changeTheme(theme) {
    var content = localStorage.getItem('theme');
    if (theme) {
        content = theme.querySelector("em").innerHTML;
    }
    t = content
    if (content === '白') {
        document.documentElement.style.setProperty('--primary-color', '#007bff');
        document.documentElement.style.setProperty('--secondary-color', '#6c757d');
        document.documentElement.style.setProperty('--background-color', '#f8f9fa');
        // 存储用户选择
        localStorage.setItem('theme', '白');
    } else if (content ==='夜') {
        document.documentElement.style.setProperty('--primary-color', '#17a2b8');
        document.documentElement.style.setProperty('--secondary-color', '#343a40');
        document.documentElement.style.setProperty('--background-color', '#222222');
        // 存储用户选择
        localStorage.setItem('theme', '夜');
    }
}
$(function() {  // 等页面渲染完成后执行
    if (t !== $('.layui-form-switch em').text()){
        $('.layui-form-switch').click()
    }
});

/**
 * 防抖函数 (禁止事件多次触发)
 * @param func  传递过来的是函数的名字
 * @param delay  延迟时间
 * @returns {(function(): void)|*}
 */
// 定义一个变量来保存上一次触发事件的时间戳
let lastClickTime = 0;
function debounce(func, delay) {
    // 获取当前时间戳
    const now = Date.now();
    // 如果当前时间戳和上一次触发事件的时间戳之差小于2秒，则直接返回，不执行事件处理函数
    if (now - lastClickTime < delay) {
        layer.msg('禁止频繁触发', {time: 500})
        return;
    }
    // 更新上一次触发事件的时间戳
    lastClickTime = now;
    // 执行事件处理函数
    func()
}


