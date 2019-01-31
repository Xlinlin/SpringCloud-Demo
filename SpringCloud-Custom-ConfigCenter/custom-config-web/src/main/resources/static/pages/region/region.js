(() => {

    App.moule({
        data: function () {
            return {
                /*添加区域的弹出框*/
                addRegion: false,
                tableHeight: 0,
                /*复选框*/
                selectionData: [],
                /*数据表格里的数据*/
                tableData: [{
                    id: '',
                    regionName: '',
                    regionDesc: '',
                    createTime: '',
                    updateTime: '',
                }],

                /*分页栏的 当前页*/
                tableCurrentPage: 1,
                /*分页栏的 每页行数*/
                tablePageSize: 10,
                /*分页栏的总条数*/
                tableTotal: 0,

                /*查询API对应的form表单*/
                form: {
                    regionName: '',
                    createTime: '',
                    updateTime: '',
                },

                /*添加区域的from*/
                addRegionForm: {
                    regionName: '',
                    regionDesc: '',
                },

                /*添加表单输入项的校验*/
                regionRules: {
                    regionName: [
                        {required: true, message: '请输入区域名称', trigger: 'blur'}
                    ],
                    regionDesc: [
                        {required: true, message: '请输入区域描述', trigger: 'blur'}
                    ],
                }
            }
        },
        methods: {
            /*查询API*/
            findApi() {
                var that = this;
                that.form.pageNum = that.tableCurrentPage;
                that.form.pageSize = that.tablePageSize;
                App.request("/region/queryRegion").post().setData(that.form).callSuccess(function (resp) {
                    that.tableData = resp.data.list;
                    that.tableTotal = resp.data.total;
                })
            },

            /*分页栏对应的函数*/
            handleSizeChange(val) {
                this.tablePageSize = val;
                this.findApi();
            },
            handleCurrentChange(val) {
                this.tableCurrentPage = val;
                this.findApi();
            },

            /* 添加区域 打开窗口*/
            addRegionFrom() {
                var that = this;
                if (that.addRegionForm.regionName != null && that.addRegionForm.regionName != "" && that.addRegionForm.regionDesc != null && that.addRegionForm.regionDesc != "") {
                    App.request("/region/addRegion").post().setData(that.addRegionForm).callSuccess(function (resp) {
                        if (resp.data) {
                            that.$message({
                                type: 'success',
                                message: '成功!'
                            });
                        } else {
                            that.$message({
                                type: 'error',
                                message: '失败!'
                            });
                        }
                        ;
                        that.addRegionClose();
                    });
                } else {
                    that.$message({
                        showClose: true,
                        message: '区域名称或者区域描述不能为空！',
                        type: 'error'
                    });
                }
            },

            // 关闭添加region的dialog
            addRegionClose() {
                this.addRegion = false;
                this.addRegionForm = {
                    regionName: '',
                    regionDesc: '',
                }
                this.$refs["addRegionForm"].resetFields();
                this.findApi();
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

            selectChagne(v) {
                this.selectionData = v;
            },
            /*删除API*/
            deleteRow(row) {
                var that = this;
                if (
                    !that.selectionData.length && !row
                ) {
                    App.error('请选择记录');
                    return;
                }
                that.$confirm('此操作将永久删除该区域信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var selData = row ? [row] : that.selectionData;
                    var newData = '';
                    selData.forEach(function (t) {
                        newData = newData + t.id + ",";
                    });
                    App.request("/region/delectRegion/" + newData.substr(0, newData.length - 1)).post().callSuccess(function (resp) {
                        if (resp.data > 0) {
                            that.$message({
                                type: 'success',
                                message: '删除成功!'
                            });
                            that.findApi();
                        } else if (resp.data == -1) {
                            App.error("已关联应用不能删除，必须先删除应用!")
                        }
                        else {
                            that.$message({
                                type: 'error',
                                message: '删除失败!'
                            });
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

            /*更新区域*/
            handleClick: function (row) {
                this.addRegion = true;
                this.addRegionForm = row;
            },
        },
        /*  组件被激活 类似小程序onshow */
        activated: function () {
            this.findApi();
        },
        /* 组件创建完成事件  */
        created: function () {
            this.$nextTick(() => {
                this.tableHeight = App.MainVueApp.pageHeight - 220
            })
        },
    });
})()