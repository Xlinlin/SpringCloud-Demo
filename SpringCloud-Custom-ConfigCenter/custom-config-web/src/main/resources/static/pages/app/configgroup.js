(() => {

    App.moule({
        data: function () {
            return {
                tableHeight: 0,
                selectionData: [],
                configModel: false,
                activeName: 'itemGroup',
                //配置组中的配置项列表数据
                configPager: new App.Pager(),
                /*数据表格里的数据*/
                tableData: [{
                    id: '',
                    groupName: '',
                    groupDesc: '',
                    creatTime: '',
                    updateTime: '',
                }],
                pager: new App.Pager(),
                appId: '',
                /*查询API对应的form表单*/
                searchForm: {
                    appId: '',
                    groupName: '',
                    groupDesc: ''
                },

                /***********私有配置相关************/
                privateModel: false,
                privateSelData: [],
                // 私有配置列表
                privatePager: new App.Page(),
                //查询表单
                priSearchForm: {
                    applicationId: '',
                    itemKey: '',
                    itemDesc: ''
                },
                // 新增/修改参数
                privTabDate: {
                    id: '',
                    applicationId: '',
                    itemKey: '',
                    itemValue: '',
                    itemDesc: ''
                },
                rules: {
                    itemKey: [{required: true, message: '请输入配置项键', trigger: "blur"}],
                    itemValue: [{required: true, message: '请输入配置项值', trigger: "blur"}]
                },
                isUpdate: false
            }
        },
        methods: {
            /* tab事件*/
            handleClick(tab, event) {
                console.log(tab, event);
            },
            /*查询API*/
            findApi() {
                var that = this;
                this.searchForm.appId = this.appId;
                // console.log(that.searchForm);
                //后台接口加载数据
                App.request('appManager/queryItemGroup').post().setData(that.searchForm).callSuccess((res) => {
                    //兼容manggo 的pager和github的pager
                    that.pager = new App.Pager(res.data);
                })
            },
            //查询该项目组下所有配置
            configDetail(data) {
                debugger
                //配置项列表
                var that = this;
                //查询当前组下的所有配置
                App.request('/api/config/configGroup/isRefGroup').post().setData({
                    "groupId": data.id,
                    'pageSize': 100
                }).callSuccess((res) => {
                    that.configPager = new App.Pager(res.data);
                    that.configModel = true;
                })
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
                this.$confirm('此操作将永久删除该配置组, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    // var selData = datas ? [datas] : that.selectionData;
                    // var newData = '';
                    // selData.forEach(function (t) {
                    //     newData = newData + t.id + ",";
                    // });
                    // var groupIds = newData.substr(0, newData.length - 1);
                    var groupId = datas.id;
                    App.request("appManager/delItemGroup", {
                        itemGroupId: groupId,
                        appId: this.appId
                    }).post().callSuccess(function (resp) {
                        if (resp) {
                            App.success('删除成功');
                        } else {
                            App.error('删除失败');
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
                    that.groupId = data.id;
                } else {
                    that.createForm = {};
                }
                that.dialogFormVisible = true;
            },
            addConfGroup() {
                App.putData("appId", this.appId);
                App.openModule("app/refconfiggroup", "添加配置组", "app/refconfiggroup.html")
            },
            //修改配置跳转
            updateConfig(data) {
                App.putData("configItem", data);
                App.openModule("#config/configitem.html", "配置项管理", "config/configitem.html")
            },

            /*****************************私有配置列表**********************************/
            //添加私有配置
            addPrivateConf() {
                this.isUpdate = false;
                this.privateModel = true;
            },
            //查找私有配置
            findPrivateConf() {
                var url = "appManager/queryPrivateConfig";
                this.priSearchForm.applicationId = this.appId;
                var _this = this;
                App.post(url).setData(this.priSearchForm).callSuccess((res) => {
                    console.log(res)
                    _this.privatePager = new App.Pager(res.data);
                })
            },
            // 修改私有配置
            editPrivate(row) {
                var url = "appManager/updatePrivateItem";
                this.privateModel = true;
                this.privTabDate.id = row.id;
                this.privTabDate.applicationId = row.applicationId;
                this.privTabDate.itemDesc = row.itemDesc;
                this.privTabDate.itemKey = row.itemKey;
                this.privTabDate.itemValue = row.itemValue;
                this.isUpdate = true;
            },
            //删除某项私有属性
            delPrivate(row) {
                if (row) {
                    var url = "appManager/delPrivateItem?id=" + row.id;
                    var that = this;
                    this.$confirm('此操作将永久删除该配置项, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        App.request(url).callSuccess(function (resp) {
                            if (resp.data) {
                                App.success('删除成功!');
                                that.findPrivateConf();
                            } else {
                                App.error("删除失败!")
                            }
                        });
                    });
                }
            },
            selPrivateChange(v) {
                this.privateSelData = v;
            },
            //提交私有配置
            submitPrivate() {
                var that = this;
                this.privTabDate.applicationId = this.appId;
                this.$refs['privTabDate'].validate((valid) => {
                    if (valid) {
                        debugger
                        var url = "appManager/addPrivateItem";
                        if (that.isUpdate) {
                            url = "appManager/updatePrivateItem";
                        }
                        App.post(url).setData(that.privTabDate).callSuccess((res) => {
                            // console.log(res)
                            if (res.data) {
                                if (that.isUpdate) {
                                    App.success('更新成功')
                                } else {
                                    App.success('保存成功')
                                }
                                that.cleanPriTab();
                                that.privateModel = false;
                                that.findPrivateConf();
                            } else {
                                App.error('操作失败')
                            }
                            that.isUpdate = false;
                        })
                    } else {
                        return false;
                    }
                });
            },
            cleanPriTab() {
                this.privTabDate.id = '';
                this.privTabDate.applicationId = '';
                this.privTabDate.itemValue = '';
                this.privTabDate.itemKey = '';
                this.privTabDate.itemDesc = '';
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
            // console.log('模板编译挂载完成事件');
        },
        /* 组件更新完成事件 */
        updated: function () {
            // console.log('组件更新完成事件');
        },
        /*  组件被激活 类似小程序onshow */
        activated: function () {
            // console.log('组件被激活');
            var appData = App.getData("relationData");
            this.activeName = 'itemGroup';
            this.isUpdate = false;
            if (appData) {
                this.appId = appData.id;
                App.removeData("relationData");
                this.findApi();
                this.findPrivateConf();
            }
            else {
                App.closeCurrentTagNav();
            }
        },
        /*  组件未被激活 类似小程序ondestroy */
        deactivated: function () {
            // console.log('组件未激活');
        }
    });

})()