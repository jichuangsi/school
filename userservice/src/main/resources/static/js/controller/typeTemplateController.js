/** 定义控制器层 */
app.controller('typeTemplateController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function(page, rows){
        baseService.findByPage("/typeTemplate/findByPage", page,
			rows, $scope.searchEntity)
            .then(function(response){
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        var url = "save";
        if ($scope.entity.id){
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/typeTemplate/" + url, $scope.entity)
            .then(function(response){
                if (response.data){
                    /** 重新加载数据 */
                    $scope.reload();
                }else{
                    alert("操作失败！");
                }
            });
    };

    /** 显示修改 */
    $scope.show = function(entity){
       /** 把json对象转化成一个新的json对象 */
       $scope.entity = JSON.parse(JSON.stringify(entity));
        // 把品牌json字符串转化成json数组
        $scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
        // 把规格json字符串转化成json数组
        $scope.entity.specIds = JSON.parse($scope.entity.specIds);
        // 把扩展属性json字符串转化成json数组
        $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/typeTemplate/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };

    /** 查询品牌数据 */
    $scope.findBrandList = function(){
        baseService.sendGet("/brand/findBrandList").then(function(response){
            // select2组件需要的数据 [{id : '', name : ''}]
            $scope.brandList = {data: response.data};
        });
    };
    /** 查询规格数据 */
    $scope.findSpecList = function(){
        baseService.sendGet("/specification/findSpecList").then(function(response){
            // select2组件需要的数据 [{id : '', spec_name : ''}]
            $scope.specList = {data: response.data};
        });
    };


    // 增加一行
    $scope.addTableRow = function(){
        // 数组中添加元素
        $scope.entity.customAttributeItems.push({});
    };

    // 删除一行
    $scope.deleteTableRow = function(idx){
        // 数组中删除元素
        $scope.entity.customAttributeItems.splice(idx, 1)
    };


    var json = {"id":1,"text":"联想"};
   // var key = "text";
   // alert(json["text"]);
});