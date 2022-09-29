var Main = {
    data() {
        return{
            active:0,
            userName:'',
            varCode:'',
            newPass:'',
            reNewPass:'',

        }
    },
    mounted: function () {

    },
    methods: {
        next() {
            if(this.active==0){
                if(this.userName==''){
                    this.$notify.error({
                        title: '错误',
                        message: '请输入用户名'
                    })
                }else{
                    axios({url:'forgetPassSendEmail',
                    data:{
                        userName:this.userName
                    },
                    method:'POST'}).then((res)=>{
                        if(res.data=='success'){
                            this.active=1
                        }else{
                            this.$notify.error({
                                title: '错误',
                                message: res.data
                            })
                        }
                    })
                }
            }else if(this.active==1){
                if(this.varCode==''){
                    this.$notify.error({
                        title: '错误',
                        message: '请输入验证码'
                    })
                }else{
                    axios({url:'forgetPassCode?varcode='+this.varCode,method: "GET"}).then((res)=>{
                            if(res.data=='success'){
                                this.active=2
                            }else{
                                this.$notify.error({
                                    title: '错误',
                                    message: res.data
                                })
                            }
                    })
                }
            }else if(this.active==2){
                if(this.newPass==''){
                    this.$notify.error({
                        title: '错误',
                        message: "密码不能为空"
                    })
                }else if(this.newPass.length<4||this.newPass.length>20){
                    this.$notify.error({
                        title: '错误',
                        message: "密码长度应为4-20位"
                    })
                }else if(this.newPass!=this.reNewPass){
                    this.$notify.error({
                        title: '错误',
                        message: "两次密码不一致"
                    })
                }else{
                    axios({url:'forgetPassNewPass',method:"POST",data:{
                            varCode: this.varCode,
                            userPass:this.newPass
                        }}).then((res)=>{
                            if(res.data=='success'){
                                this.active=3
                            }else{
                                this.$notify.error({
                                    title: '错误',
                                    message: res.data
                                })
                            }
                    })
                }
            }
            // if (this.active++ > 2) this.active = 0;
        }
    }
}
var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')