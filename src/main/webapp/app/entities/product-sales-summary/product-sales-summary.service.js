(function() {
    'use strict';
    angular
        .module('reportApp')
        .factory('ProductSalesSummary', ProductSalesSummary);

    ProductSalesSummary.$inject = ['$resource'];

    function ProductSalesSummary ($resource) {
        var resourceUrl =  'api/sales-summaries/product/:id';

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
