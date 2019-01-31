(() => {

    App.moule({
        data: function () {
            return {
                tableHeight: 0,
                selectionData: [],
                /*数据表格里的数据*/
                tableData: [{
                    id: '',
                    itemKey: '',
                    itemValue: '',
                    itemDesc: '',
                    createTime: '',
                    updateTime: '',
                    status: '',
                    itemType: '',
                }],
                dialogFormVisible: false,
                pager: new App.Pager(),
                /*分页栏的 当前页*/
                tableCurrentPage: 1,
                /*分页栏的 每页行数*/
                tablePageSize: 10,
                /*分页栏的总条数*/
                tableTotal: 0,

                /*查询API对应的form表单*/
                form: {
                    itemKey: '',
                    itemDesc: '',
                    status: '',
                    itemType: '',
                    pageNum: '',
                    pageSize: '',
                },
                createForm: {
                    id: '',
                    itemKey: '',
                    itemValue: '',
                    itemDesc: '',
                    itemType: '',
                    status: 0,
                },
                title: '',
                rules: {
                    itemKey: [{required: true, message: '请输入配置项键', trigger: "blur"}],
                    itemValue: [{required: true, message: '请输入配置项值', trigger: "blur"}]
                },
                itemId: '',
            }
        },
        methods: {
            /*查询API*/
            findApi() {
                var that = this;
                that.form.pageNum = that.tableCurrentPage;
                that.form.pageSize = that.tablePageSize;
                App.request("/api/config/configItem/page").post().setData(that.form).callSuccess(function (resp) {
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
            fomatterStatus(v) {
                if (v.status == 0) {
                    return "启用";
                } else if (v.status == 1) {
                    return "禁用";
                }
            },
            fomatterItemType(v) {
                if (v.itemType == 0) {
                    return "通用";
                } else if (v.itemType == 1) {
                    return "开发环境";
                } else if (v.itemType == 2) {
                    return "测试环境";
                } else if (v.itemType == 3) {
                    return "生产环境";
                } else if (v.itemType == 4) {
                    return "其他";
                }
            },
            selectChagne(v) {
                this.selectionData = v;
            },
            del(datas) {
                var that = this;
                if (!that.selectionData.length && !datas) {
                    App.error('请选择记录');
                    return;
                }
                this.$confirm('此操作将永久删除该配置项, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var selData = datas ? [datas] : that.selectionData;
                    var newData = '';
                    selData.forEach(function (t) {
                        newData = newData + t.id + ",";
                    });
                    App.request("/api/config/configItem/batchDelete/" + newData.substr(0, newData.length - 1)).post().setData(newData).callSuccess(function (resp) {
                        if (resp.data == 0) {
                            App.success('删除成功!');
                        } else {
                            //App.error('部分删除成功，其中' + resp.data + "条配置项已经关联配置组在使用中不能进行删除，必须删除组!");
                            App.error("不能删除，请先从配置组中删除该项的使用关系!")
                        }
                        that.findApi();
                    });
                });
            },
            open(title, data) {
                var that = this;
                that.title = title;
                if (data != null) {
                    that.createForm = data;
                    that.itemId = data.id;
                } else {
                    that.createForm = {};
                }
                that.dialogFormVisible = true;
            },
            submit(formName) {
                var that = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var url = '';
                        if (that.itemId == '' || that.itemId == null) {
                            url = 'api/config/configItem/save';
                            that.createForm.status = 0;
                            if (!that.createForm.itemType) {
                                that.createForm.itemType = 0;
                            }
                        } else {
                            url = 'api/config/configItem/update';
                            that.createForm.id = that.itemId;
                        }
                        App.post(url).setData(that.createForm).callSuccess((res) => {
                            console.log(res)
                            if (!that.createForm.id) {
                                if (res) {
                                    App.success('保存成功')
                                    that.dialogFormVisible = false;
                                    that.findApi();
                                } else {
                                    App.success('保存失败')
                                }
                            } else {
                                if (res) {
                                    App.success('修改成功')
                                    that.dialogFormVisible = false;
                                    that.findApi();
                                } else {
                                    App.success('修改失败')
                                }
                            }
                        })
                    } else {
                        return false;
                    }
                });

            },
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

            var itemData = App.getData("configItem");
            if (itemData) {
                App.removeData("configItem");
                this.open('修改配置项', itemData);
            }

        },
        /*  组件未被激活 类似小程序ondestroy */
        deactivated: function () {
            console.log('组件未激活');
        }
    });

})()