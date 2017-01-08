(function() {
    'use strict';
    angular
        .module('reportApp')
        .factory('GeneralSalesSummary', GeneralSalesSummary);

    GeneralSalesSummary.$inject = ['$resource'];

    function GeneralSalesSummary ($resource) {
        var resourceUrl =  'api/general-sales-summaries/:id';

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
