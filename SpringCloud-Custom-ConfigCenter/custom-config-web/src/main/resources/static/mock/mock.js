// 配置Ajax请求延时，可用来测试网络延迟大时项目中一些效果
Mock.setup({
  timeout: 500
})

MockResult = function(data){
    this.code = 200
    this.data = data
    this.message = '请求成功'
    this.success = true
}

MockPageResult = function(data){
    this.code = 200
    this.data = new App.Pager(data)
    this.message = '请求成功'
    this.success = true
}

Mock.mock(/api\/mpc\/shopdec\/catalog\/pageSubject.?/,{"code":200,"data":{"endRow":4,"firstPage":1,"hasNextPage":false,"hasPreviousPage":false,"isFirstPage":true,"isLastPage":true,"lastPage":1,"list":[{"createTime":"2018-10-30 11:13:26","creator":"admin","endTime":"2018-11-30 00:00:00","id":19,"pageNum":1,"pageSize":10,"shopId":"21","shopName":"北京王府井店","sort":1,"startTime":"2018-10-30 00:00:00","status":0,"subjectName":"首页","updateTime":"2018-11-11 10:31:39","updator":"admin"},{"createTime":"2018-10-30 15:31:21","creator":"admin","endTime":"2018-11-29 00:00:00","id":20,"pageNum":1,"pageSize":10,"shopId":"21","shopName":"北京王府井店","sort":2,"startTime":"2018-10-30 00:00:00","status":0,"subjectName":"纯粹自然","updateTime":"2018-11-07 19:39:05","updator":"admin"},{"createTime":"2018-11-07 19:37:38","creator":"admin","endTime":"2029-12-31 00:00:00","id":21,"pageNum":1,"pageSize":10,"shopId":"21","shopName":"北京王府井店","sort":3,"startTime":"2018-11-07 00:00:00","status":0,"subjectName":"纯梦空间","updateTime":"2018-11-07 19:39:00","updator":"admin"},{"createTime":"2018-11-08 09:18:03","creator":"admin","endTime":"2018-11-30 00:00:00","id":22,"pageNum":1,"pageSize":10,"shopId":"21","shopName":"北京王府井店","sort":4,"startTime":"2018-11-08 00:00:00","status":0,"subjectName":"智慧药房","updateTime":"2018-11-15 10:13:19","updator":"admin"}],"navigateFirstPage":1,"navigateLastPage":1,"navigatePages":8,"navigatepageNums":[1],"nextPage":0,"pageNum":1,"pageSize":10,"pages":1,"prePage":0,"size":4,"startRow":1,"total":4},"message":"请求成功","success":true})

Mock.mock(/api\/mpc\/shopdec\/sowingMap\/query.?/,{"code":200,"data":{"endRow":4,"firstPage":1,"hasNextPage":false,"hasPreviousPage":false,"isFirstPage":true,"isLastPage":true,"lastPage":1,"list":[{"createTime":"2018-11-02 16:08:40","creator":"admin","endTime":"2018-12-29 00:00:00","id":13,"pageNum":1,"pageSize":10,"picUrl":"http://omni-test.oss-cn-shenzhen.aliyuncs.com/omni/others/picture/d3de5103-a0e2-4d76-a64b-7f50303c91f2.jpg","shopCode":"10000","shopId":21,"shopName":"北京王府井店","showType":0,"sort":1,"startTime":"2018-11-02 00:00:00","status":0,"subjectId":19,"subjectName":"首页","updateTime":"2018-11-02 16:08:40","updator":"admin"},{"createTime":"2018-11-02 16:07:33","creator":"admin","endTime":"2018-12-31 00:00:00","id":12,"pageNum":1,"pageSize":10,"picUrl":"http://omni-test.oss-cn-shenzhen.aliyuncs.com/omni/others/picture/80a999c4-fd14-41d1-8453-25999564668e.jpg","shopCode":"10000","shopId":21,"shopName":"北京王府井店","showType":0,"sort":1,"startTime":"2018-11-02 00:00:00","status":0,"subjectId":19,"subjectName":"首页","updateTime":"2018-11-03 18:05:16","updator":"admin"},{"createTime":"2018-11-02 16:09:04","creator":"admin","endTime":"2018-12-28 00:00:00","id":14,"pageNum":1,"pageSize":10,"picUrl":"http://omni-test.oss-cn-shenzhen.aliyuncs.com/omni/others/picture/741d1f71-aff7-49ae-bbc0-4b078c5096c5.jpg","shopCode":"10000","shopId":21,"shopName":"北京王府井店","showType":0,"sort":3,"startTime":"2018-11-02 00:00:00","status":0,"subjectId":19,"subjectName":"首页","updateTime":"2018-11-02 16:09:04","updator":"admin"},{"createTime":"2018-11-02 16:09:36","creator":"admin","endTime":"2018-12-29 00:00:00","id":15,"pageNum":1,"pageSize":10,"picUrl":"http://omni-test.oss-cn-shenzhen.aliyuncs.com/omni/others/picture/98bd454c-ed7a-4eee-9da5-98a303b174bf.jpg","shopCode":"10000","shopId":21,"shopName":"北京王府井店","showType":0,"sort":4,"startTime":"2018-11-02 00:00:00","status":0,"subjectId":20,"subjectName":"纯粹自然","updateTime":"2018-11-15 11:36:49","updator":"admin"}],"navigateFirstPage":1,"navigateLastPage":1,"navigatePages":8,"navigatepageNums":[1],"nextPage":0,"pageNum":1,"pageSize":10,"pages":1,"prePage":0,"size":4,"startRow":1,"total":4},"message":"请求成功","success":true});

Mock.mock(contextPath+'api/demo/tempate',function(){
    var data = {
        list:[{
            date: '2016-05-02',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1518 弄'
        }, {
            date: '2016-05-04',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1517 弄'
        }, {
            date: '2016-05-01',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1519 弄'
        }, {
            date: '2016-05-03',
            name: '王小虎',
            address: '上海市普陀区金沙江路 1516 弄'
        }]
    }
    return new MockPageResult(data);
})

Mock.mock(contextPath+'api/demo/submit',function() {
    return new MockResult();
})
