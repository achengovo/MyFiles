var Main = {
    data() {
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (this.formData.reUserPass !== '') {
                    this.$refs.dataAddForm.validateField('reUserPass');
                }
                callback();
            }
        };
        var validateName = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入用户名'));
            } else if (value.length < 2 || value.length > 20) {
                callback(new Error('用户名长度为2-20位'))
            }else {
                callback();
            }
        };
        var validateVerCode = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入验证码'));
            } else {
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'))
            } else if (value != this.formData.userPass) {
                callback(new Error('两次输入密码不一致'))
            }
        }
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
            formData: {
                userName: '',
                userPass: '',
                reUserPass: '',
                userEmail: '',
                verCode: ''
            },
            rules: {
                userPass: [{ validator: validatePass, trigger: 'blur' }],
                userName: [{ validator: validateName, trigger: 'blur' }],
                reUserPass: [{ validator: validatePass2, trigger: 'blur' }],
                userEmail: [{ validator: validateEmail, trigger: 'blur' }],
                verCode: [{ validator: validateVerCode, trigger: 'blur' }],
            }

        };
    },
    methods: {
        sendMail() {
            var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
            if (this.formData.userEmail == '') {
                this.$message('邮箱不能为空')
            } else if (!reg.test(this.formData.userEmail)) {
                this.$message('邮箱格式错误')
            } else {
                axios({
                    url: '/registerCode?email=' + this.formData.userEmail,
                    method: "get",
                }).then((res) => {
                    console.log(res.data)
                    if (res.data) {
                        this.$notify({
                            title: '发送成功',
                            message: '注册验证码发送成功',
                            type:'success'
                        })
                    }else{
                        this.$notify.error({
                            title:"失败",
                            message:"验证码发送失败"
                        })
                    }
                })
            }

        },
        submitregister() {
            console.log(this.formData)
            var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
            if (this.formData.userName == '') {
                this.$message('用户名不能为空');
            } else if (this.formData.userName < 2 || this.formData.userName > 20) {
                this.$message('用户名长度为2-20位');
            } else if (this.formData.userPass == '') {
                this.$message('密码不能为空');
            } else if (this.formData.userPass != this.formData.reUserPass) {
                this.$message('两次密码不一致')
            } else if (this.formData.userEmail == '') {
                this.$message('邮箱不能为空')
            } else if (!reg.test(this.formData.userEmail)) {
                this.$message('邮箱格式错误')
            } else if (this.formData.verCode == '') {
                this.$message('验证码不能为空')
            } else {
                axios({
                    url: '/register',
                    data: {
                        "userName": this.formData.userName,
                        "userPass": this.formData.userPass,
                        "userEmail": this.formData.userEmail,
                        "varCode": this.formData.verCode
                    },
                    method: 'POST',
                }
                ).then((res) => {
                    console.log(res.data);
                    if (res.data == '注册成功') {
                        this.$alert('注册成功', '成功', {
                            confirmButtonText: '去登录',
                            callback: action => {
                                window.location.href = "/page/login"
                            }
                        });
                    } else {
                        this.$notify.error({
                            title: '失败',
                            message: res.data,
                        });
                    }
                });
            }

        },

    }
}
var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')