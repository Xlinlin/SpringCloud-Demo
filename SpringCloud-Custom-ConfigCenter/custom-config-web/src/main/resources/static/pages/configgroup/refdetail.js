(() => {

    App.moule({
        data: function () {
            return {
                tableHeight: 0,
                selectionData: [],
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
                form: {
                    groupId: '',
                    itemKey: '',
                    pageNum: '',
                    pageSize: '',
                },
                notform: {
                    groupId: '',
                    itemKey: '',
                    pageNum: '',
                    pageSize: '',
                },
                /*分页栏的 当前页*/
                tableCurrentPage: 1,
                /*分页栏的 每页行数*/
                tablePageSize: 10,
                /*分页栏的总条数*/
                tableTotal: 0,
                groupId:
                    '',
            }
        },
        /* 组件创建完成事件  */
        created: function () {
            this.$nextTick(() => {
                this.tableHeight = App.MainVueApp.pageHeight - 220
            })
        },
        activated() {
            var that = this;
            //接收数据
            var editData = App.getData('editType');
            that.groupId = editData;
            this.findApi();
        },
        methods: {
            /*查询API*/
            findApi() {
                var that = this;
                that.form.pageNum = that.tableCurrentPage;
                that.form.pageSize = that.tablePageSize;
                that.form.groupId = that.groupId;
                App.request("/api/config/configGroup/notRefGroup").post().setData(that.form).callSuccess(function (resp) {
                    that.tableData = resp.data.list;
                    that.tableTotal = resp.data.total;
                });
            },
            selectChagne(v) {
                this.selectionData = v;
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
            refItem(datas) {
                var that = this;
                if (
                    !that.selectionData.length && !datas
                ) {
                    App.error('请选择记录');
                    return;
                }
                var selData = datas ? [datas] : that.selectionData;
                var newData = '';
                selData.forEach(function (t) {
                    newData = newData + t.id + ",";
                });
                App.request("/api/config/configGroup/batchSave/" + that.groupId + "/" + newData.substr(0, newData.length - 1)).post().callSuccess(function (resp) {
                    if (resp) {
                        App.success('添加成功');
                    } else {
                        App.error('添加失败');
                    }
                    that.findApi();
                });
            }
        },
    });

})()