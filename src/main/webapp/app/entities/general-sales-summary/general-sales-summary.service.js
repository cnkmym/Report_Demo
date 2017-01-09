(function() {
  'use strict';
  angular
    .module('reportApp')
    .factory('GeneralSalesSummary', GeneralSalesSummary);

  GeneralSalesSummary.$inject = ['$resource'];

  function GeneralSalesSummary($resource) {
    var resourceUrl = 'api/sales-summaries/general/:id';

    return $resource(resourceUrl, {}, {
      'query': {
        method: 'GET',
        isArray: true
      },
      'get': {
        method: 'GET',
        transformResponse: function(data) {
          if (data) {
            data = angular.fromJson(data);
          }
          return data;
        }
      },
      'search': {
        url: 'api/sales-summaries/general/search',
        method: 'GET',
        isArray: true
      },
      'detail':{
        url: 'api/sales-summaries/general/detail/:id',
        method: 'GET',
        isArray: true
      }
    });
  }
})();
