(function() {
    'use strict';
    angular
        .module('reportApp')
        .factory('Nvd3Demo', Nvd3Demo);

    Nvd3Demo.$inject = ['$resource'];

    function Nvd3Demo ($resource) {
        var resourceUrl =  'api/sales-summaries/general';

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
            }
        });
    }
})();
