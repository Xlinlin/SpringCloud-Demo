(() => {

    App.moule({
        data: function () {
            return {
                tableHeight: 0,
                deleteModal: false,
                /*数据表格里的数据*/
                tableData: [{
                    id: '',
                    applicationClientId: '',
                    application: '',
                    hostIp: '',
                    hostPort: '',
                    status: '',
                    createTime: '',
                    updateTime: '',
                }],
                pager: new App.Pager(),
                /*分页栏的 当前页*/
                tableCurrentPage: 1,
                /*分页栏的 每页行数*/
                tablePageSize: 10,
                /*分页栏的总条数*/
                tableTotal: 0,
                /*查询API对应的form表单*/
                form: {
                    hostIp: '',
                    application: '',
                    status: ''
                }
            }
        },
        methods: {
            statusFormatter(v) {
                if (v.status == 0) {
                    return "在线";
                } else if (v.status == 1) {
                    return "离线";
                }
            },
            /*查询API*/
            findApi() {
                var that = this;
                that.form.pageNum = that.tableCurrentPage;
                that.form.pageSize = that.tablePageSize;
                App.request("/api/config/clientInfo/page").post().setData(that.form).callSuccess(function (resp) {
                    that.tableData = resp.data.list;
                    // console.log(resp.data.list);
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
            deleteBatch() {
                //获取选择记录
                if (!this.selectionData.length) {
                    App.error('请选择记录');
                    return;
                }
                this.deleteModal = true;
            },
            confirmDelete() {
                var _this = this;
                App.post('/api/server/delect').setData(_this.selectionData)
                    .setLoadArea(_this.$refs.deleteBtn).callSuccess(() => {
                    App.success('删除成功！');
                    _this.deleteModal = false
                });
            },
            del(row) {
                var that = this;
                that.$confirm('此操作将永久删除该服务器信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var id = row.id;
                    App.request("/api/config/clientInfo/del", {"id": id})
                        .callSuccess((res) => {
                            if (res.data) {
                                App.success('删除成功');
                            } else {
                                App.error('删除失败');
                            }
                            that.findApi();
                        });
                }).catch(() => {
                    that.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
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