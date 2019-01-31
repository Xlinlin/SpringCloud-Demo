(()=>{

    App.moule({
        data :function() {
            return {
            	/*添加区域的弹出框*/
                addServer: false,
            	tableHeight:0,
            	deleteModal:false,
                /*数据表格里的数据*/
                tableData: [{
                    id: '',
                    serverHost: '',
                    serverDesc: '',
                    regionName: '',
                    createTime: '',
                    updateTime: '',
                }],
                pager:new App.Pager(),
                /*分页栏的 当前页*/
                tableCurrentPage: 1,
                /*分页栏的 每页行数*/
                tablePageSize: 10,
                /*分页栏的总条数*/
                tableTotal: 0,
                addServerform:{
                	id: '',
                    serverHost: '',
                    serverDesc: '',
                    regionId: '',
                    regionName:''
                },
             // 区域
                regions: [],
                /*查询API对应的form表单*/
                form: {
                	serverHost: '',
                	serverDesc: '',
                    createTime: '',
                    updateTime: '',
                    pageNum: '',
                    pageSize: '',
                },
                rules: {
                	serverHost: [{required: true, message: '请输入服务器IP', trigger: "blur"}],
                	serverDesc:[{required: true, message: '请输入服务器描述', trigger: "blur"}],
                	regionId: [{required: true, message: '请选择一个区域', trigger: 'change'}
                    ]
                },
                itemId: '',
                title: '',
            }
        },
        methods:{
        	 /*查询API*/
            findApi() {
                var that = this;
                that.form.pageNum = that.tableCurrentPage;
                that.form.pageSize = that.tablePageSize;
                App.request("/api/serverHostConfig/page").post().setData(that.form).callSuccess(function (resp) {
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
            selectChagne(v){
                this.selectionData = v;
            },
            deleteBatch(){
                //获取选择记录
                if ( !this.selectionData.length ){
                    App.error('请选择记录');
                    return;
                }
                this.deleteModal=true;
            },
            confirmDelete(){
                var _this = this;
                App.post('/api/server/delect').setData(_this.selectionData)
                    .setLoadArea(_this.$refs.deleteBtn).callSuccess( ()=> {
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
                	var id=row.id;
                    App.request("/api/serverHostConfig/delectServerHostConfig/" + id).post().callSuccess(function (resp) {
                    	debugger;
                    	if (resp.data == 1) {
                            App.success('删除成功');
                        }else{
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
            open(title, data) {
                var that = this;
                that.title = title;
                if (data != null) {
                    that.addServerform = data;
                    that.itemId = data.id;
                } else {
                    that.addServerform = {};
                }
                that.addServer = true;
            },
            submit(formName) {
                       var that = this;
                        var url = '';
                        if (that.itemId == '' || that.itemId == null) {
                            url = '/api/serverHostConfig/addServerHostConfig';
                            
                        } else {
                            url = '/api/serverHostConfig/updateServerHostConfig';
                            that.addServerform.id = that.itemId;
                        }
                        if(that.addServerform.serverDesc != null && that.addServerform.serverDesc != "" && that.addServerform.serverHost != null && that.addServerform.serverHost != "" && that.addServerform.regionId != null && that.addServerform.regionId != ""){
                        App.post(url).setData(this.addServerform).callSuccess((res) => {
                            console.log(res)
                            if (!that.addServerform.id) {
                                if (res) {
                                    App.success('保存成功')
                                    that.addServer = false;
                                    that.findApi();
                                } else {
                                    App.success('保存失败')
                                }
                            } else {
                                if (res) {
                                    App.success('修改成功')
                                    that.addServer = false;
                                    that.itemId ='';
                                    that.findApi();
                                } else {
                                    App.success('修改失败')
                                }
                            }
                        })}
                        else{
                            that.$message({
                                showClose: true,
                                message: '服务器ip或者区域或者描述不能为空！',
                                type: 'error'
                            });
                        }

            },
        },
        /* 组件创建完成事件  */
        created :function(){
            this.$nextTick(()=>{
                this.tableHeight = App.MainVueApp.pageHeight - 220
            })
        },
        /* 模板编译挂载完成事件 类似小程序onload */
        mounted :function(){
        	 App.request("/appManager/queryAllRegion").callSuccess((res) => {
                 // 无区域可用，提示添加区域
                 if (res.data.length == 0) {
                     //调整添加区域
                     App.error("暂无区域可选择，请先添加区域!!");
                     //App.openModule("addRegion","添加区域","template/detail.html");
                 } else {
                     this.regions = res.data;
                 }
             })
        },
        /* 组件更新完成事件 */
        updated:function(){
            console.log('组件更新完成事件');
        },
        /*  组件被激活 类似小程序onshow */
        activated :function(){
            console.log('组件被激活');
            this.findApi();
        },
        /*  组件未被激活 类似小程序ondestroy */
        deactivated :function() {
            console.log('组件未激活');
        }
    });

})()