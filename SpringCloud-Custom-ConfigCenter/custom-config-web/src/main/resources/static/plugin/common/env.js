/**
 *  环境信息
 *  @author zhdong
 *  @date 2018/8/26
 */
var contextPath = "/";//上下文根

var LOCAL_STORAGE_TABBTN_KEY = "tabBtnStorage";//导航存储key
var pageContextPath = contextPath+"pages/";//页面路径
var OSSAccessDomain= "oss-cn-shenzhen.aliyuncs.com";


//调试模式
var DEBUGGER = false;
//首页配置
var HOME_NAV_TAB_OBJ = {
    id: "#1",
    menuId: "#1",
    name: "首页",
    url: "#home/home.html",
    iconCls: "ios-home-outline",
    componentId:"admin-home"
};

// //浏览器检查，是否支持es6表达式
// try {
//     //表达式检查
//     eval('var a1 = { func(){ }, func2: () => { } };');
//     [].findIndex(function(){})
//     const aaaaaaaaa = 1;
//
//
//
//
// } catch (e) {
//     window.location.href=contextPath+"pages/support/support.html";
// }