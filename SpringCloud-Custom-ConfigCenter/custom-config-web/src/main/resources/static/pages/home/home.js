(()=>{

    App.moule({
        data :function() {
            return {
                prop: "我是属性",
                dialogVisible: false
            }
        },
        methods:{
            onClick(){
                 App.openModule("api_ut",'API单元测试','#api_ut/api_ut.html');
            },
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {});
            },
            onSubmit(){
                alert("aa")
                this.dialogVisible=false;
            }
        },
        /* 组件创建完成事件  */
        created :function(){
            console.log('组件创建完成事件');
        },
        /* 模板编译挂载完成事件 类似小程序onload */
        mounted :function(){
            console.log('模板编译挂载完成事件');
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