(function() {
    'use strict';
    angular
        .module('reportApp')
        .factory('SalesTransaction', SalesTransaction);

    SalesTransaction.$inject = ['$resource', 'DateUtils'];

    function SalesTransaction ($resource, DateUtils) {
        var resourceUrl =  'api/sales-transactions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.transactionDate = DateUtils.convertDateTimeFromServer(data.transactionDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
