(() => {

    App.moule({
        data: function () {
            return {
                tableHeight: 0,
                selectionData: [],
                /*数据表格里的数据*/
                tableData: [{
                    id: '',
                    groupName: '',
                    groupDesc: '',
                    creatTime: '',
                    updateTime: '',
                }],
                pager: new App.Pager(),
                /*分页栏的 当前页*/
                tableCurrentPage: 1,
                /*分页栏的 每页行数*/
                tablePageSize: 10,
                /*分页栏的总条数*/
                tableTotal: 0,

                dialogFormVisible: false,
                /*查询API对应的form表单*/
                form: {
                    groupName: '',
                    createTime: '',
                    updateTime: '',
                    pageNum: '',
                    pageSize: '',
                },
                createForm: {
                    id: '',
                    groupName: '',
                    groupDesc: '',
                },
                title: '',
                rules: {
                    groupName: [{required: true, message: '请输入配置组名称', trigger: "blur"}],
                    groupDesc: [{required: true, message: '请输入配置组描述', trigger: "blur"}]
                },
                groupId: '',
            }
        },
        methods: {
            /*查询API*/
            findApi() {
                var that = this;
                that.form.pageNum = that.tableCurrentPage;
                that.form.pageSize = that.tablePageSize;
                App.request("/api/config/configGroup/page").post().setData(that.form).callSuccess(function (resp) {
                    that.tableData = resp.data.list;
                    that.tableTotal = resp.data.total;
                });
            },
            /*查询API 提交的函数*/
            onSubmit() {
                this.tableCurrentPage = 1;
                this.findApi();
            },
            /*重置查询*/
            resetForm() {
                var that = this;
                this.form = {};
                that.tableCurrentPage = 1;
                that.findApi();
            },
            /*分页栏对应的函数*/
            handleSizeChange(val) {
                this.tablePageSize = val;
                //console.log(`每页 ${val} 条`);
                this.findApi();
            },
            handleCurrentChange(val) {
                this.tableCurrentPage = val;
                // console.log(`当前页: ${val}`);
                this.findApi();
            },
            selectChagne(v) {
                this.selectionData = v;
            },
            del(datas) {
                var that = this;
                if (
                    !that.selectionData.length && !datas
                ) {
                    App.error('请选择记录');
                    return;
                }
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var selData = datas ? [datas] : that.selectionData;
                    var newData = '';
                    selData.forEach(function (t) {
                        newData = newData + t.id + ",";
                    });
                    App.request("/api/config/configGroup/delete/" + newData.substr(0, newData.length - 1)).post().callSuccess(function (resp) {
                        if (resp.data == 0) {
                            App.success('全部删除成功!');
                        } else {
                            App.error('部分删除成功，其中' + resp.data + '条已关联应用，不能进行删除，必须先解除应用关联!');
                        }
                        that.findApi();
                    });
                });
            },
            create(formName) {
                var that = this;
                that.$refs[formName].validate((valid) => {
                    if (valid) {
                        var url = '';
                        if (that.groupId == '' || that.groupId == null) {
                            url = 'api/config/configGroup/save';
                        } else {
                            url = 'api/config/configGroup/update';
                            that.createForm.id = that.groupId;
                        }
                        App.post(url).setData(that.createForm).callSuccess((res) => {
                            console.log(res);
                            if (!that.createForm.id) {
                                if (res) {
                                    App.success('保存成功');
                                    that.dialogFormVisible = false;
                                    that.findApi();
                                } else {
                                    App.error('保存失败');
                                }
                            } else {
                                if (res) {
                                    App.success('修改成功');
                                    that.dialogFormVisible = false;
                                    that.findApi();
                                } else {
                                    App.error('修改失败');
                                }
                            }
                        })
                    } else {
                        return false;
                    }
                });
            }
            ,
            open(title, data) {
                var that = this;
                that.title = title;
                if (data != null) {
                    that.createForm = data;
                    that.groupId = data.id;
                } else {
                    that.createForm = {};
                }
                that.dialogFormVisible = true;
            },
            refItem(group) {
                App.putData("editType", group.id);
                App.openModule("detail", "已关联配置项", "configgroup/detail.html");
            }
        },
        /* 组件创建完成事件  */
        created: function () {
            this.$nextTick(() => {
                this.tableHeight = App.MainVueApp.pageHeight - 220
            })
        },
        /* 模板编译挂载完成事件 类似小程序onload */
        mounted: function () {
            console.log('模板编译挂载完成事件');
        },
        /* 组件更新完成事件 */
        updated: function () {
            console.log('组件更新完成事件');
        },
        /*  组件被激活 类似小程序onshow */
        activated: function () {
            console.log('组件被激活');
            this.findApi();
        },
        /*  组件未被激活 类似小程序ondestroy */
        deactivated: function () {
            console.log('组件未激活');
        }
    });

})()