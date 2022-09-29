var Main = {
    data() {
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (this.pass.reNewPass !== '') {
                    this.$refs.dataAddForm.validateField('reNewPass');
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'))
            } else if (value != this.pass.newPass) {
                callback(new Error('两次输入密码不一致'))
            }
        }
        var validateName = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入用户名'));
            } else if (value.length < 2 || value.length > 20) {
                callback(new Error('用户名长度为2-20位'))
            } else {
                callback();
            }
        };
        var validateEmail = (rule, value, callback) => {
            var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
            if (value === '') {
                callback(new Error('请输入邮箱'));
            } else if (!reg.test(value)) {
                callback(new Error('邮箱格式错误'))
            } else {
                callback();
            }
        };
        return {
            nowEmail: '',
            isone: true,
            //折叠菜单
            isCollapse: false,
            //用户信息
            user:{
                "userAvatar":"default.png"
            },
            pass: {
                userPass: "",
                newPass: "",
                reNewPass: "",
            },
            rules: {
                userName: [{validator: validateName, trigger: 'blur'}],
                userEmail: [{validator: validateEmail, trigger: 'blur'}],

            },
            rules2: {
                newPass: [{validator: validatePass, trigger: 'blur'}],
                reNewPass: [{validator: validatePass2, trigger: 'blur'}],
            }
        }
    },
    created: function(){
        axios({
            url:'/getUserInfo',
            method:'GET',
        }).then((res)=>{
            console.log(res.data)
            this.user=res.data
            this.nowEmail=res.data.userEmail
        })
    },
    mounted: function () {
        // axios({url: "/getUserInfo"}).then((res) => {
        //     this.user = res.data
        //     this.nowEmail = this.user.userEmail
        // })
    },
    methods: {
        changePassByPass() {
            if (this.pass.userPass == '' || this.pass.newPass == '') {
                this.$notify.error({
                    title: '错误',
                    message: '密码不能为空'
                })
            } else if (this.pass.newPass != this.pass.reNewPass) {
                this.$notify.error({
                    title: '错误',
                    message: '两次密码不同'
                })
            } else {
                axios({
                    url: '/changePassByPass',
                    params: {
                        'userPass': this.pass.userPass,
                        'newPass': this.pass.newPass
                    },
                    contentType: "application/text",
                    method: "POST"
                }).then((res) => {
                    if (res.data) {
                        this.$notify({
                            title: '修改成功',
                            message: '密码修改成功',
                            type: 'success'
                        });
                    }else{
                        this.$notify.error({
                            title:'失败',
                            message:'密码修改失败'
                        })
                    }
                })
            }

        }
        ,
        sendMail() {
            var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
            if (this.user.userEmail == '') {
                this.$message('邮箱不能为空')
            } else if (!reg.test(this.user.userEmail)) {
                this.$message('邮箱格式错误')
            } else {
                console.log("发送验证邮件")
                axios({
                    url: '/changeEmailCode?email=' + this.user.userEmail,
                    method: "get",
                }).then((res) => {
                    if(res.data){
                        this.$notify({
                            title: '成功',
                            message: '邮件发送成功',
                            type: 'success'
                        });
                    }else{
                        this.$notify.error({
                            title: '失败',
                            message: '邮件发送失败',
                        });
                    }
                })
            }

        },
        changePass(e) {
            console.log(e)
        },
        //上传头像成功
        uploadSuccess(e) {
            // console.log(e)
            // this.user.userAvatar = e
            if(e){
                this.$notify({
                    title:'成功',
                    message:'头像修改成功',
                    type:'success'
                })
            }
            this.getUserInfo()
        },
        //获取用户信息
        getUserInfo() {
            axios({url: "/getUserInfo"}).then((res) => {
                this.user = res.data
                this.nowEmail = this.user.userEmail
            })
        },
        //修改用户信息
        changeUser() {
            var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
            if (this.user.userName == '') {
                this.$notify.error({
                    title: '错误',
                    message: '用户名不能为空'
                })
            } else if (this.user.userName.length < 2 || this.user.userName > 20) {
                this.$notify.error({
                    title: '错误',
                    message: '用户名长度为2-20位'
                })
            } else if (this.user.userEmail == '') {
                this.$notify.error({
                    title: '错误',
                    message: '邮箱不能为空'
                })
            } else if (!reg.test(this.user.userEmail)) {
                this.$notify.error({
                    title: '错误',
                    message: '邮箱格式错误'
                })
            } else if (this.user.varCode == '') {
                this.$notify.error({
                    title: '错误',
                    message: '请输入验证码'
                })
            } else {
                axios({
                    url: "/changeUser", method: "POST",
                    data: {
                        userName: this.user.userName,
                        userEmail: this.user.userEmail,
                        userSex: this.user.userSex,
                        userAvatar: this.user.userAvatar,
                        varCode: this.user.varCode
                    }
                }).then((res) => {
                    console.log(res.data)
                    if(res.data=="success"){
                        this.$notify({
                            title: '成功',
                            message: '用户信息修改成功',
                            type: 'success'
                        })
                        this.getUserInfo()
                    }else{
                        this.$notify.error({
                            title: '错误',
                            message: res.data
                        })
                    }
                })
            }
        }
        ,
        handleChange(e) {
            console.log(e)
        }
        ,
        //左侧菜单打开
        handleOpen(key, keyPath) {
            console.log(key, keyPath)
        }
        ,
        //左侧菜单关闭
        handleClose(key, keyPath) {
            console.log(key, keyPath)
        }
        ,
        //左侧菜单状态改变
        changeisCollapse(bool) {
            this.isCollapse = bool
        }
        ,
        //点击左侧菜单
        clickMenu(e) {
            console.log(e)
            if (e == 2) {
                window.location.href = "file"
            } else if (e == 3) {
                window.location.href = "user"
            }
        },
        //退出登录
        loginOut() {
            axios({url: '/loginOut', method: 'POST'}).then((res) => {
                if (res.data) {
                    window.location.href = '/page/login'
                }
            })
        },
        //头像下拉菜单
        avatarCommand(e) {
            console.log(e)
            if (e == 'loginOut') {
                console.log("退出登录")
                this.loginOut()
            } else if (e == 'user') {
                console.log("个人中心")
                window.location.href = "user"
            }
        }
        ,

    },
}
var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}