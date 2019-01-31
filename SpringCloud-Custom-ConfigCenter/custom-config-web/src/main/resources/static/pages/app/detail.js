(() => {

    App.moule({
        data: function () {
            return {
                appData: {
                    id: "",
                    application: '',
                    applicationName: '',
                    label: 'master',
                    profile: '',
                    region: '',
                    regionName: ''
                },
                update: false,
                tabStyle: {},
                // 区域
                regions: [],
                //校验规则
                rules: {
                    application: [
                        {required: true, message: '请输入应用名称', trigger: 'blur'},
                        {min: 3, max: 128, message: '长度在 3 到 128 个字符', trigger: 'blur'}
                    ],
                    profile: [
                        {required: true, message: '请选择一个环境', trigger: 'change'}
                    ],
                    region: [
                        {required: true, message: '请选择一个区域', trigger: 'change'}
                    ]
                }
            }
        },
        activated() {
            //接收数据
            var editData = App.getData("editData");

            //初始化值
            this.appData.application = editData.application;
            this.appData.id = editData.id;
            this.appData.applicationName = editData.applicationName;
            this.appData.label = editData.label;
            this.appData.profile = editData.profile;
            this.appData.region = editData.regionId;
            console.log(this.appData)

            // App.success("接收传过来的数据为：" + JSON.stringify(editData));
            this.$nextTick(() => {
                this.$set(this.tabStyle, 'height', (App.MainVueApp.pageHeight - 150) + "px")
                this.$set(this.tabStyle, 'overflow', "auto")
            })
        },
        methods: {
            submit(formName) {
                console.log(formName);
                debugger
                console.log(this.appData);
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        App.post('/appManager/update').setData(this.appData).callSuccess((res) => {
                            // console.log(res.success);
                            if (res.success) {
                                App.success('保存成功');
                                App.openModule("detail", "应用管理", "app/app.html");
                            }
                            else {
                                App.error("添加应用错误，对应的应用、环境已经存在");
                            }
                        })
                    } else {
                        App.success('必填参数错误!');
                        return false;
                    }
                });

            },
            //关联配置项组
            relation() {
                // TODO
            }
        },
        /* 模板编译挂载完成事件 类似小程序onload */
        mounted: function () {
            // console.log('模板编译挂载完成事件');
            // 加载区域信息
            App.request("/appManager/queryAllRegion").callSuccess((res) => {
                // 无区域可用，提示添加区域
                if (res.data.length == 0) {
                    //调整添加区域
                    App.error("暂无区域可选择，请先添加区域!!");
                    //App.openModule("addRegion","添加区域","template/detail.html");
                } else {
                    debugger
                    this.regions = res.data;
                }
            })
        },
        /*  组件未被激活 类似小程序ondestroy */
        deactivated: function () {
            // console.log('未激活');
        }
    });

})()