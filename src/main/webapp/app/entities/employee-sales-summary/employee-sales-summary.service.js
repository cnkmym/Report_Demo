(function() {
    'use strict';
    angular
        .module('reportApp')
        .factory('EmployeeSalesSummary', EmployeeSalesSummary);

    EmployeeSalesSummary.$inject = ['$resource'];

    function EmployeeSalesSummary ($resource) {
        var resourceUrl =  'api/sales-summaries/employee/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'search': {
              url: 'api/sales-summaries/employee/search',
              method: 'GET',
              isArray: true,
              transformResponse: function (data) {
                  var ret = [];
                  if (data) {
                    var json = angular.fromJson(data);
                    for (var i in json){
                        var item = angular.fromJson(i);
                        item.value = json[i];
                        ret.push(item);
                    }
                  }
                  return ret;
              }
            },
            'detail':{
              url: 'api/sales-summaries/employee/detail/:id',
              method: 'GET',
              isArray: true
            }
        });
    }
})();
