/** 定义基础服务层 */
app.service('baseService', function($http){

    /** 发送get请求(带请求参数或不带请求参数) */
    this.sendGet = function(url, data){
        if (data){
            url = url + "?" + data;
        }
        return $http.get(url);
    };

    /** 发送post请求(带请求参数或不带请求参数) */
    this.sendPost = function(url, data){
        if (data) {
            return $http.post(url, data);
        }else{
            return $http.post(url);
        }
    };

    /** 根据主键id查询 */
    this.findOne = function(url, id){
        return this.sendGet(url, "id=" + id);
    };

    /** 分页查询(带查询条件或不带查询条件) */
    this.findByPage = function(url, page, rows, data){
        /** 定义分页URL */
        url += '?page='+ page +'&rows=' + rows;
        if (data && JSON.stringify(data) != "{}"){
            return $http({
                method : 'get',
                url : url,
                params : data
            });
        }else{
            return this.sendGet(url);
        }
    };

    /** 删除或批量删除 */
    this.deleteById = function(url, ids){
        /** 判断ids是否为数组 */
        if (ids instanceof Array){
            return this.sendGet(url, "ids=" + ids);
        }else{
            return this.sendGet(url, "id=" + ids);
        }
    };
});