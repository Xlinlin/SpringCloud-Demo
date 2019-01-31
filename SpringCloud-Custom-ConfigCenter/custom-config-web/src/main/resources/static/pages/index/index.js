/**
 * 系统架构
 * @author zhdong
 * @date 2018/8/26
 */
/*********全局混入*********/
Vue.mixin({
    created(){
        this.$getPath = function(url){
            return App.getPath(url);
        }
    }
});

App.start({
    el: '#app',
    data: function() {
        return {
            activeName:'',
            cacheList:[],
            homeData:{"id":"#1","menuId":"#1","name":"首页","url":"#home/home.html","iconCls":"iconfont icon-home","componentId":"admin-home"},
            tagNavList:[{"id":"#1","menuId":"#1","name":"首页","url":"#home/home.html","iconCls":"iconfont icon-home","componentId":"admin-home"}],
            menuList:[],
            componentId:'',
            mappingObject:{},
            openedNames:[],
            pageHeight:0,
            wrapStyle:{},
            collapsed:true,
            menuWidth:200,
        }
    },
    created(){
        this.pageHeight = $(document).height()
        this.wrapStyle = "height:"+this.pageHeight+"px;";//"height:"+this.pageHeight+"px;";
        //加载本地缓存
        var collapsed = window.localStorage.getItem("collapsed");
        this.collapsed = eval(collapsed);
        //显示
        if ( !this.collapsed ){
            this.menuWidth = 200;
        }else{
            this.menuWidth = 55;
        }
    },
    mounted(){
        var that = this;
        this.$nextTick(()=>{
            App.request('api/user/menuList').callSuccess((res)=>{
                this.menuList = res.data;

                this.menuList.forEach((item)=>{
                    if ( item.children && item.children.length ){
                        item.children.forEach((sub)=>{
                            this.mappingObject[sub.menuId] = sub.url;
                        })
                    }else{
                        this.mappingObject[item.menuId] = item.url;
                    }
                })
                $.history.init(function(hash){
                    that.initHash(hash);
                });
            });
        })

    },
    methods:{
        closeMenu(){
            this.collapsed = !this.collapsed
            //显示
            if ( !this.collapsed ){
                this.menuWidth = 200;
            }else{
                this.menuWidth = 65;
            }
            window.localStorage.setItem("collapsed",this.collapsed)
        },
        initHash (hash) {

            var that = this;
            var currentHash = that.mappingObject[that.activeName];
            currentHash = currentHash ? currentHash.substring(1) : "";
            if (hash != "" && hash == currentHash){
                return;
            }

            var findMenuId;
            //默认首页
            if ( !hash || "#"+hash == that.homeData.url ){
                hash = that.homeData.url.substring(1);
                findMenuId = that.homeData.menuId;
            }else{
                $.each(that.mappingObject,function(r){
                    if (this == "#"+hash ){
                        findMenuId = r;
                    }
                })
                //从tabnav里面找
                $.each(that.tagNavList,function(){
                    if (this.url == "#"+hash ){
                        findMenuId = this.menuId;
                    }
                });
            }
            //如果找到菜单，没找到菜单打开临时页面
            if ( findMenuId ){
                that.activeName = findMenuId;
            }else{
                //that.loadModule(hash,new NavObject("#2",hash));
                that.activeName ="#1"
            }
        },
//添加页面子模块导航
        addSubPageTag :function(tabNav){
            var exists = false;
            var that = this;
            var menuIndex = -1;
            $.each(this.tagNavList,function(idx){
                if ( this.menuId == tabNav.menuId ){
                    exists = this;
                    //跳出循环
                    return false;
                }
                //当前菜单的下标
                if ( that.activeName == this.menuId){
                    menuIndex = idx;
                }
            })
            //插入到当前菜单tag的后面
            if ( !exists ){
                var arySize = this.tagNavList.length;
                //如果是最后的位置，直接追加
                if ( menuIndex == arySize - 1){
                    this.tagNavList.push(tabNav);
                }else{
                    let startList = this.tagNavList.slice(0,menuIndex+1);
                    let endList = this.tagNavList.slice(menuIndex+1,arySize);
                    let newAry = startList.concat(tabNav).concat(endList);
                    this.tagNavList = newAry;
                }
                this.activeName = tabNav.menuId;
            }else{
                var componentId = this.convertToComp(exists.url.substring(1));;
                if ( componentId ){
                    this.$delete(this.cacheList,this.cacheList.indexOf(componentId))
                }
                exists.name = tabNav.name;
                this.$nextTick(()=>{
                    this.activeName = tabNav.menuId;
                })
            }
        },
        //加载模块页面
        loadModule :function(url_,menuObj){
            var that = this;
            var cmpId = this.convertToComp(url_);

            //判断组件是否存在
            if ( cmpId ){
                var comp = Vue.component(cmpId);
                if ( comp ){
                    //不存在则缓存
                    if ( this.cacheList.indexOf(cmpId) == -1 ){
                        this.cacheList.push(cmpId);
                    }
                    that.$nextTick(function(){
                        that.componentId = comp;
                    })
                    return;
                }
            }
            //加载前清掉组件
            that.componentId = '';
            App.request({
                dataType:"text",
                url:pageContextPath + url_,
                success:function(res){
                    var tmp = $('<div></div>').html(res).appendTo('#componentRegister');
                    var template = $('template:eq(0)', tmp);
                    if (template[0]) {
                        App.currentMoule.template = template.html();
                        Vue.component(cmpId, App.currentMoule);
                        that.cacheList.push(cmpId)//组件缓存
                        menuObj.componentId = cmpId;
                        that.$nextTick(function(){
                            that.componentId = cmpId;
                            //5s后移除减轻页面压力
                            setTimeout(function(){
                                template.remove();
                            },5000)
                        })
                    }
                }
            }).hideLoad();
        },
        convertToComp(url){

            if ( !url ) return "";

            if ( url.indexOf('?') != -1 ){
               url = url.substring(0,url.indexOf('?'));
            }
            url = url.substring(0,url.indexOf('.'));
            url = url.replace(/\//,'-')
            return url;
        },
        //添加页面导航
        addPageTag :function(menuId,url_){
            if ( typeof menuId !== 'string' ){
                return [];
            }

            var exists = false;
            var menuObj;
            //优先从临时导航中查找
            $.each(this.tagNavList,function(){
                if ( this.menuId == menuId ){
                    exists = true;
                    menuObj = this;
                    //跳出循环
                    return false;
                }
            })
            menuObj = menuObj || this.findMenuByMenuId(menuId)

            //设置标题
            document.title = menuObj.name == '首页' ? '配置中心管理系统':menuObj.name;

            if ( !exists ){
                this.tagNavList.push(menuObj);
            }
            this.loadModule(url_,menuObj);
        },
        //根据菜单id找到菜单
        findMenuByMenuId :function(menuId){
            var that = this;

            if ( typeof menuId !== 'string' ){
                return [];
            }
            if ( menuId == this.homeData.menuId){
                return this.homeData;
            }
            var findData;
            $.each(this.menuList,function(){
                var item = this;
                if ( item.menuId == menuId ){
                    findData = item;
                    return false;
                }
                //检查子项是否存在
                if ( item.children && item.children.length ){
                    $.each(item.children,function(){
                        var sub = this;
                        if ( sub.menuId == menuId ){
                            findData = sub;
                            return false;
                        }
                    })
                    //跳出外部循环
                    if ( findData ){
                        return false;
                    }
                }
            })

            return findData;
        },
        closeCurrentTagNav :function(item){
            if ( !this.activeName || this.activeName == this.homeData.menuId ){
                return;
            }

            this.closeNav({menuId:this.activeName});
        },
        closeNav(item){
            var that = this;
            var menuId = item.menuId;
            var find = -1;
            $.each(this.tagNavList,function(i_){
                if ( this.menuId == menuId){
                    find = i_;
                    return false;
                }
            })

            if ( find != -1){
                let isCur = that.tagNavList[find].menuId == that.activeName;
                Vue.delete(that.tagNavList,find);
                //如果关闭的是当前菜单，则选定下一个菜单
                if ( isCur ){
                    //激活下一个页签，前一个找不到，就找下一个，再找不到就找首页
                    var menu = that.tagNavList[find-1] || that.tagNavList[find] || that.tagNavList[0];
                    if ( menu ){
                        that.activeName = menu.menuId;
                    }
                }
                //根据menuId找到url
                var url = this.mappingObject[menuId];
                //获取组件，清除组件缓存
                var componentId = url && this.convertToComp(url.substring(1));
                if ( componentId ){
                    this.$delete(this.cacheList,this.cacheList.indexOf(componentId))
                }
            }

        },
        handTags(item){
            this.activeName = item.menuId;
        },
        menuSelect(n){
            this.activeName = n;
        }
    },
    watch:{
        openedNames(v){
            console.log(v)
            var reallyActiveName = this.activeName.split('@')[0];
            var menuIdArray = reallyActiveName.split("-");
            var openNames = [];
            var conect = [];
            $.each(menuIdArray,function(){
                conect.push(this);
                openNames.push(conect.join('-'));
            });
        },
        //监听菜单选择
        activeName :function(name) {

            //选中系统
            var that = this;
            var reallyActiveName = name.split('@')[0];
            var menuIdArray = reallyActiveName.split("-");

            var openNames = [];
            var conect = [];
            $.each(menuIdArray,function(){
                conect.push(this);
                openNames.push(conect.join('-'));
            });
            this.openedNames = openNames;

            var url_ = this.mappingObject[name];

            //菜单中没有就到导航里面找
            if ( !url_ ){
                //从tabnav里面找
                $.each(that.tagNavList,function(){
                    if (this.menuId == name ){
                        url_ = this.url;
                    }
                });
            }

            if ( url_ ){
                if ( url_.indexOf('#') == 0 ){
                    window.location.hash=url_;
                    this.addPageTag(name,url_.substring(1));
                }
                else{
                    window.open(url_);
                }
            }

        }
    }
})