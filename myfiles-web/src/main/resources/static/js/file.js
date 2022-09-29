var Main = {
    data() {
        return {
            //要移动的文件列表
            moveList: [],
            //当前目录结构
            dirList: [{
                userFileId: '',
                userFileName: '我的文件'
            }],
            //目录树容器是否显示
            centerDialogVisible: true,
            newDirName: '',
            //当前目录
            nowDirId: '',
            //折叠菜单
            isCollapse: false,
            //搜索框文本
            searchtxt: '',
            //选中的
            multipleSelection: [],
            //上方功能按钮是否显示
            // showButton: false,
            //抽屉
            drawer: false,
            direction: 'rtl',
            //上传列表
            uploadList: [],
            //表格数据
            tableData: [
            ],
            playerVisible: false,
            previewpic: "",
            userInfo:{
                "userAvatar":"default.png"
            }
        }
    },
    created: function(){
        axios({
            url:'/getUserInfo',
            method:'GET',
        }).then((res)=>{
            console.log(res.data)
            this.userInfo=res.data
        })
    },
    mounted: function () {
        console.log(this.nowDirId)
        axios({
                url: '/getFileList?dir=',
                method: 'GET',
            }
        ).then((res) => {
            console.log(res.data);
            this.tableData = res.data;
        });

    },
    methods: {
        //新建文件夹
        newDir() {
            this.$prompt('请输文件夹名称', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                inputValue: '新建文件夹',
            }).then(({value}) => {
                if (value === '') {
                    this.$message.error('文件夹名称不能为空');
                } else {
                    axios({
                            url: '/newDir',
                            data: {
                                "dir": this.nowDirId,
                                "userFileName": value
                            },
                            method: 'POST',
                        }
                    ).then((res) => {
                        console.log(res.data);
                        if (res.data) {
                        } else {
                            this.$notify.error({
                                title:"失败",
                                message:"新建文件夹失败"
                            })
                        }
                        this.getFilesByDir();
                    });
                }
            }).catch((err) => {
                console.log(err)
            });
        },
        //根据当前目录获取文件列表
        getFilesByDir() {
            axios({
                    url: '/getFileList?dir=' + this.nowDirId,
                    method: 'GET',
                }
            ).then((res) => {
                console.log(res.data);
                this.tableData = res.data;
                return true;
            });
        },
        //点击某个目录跳转
        toDir(e) {
            this.nowDirId = e.userFileId
            for (var i = this.dirList.length; i > 0; i--) {
                if (this.nowDirId != this.dirList[i - 1].userFileId) {
                    this.dirList = this.dirList.slice(0, this.dirList.length - 1);
                } else {
                    break;
                }
            }
            this.getFilesByDir()
        },
        //左侧菜单打开
        handleOpen(key, keyPath) {
            console.log(key, keyPath)
        },
        //左侧菜单关闭
        handleClose(key, keyPath) {
            console.log(key, keyPath)
        },
        //左侧菜单状态改变
        changeisCollapse(bool) {
            this.isCollapse = bool
        },
        //点击左侧菜单
        clickMenu(e) {
            console.log(e)
            if (e == 2) {
                window.location.href = "/page/file"
            } else if (e == 3) {
                window.location.href = "/page/user"
            }
        },
        //文件预览
        doPriveFile(e) {
            console.log(e)
            var url ="/userFile/"+this.userInfo.userId+"/"+ e.fileLocation + ''
            console.log(url)
            filename = e.userFileName
            //目录
            if (e.fileType == 'dir') {
                for (var i = 0; i < this.moveList.length; i++){
                    if(e.userFileId==this.moveList[i].userFileId){
                        this.$notify.error({
                            title:"错误",
                            message:"目标文件夹是源文件夹的子文件夹"
                        })
                        return
                    }
                }
                this.nowDirId = e.userFileId
                if (this.nowDirId != this.dirList[this.dirList.length - 1].userFileId) {
                    this.dirList.push(e)
                }
                this.getFilesByDir();
            } else if (e.fileType == 'pic') {
                //图片
                this.$alert('<embed src="' + url + '" style="max-height: 80vh;max-width:100%;"></embed>', filename, {
                    dangerouslyUseHTMLString: true,
                    center: true,
                    showClose: true,
                    showCancelButton: false,
                    showConfirmButton: false,
                    customClass: "my-el-message-box"
                }).catch((err) => {
                    console.log(err)
                });
            } else if (e.fileType == 'text') {
                //文本
                axios({url: url}).then((res) => {
                    console.log(res.data)
                    var text = "" + res.data + ""
                    this.$alert('<textarea wrap="off" class="textarea" disabled="disabled" style="height:80vh;width:100%;max-width:100%;resize:none">' + text + '</textarea style="height:80vh;width:100%;max-width:100%;">', filename, {
                        dangerouslyUseHTMLString: true,
                        center: true,
                        showClose: true,
                        showCancelButton: false,
                        showConfirmButton: false,
                        customClass: "my-el-message-box"
                    }).catch((err) => {
                        console.log(err)
                    });
                })
            } else if (e.fileType == 'music') {
                //音频
                console.log(filename)
                this.$alert('<video style="max-height: 80vh;width:100%;max-width:100%;height:35px;" src="' + url + '" controls="controls">' +
                    '您的浏览器不支持 video 标签。' +
                    '</video>', filename, {
                    dangerouslyUseHTMLString: true,
                    center: true,
                    showClose: true,
                    showCancelButton: false,
                    showConfirmButton: false,
                    beforeClose: (action, instance, done) => {
                        var myVideo = document.getElementsByTagName('video')[0];   //获取视频video
                        if (!myVideo.paused) {
                            myVideo.pause();
                        }
                        done();
                    }
                }).catch((err) => {
                    console.log(err)
                });
            } else if (e.fileType == 'video') {
                //视频
                console.log(filename)
                this.$alert('<video style="width: 100%;height: 100%;" src="' + url + '" controls="controls">' +
                    '您的浏览器不支持 video 标签。' +
                    '</video>', filename, {
                    dangerouslyUseHTMLString: true,
                    center: true,
                    showClose: true,
                    showCancelButton: false,
                    showConfirmButton: false,
                    beforeClose: (action, instance, done) => {
                        var myVideo = document.getElementsByTagName('video')[0];   //获取视频video
                        if (!myVideo.paused) {
                            myVideo.pause();
                        }
                        done();
                    }
                }).catch((err) => {
                    console.log(err)
                });
            } else if (e.fileType == 'pdf') {
                //PDF
                window.open(url)
            } else {
                this.$notify({
                    title: '警告',
                    message: '暂不支持该类型文件预览',
                    type: 'warning'
                });
            }
        },
        //下载选中部分
        downLoadList() {
            for (var i = 0; i < this.multipleSelection.length; i++) {
                if (this.multipleSelection[i].fileType == 'dir') {
                    sleep(100).then(() => {
                        this.$notify.error({
                            title: '错误',
                            message: '暂不支持下载文件夹'
                        })
                    });
                } else {
                    var a = document.createElement('a');//在dom树上创建一个a标签
                    var url = "/download?userFileId=" + this.multipleSelection[i].userFileId
                    a.href = url;//将url赋值给a标签的href属性
                    a.download = this.multipleSelection[i].userFileName;//设置设置下载文件的名称
                    a.click();//主动触发a标签点击事件
                }
            }
        },
        //重命名选中文件
        reNameList() {
            var file = this.multipleSelection[0]
            this.$prompt('请输文件名', '重命名', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                inputValue: file.userFileName,
            }).then(({value}) => {
                if (value === '') {
                    this.$message.error('文件名不能为空');
                } else {
                    axios({
                            url: '/reName',
                            data: {
                                "userFileId": file.userFileId,
                                "userFileName": value
                            },
                            method: 'POST',
                        }
                    ).then((res) => {
                        console.log(res.data);
                        if (res.data) {
                        } else {
                            this.$notify.error({
                                title:"失败",
                                message:"重命名失败"
                            })
                        }
                        this.getFilesByDir();
                    });
                }
            }).catch((err) => {
                console.log(err)
            });
        },
        //删除选中部分
        delList() {
            for (var i = 0; i < this.multipleSelection.length; i++) {
                this.delFileById(this.multipleSelection[i].userFileId);
            }
        },
        //添加到移动列表
        addToMoveList() {
            this.moveList = this.multipleSelection;
        },

        //移动
        moveToThis() {
            for (var i = 0; i < this.moveList.length; i++) {
                axios({
                    url: '/moveFile',
                    data: {
                        "userFileId": this.moveList[i].userFileId,
                        "dir": this.nowDirId
                    },
                    method: 'POST'
                }).then((res) => {
                    if (!res.data) {
                        this.$notify.error({
                            title: '错误',
                            message: '文件移动失败'
                        })
                    }
                    this.getFilesByDir();
                    this.moveList = []
                })
            }
        },
        //删除一项
        delFileById(userFileId) {
            axios({
                    url: '/delFileById',
                    data: {
                        "userFileId": userFileId
                    },
                    method: 'POST',
                }
            ).then((res) => {
                if (res.data) {
                } else {
                    this.$notify.error({
                        title:"失败",
                        message:"删除失败"
                    })
                }
                this.getFilesByDir();
            });
        },
        //表格内下拉菜单
        handleCommand(command) {
            // this.$message('click on item ' + command);
            console.log(command)
            if (command.button == 'download') {
                if (command.row.fileType == "dir") {
                    this.$notify.error({
                        title: '错误',
                        message: '暂不支持下载文件夹'
                    })
                } else {
                    window.location.href = "/download?userFileId=" + command.row.userFileId;
                }

            } else if (command.button == 'del') {
                this.delFileById(command.row.userFileId)
            } else if (command.button == 'move') {
                this.moveList = [command.row];
            } else if (command.button == 'rename') {
                this.$prompt('请输文件名', '重命名', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputValue: command.row.userFileName,
                }).then(({value}) => {
                    if (value === '') {
                        this.$message.error('文件名不能为空');
                    } else {
                        axios({
                                url: '/reName',
                                data: {
                                    "userFileId": command.row.userFileId,
                                    "userFileName": value
                                },
                                method: 'POST',
                            }
                        ).then((res) => {
                            console.log(res.data);
                            if (res.data) {
                            } else {
                                this.$notify.error({
                                    title:"失败",
                                    message:"重命名失败"
                                })
                            }
                            this.getFilesByDir();
                        });
                    }
                }).catch((err) => {
                    console.log(err)
                });
            }
            // console.log(e)
        },
        composeValue(item, row) {
            return {
                'button': item,
                'row': row
            }
        },

        //搜索
        search() {
            console.log(this.searchtxt)
        },
        //选项发生变化
        handleSelectionChange(val) {
            this.multipleSelection = val;
            // console.log(val)
            // if (val.length != 0) {
            //     this.showButton = true
            // } else {
            //     this.showButton = false
            // }
        },
        handleChange(file, fileList) {
            this.fileList = fileList.slice(-3);
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
                window.location.href = "/page/user"
            }
        },
        change(limit) {
            if (limit != null) {
                var size = ""
                if (limit < 1024) {
                    return limit + 'B'
                } else if (limit < 1024 * 1024) {
                    return (limit / 1024).toFixed(2) + "KB"
                } else if (limit < 1024 * 1024 * 1024) {
                    return (limit / 1024 / 1024).toFixed(2) + "MB"
                } else if (limit < 1024 * 1024 * 1024 * 1024) {
                    return (limit / 1024 / 1024 / 1024).toFixed(2) + "GB"
                }
            } else {
                return null;
            }
        }
    },
}
var Ctor = Vue.extend(Main)
new Ctor().$mount('#app')

function sleep(time) {
    return new Promise((resolve) => setTimeout(resolve, time));
}

function change(limit) {
    var size = "";
    if (limit < 0.1 * 1024) {                            //小于0.1KB，则转化成B
        size = limit.toFixed(2) + "B"
    } else if (limit < 0.1 * 1024 * 1024) {            //小于0.1MB，则转化成KB
        size = (limit / 1024).toFixed(2) + "KB"
    } else if (limit < 0.1 * 1024 * 1024 * 1024) {        //小于0.1GB，则转化成MB
        size = (limit / (1024 * 1024)).toFixed(2) + "MB"
    } else {                                            //其他转化成GB
        size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB"
    }

    var sizeStr = size + "";                        //转成字符串
    var index = sizeStr.indexOf(".");                    //获取小数点处的索引
    var dou = sizeStr.substr(index + 1, 2)            //获取小数点后两位的值
    if (dou == "00") {                                //判断后两位是否为00，如果是则删除00
        return sizeStr.substring(0, index) + sizeStr.substr(index + 3, 2)
    }
    return size;
}

// function addNum(){
//     $(".textarea").setTextareaCount({
//         width: "30px",
//         bgColor: "#999",
//         color: "#FFF",
//         display: "block"
//     });
// }