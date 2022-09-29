var Main = {
    data() {
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                callback();
            }
        };
        var validateName = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入用户名'));
            } else if (value.length < 2 || value.length > 20) {
                callback(new Error('用户名长度为2-20位'))
            } else {
                callback();
            }
        };
        return {
            formData: {
                userName: '',
                userPass: '',
                autoLogin: false,
            },
            rules: {
                userPass: [{validator: validatePass, trigger: 'blur'}],
                userName: [{validator: validateName, trigger: 'blur'}]
            }

        };
    },
    mounted: function () {
        // var autoLoginCode = getCookie("autoLoginCode");
        // if (autoLoginCode != "") {
        //     axios({
        //         method: "POST",
        //         url: "loginByCookie",
        //     }).then((res) => {
        //         console.log(res.data);
        //         if (res.data == 'user') {
        //             window.location.href = "user";
        //         } else if (res.data == 'admin') {
        //             window.location.href = "admin";
        //         }
        //     })
        //     alert("欢迎 " + user + " 再次访问");
        // }
    },
    methods: {
        submitlogin() {
            console.log(this.formData)
            if (this.formData.userName == '') {
                this.$message('用户名不能为空');
            } else if (this.formData.userName < 2 || this.formData.userName > 20) {
                this.$message('用户名长度为2-20位');
            } else if (this.formData.userPass == '') {
                this.$message('密码不能为空');
            } else {
                axios({
                    method: "POST",
                    url: "/login",
                    data: {
                        "userName": this.formData.userName,
                        "userPass": this.formData.userPass,
                    }
                }).then((res) => {
                    console.log(res.data);
                    if(res.data=='fail'){
                        this.$notify.error({
                            title: '登录失败',
                            message: '用户名或密码错误'
                        })
                    }else{
                        window.location.href = "/page/file";
                    }
                })
            }
        },

    }
}
var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}