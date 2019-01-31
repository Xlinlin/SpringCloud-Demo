(()=>{

    App.moule({
        data :function() {
            return {
                form:{},
                tabStyle:{}
            }
        },
        activated(){
            //接收数据
            var editData = App.getData('editType');
            App.success("接收传过来的数据为："+JSON.stringify(editData));
            this.$nextTick(()=>{
                this.$set(this.tabStyle,'height',(App.MainVueApp.pageHeight - 150)+"px")
                this.$set(this.tabStyle,'overflow',"auto")
            })
        },
        methods:{
            submit(){
                App.post('api/demo/submit').setData(this.form).callSuccess((res)=>{
                    console.log(res)
                    App.success('保存成功')
                    App.closeCurrentTagNav()
                })
            },
        },
    });

})()