//模拟菜单数据
Mock.mock(/api\/user\/menuList/, new MockResult([
    {
        "id": "#1",
        "menuId": "#1",
        "name": "首页",
        "url": "#home/home.html",
        "iconCls": "iconfont icon-home",
        "componentId": "admin-home"
    },
    {"id": "2", "menuId": "2", "name": "应用管理", "url": "#app/app.html", "iconCls": "iconfont icon-gaiicon-"},
    {
        "id": "3", "menuId": "3", "name": "配置管理", "iconCls": "iconfont icon-peizhi",
        children: [
            {
                "id": "31",
                "menuId": "3-31",
                "name": "配置项管理",
                "url": "#config/configitem.html",
                "iconCls": "el-icon-setting"
            }, {
                "id": "32",
                "menuId": "3-32",
                "name": "配置组管理",
                "url": "#configgroup/configgroup.html",
                "iconCls": "el-icon-setting"
            }
        ]
    },
    {"id": "4", "menuId": "4", "name": "区域管理", "url": "#region/region.html", "iconCls": "iconfont icon-quyu"},
    {"id": "5", "menuId": "5", "name": "服务管理", "url": "#server/serverlist.html", "iconCls": "iconfont el-icon-document"},
    // {
    //     "id": "5", "menuId": "5", "name": "演示", "iconCls": "iconfont icon-yanshi",
    //     children: [
    //         {
    //             "id": "51",
    //             "menuId": "5-51",
    //             "name": "查询演示",
    //             "url": "#template/template.html",
    //             "iconCls": "el-icon-search"
    //         },
    //     ]
    // },
]));

//应用数据
(function () {
    var pager = new App.Pager()
    delete pager.list;
    pager["list|1-100"] = [
        {
            "id|1-1000000": 0,
            "name": "消息服务",
            "desc": "这是应用描述。。。。",
            "person|1": [Mock.Random.cname(), Mock.Random.cname(), Mock.Random.cname(), Mock.Random.cname(), Mock.Random.cname(), Mock.Random.cname(), Mock.Random.cname(), Mock.Random.cname(), Mock.Random.cname()],
            "creator": "toga",
            "createTime": new Date(),
            "envList": [
                {
                    "profile": "dev",
                    "url": "http://localhost:7775"
                },
                {
                    "profile": "test",
                    "url": "http://localhost:7775"
                }
            ]
        }
    ];

    Mock.mock(/api\/app\/query/, new MockResult(pager))

})()

