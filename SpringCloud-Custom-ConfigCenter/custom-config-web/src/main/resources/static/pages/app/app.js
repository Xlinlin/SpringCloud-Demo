(() => {
    App.moule({
        data: function () {
            return {
                prop: "我是属性",
                tableHeight: 0,
                deleteModal: false,
                selectionData: [],
                searchForm: {
                    application: '',
                    profile: '',
                    applicationName: ''
                },
                pager: new App.Pager(),
            }
        },
        methods: {
            //选中事件
            selectChange(v) {
                this.selectionData = v;
            },
            search() {
                //后台接口加载数据
                this.loadData();
            },
            //选择删除事件
            deleteBatch() {
                //获取选择记录
                if (!this.selectionData.length) {
                    App.error('请选择记录');
                    return;
                }
                // this.deleteModal = true;
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var _this = this;
                    App.request("appManager/delete", {"id": _this.selectionData[0].id})
                        .callSuccess((res) => {
                            if (res.data) {
                                App.success('删除成功!');
                                _this.loadData();
                            } else {
                                App.error("删除失败!");
                            }
                        });
                });
            },
            confirmDelete() {
                this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var _this = this;
                    App.request("appManager/delete", {"id": _this.selectionData[0].id})
                        .callSuccess((res) => {
                            if (res.data) {
                                App.success('删除成功!');
                                _this.loadData();
                            } else {
                                App.error("删除失败!");
                            }
                        });
                });
            },
            edit(row) {
                //传递数据
                App.putData("editData", row);
                App.putData("editType", "update");
                App.openModule("edit", "信息编辑", "app/detail.html");
            },
            //关联数据
            relation(row) {
                //传递数据
                App.putData("relationData", row);
                App.openModule("config", "配置管理", "app/configgroup.html");
            },
            del(row) {
                this.$set(this, 'selectionData', [row]);
                // this.deleteModal = true;
                this.$confirm('此操作将永久删除该应用, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var _this = this;
                    App.request("appManager/delete", {"id": _this.selectionData[0].id})
                        .callSuccess((res) => {
                            if (res.data) {
                                App.success('删除成功!');
                                _this.loadData();
                            } else {
                                App.error("删除失败!");
                            }
                        });
                });
            },
            //发布配置，动态更新
            refresh(row) {
                this.$confirm('确定发布配置?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var _this = this;
                    App.request("appManager/refresh", {"id": row.id})
                        .callSuccess((res) => {
                            if (res.data) {
                                App.success('发布成功!');
                            } else {
                                App.error("发布失败,确认应用在线,有问题请联系管理员!");
                            }
                        });
                });
            },
            create() {
                //传递数据-新增
                App.openModule("add", "新增应用配置", "app/add.html");
            },
            loadData: function () {
                //后台接口加载数据
                App.request('appManager/pageQuery').post().setData(this.searchForm).callSuccess((res) => {
                    //兼容manggo 的pager和github的pager
                    this.pager = new App.Pager(res.data);
                })
            },
            /*重置查询*/
            resetForm: function () {
                var that = this;
                that.searchForm = {};
                that.tableCurrentPage = 1;
                that.loadData();
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
            // console.log('模板编译挂载完成事件');
            //后台接口加载数据
            this.loadData();
        },
        /* 组件更新完成事件 */
        updated: function () {
            // console.log('组件更新完成事件');
        },
        /*  组件被激活 类似小程序onshow */
        activated: function () {
            // console.log('组件被激活');
            this.loadData();
        },
        /*  组件未被激活 类似小程序ondestroy */
        deactivated: function () {
            // console.log('组件注销');
        }
    });

})()