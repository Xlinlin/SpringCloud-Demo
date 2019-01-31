(() => {

    App.moule({
        data: function () {
            return {
                tableHeight: 0,
                selectionData: [],
                configModel: false,
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

            }
        },
        methods: {
            /*查询API*/
            findApi() {
                debugger
                var that = this;
                this.searchForm.appId = this.appId;
                //后台接口加载为关联的数据
                App.request('appManager/notRefApp').post().setData(that.searchForm).callSuccess((res) => {
                    //兼容manggo 的pager和github的pager
                    that.pager = new App.Pager(res.data);
                })
            },
            //查询该项目组下所有配置
            configDetail(data) {
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
            ref(datas) {
                debugger
                var that = this;
                if (!that.selectionData.length && !datas) {
                    App.error('请选择记录');
                    return;
                }
                var selData = datas ? [datas] : that.selectionData;
                var newData = '';
                selData.forEach(function (t) {
                    newData = newData + t.id + ",";
                });
                var postData = {};
                postData.appId = this.appId;
                postData.groupIds = newData.substr(0, newData.length - 1);
                console.log(postData);
                App.request('appManager/batchSaveRef', postData).callSuccess((res) => {
                    if (res.data) {
                        App.success('添加成功!');
                        App.closeCurrentTagNav();
                        App.putData("appId", this.appId);
                    }
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
            //批量关联
            batchRef() {
                console.log("");
                this.ref();
            },
            //修改配置跳转
            updateConfig(data) {
                App.putData("configItem", data);
                App.openModule("#config/configitem.html", "配置项管理", "config/configitem.html")
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
            debugger
            console.log('组件被激活');
            this.appId = App.getData("appId");
            App.removeData("appId");
            this.findApi();
        },
        /*  组件未被激活 类似小程序ondestroy */
        deactivated: function () {
            console.log('组件未激活');
        }
    });

})()