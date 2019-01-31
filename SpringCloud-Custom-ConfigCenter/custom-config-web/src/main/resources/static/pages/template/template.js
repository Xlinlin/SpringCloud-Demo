(()=>{

    App.moule({
        data :function() {
            return {
                prop: "我是属性",
                tableHeight:0,
                deleteModal:false,
                selectionData:[],
                pager:new App.Pager(),
            }
        },
        methods:{
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
                App.post('/api/template/test').setData(_this.selectionData)
                    .setLoadArea(_this.$refs.deleteBtn).callSuccess( ()=> {
                    App.success('删除成功！');
                    _this.deleteModal = false
                });
            },
            edit(row){
                //传递数据
                App.putData("editData",row);
                App.openModule("detail","信息编辑","template/detail.html");
            },
            del(row){
                this.$set(this,'selectionData',[row]);
                this.deleteModal = true;
            },
            create(){
                //传递数据
                App.putData("editType","new");
                App.openModule("detail","信息创建","template/detail.html");
            }
        },
        /* 组件创建完成事件  */
        created :function(){
            this.$nextTick(()=>{
                this.tableHeight = App.MainVueApp.pageHeight - 220
            })
        },
        /* 模板编译挂载完成事件 类似小程序onload */
        mounted :function(){
            console.log('模板编译挂载完成事件');
            App.request('api/demo/tempate').callSuccess((res)=>{
                //兼容manggo 的pager和github的pager
                this.pager = new App.Pager(res.data);
            })
        },
        /* 组件更新完成事件 */
        updated:function(){
            console.log('组件更新完成事件');
        },
        /*  组件被激活 类似小程序onshow */
        activated :function(){
            console.log('组件被激活');
        },
        /*  组件未被激活 类似小程序ondestroy */
        deactivated :function() {
            console.log('组件未激活');
        }
    });

})()