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
            'update': { method:'PUT' }
        });
    }
})();
