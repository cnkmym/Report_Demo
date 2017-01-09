(function() {
  'use strict';

  angular
    .module('reportApp')
    .config(stateConfig);

  stateConfig.$inject = ['$stateProvider'];

  function stateConfig($stateProvider) {
    $stateProvider
      .state('general-sales-summary-graph', {
        parent: 'graph',
        url: '/graph/general-sales-summary',
        data: {
          authorities: [], //['ROLE_USER'],
          pageTitle: 'This is Title'
        },
        views: {
          'content@': {
            templateUrl: 'app/graphs/general-sales-summary/general-sales-summary-graph.html',
            controller: 'GeneralSalesSummaryGraphController',
            controllerAs: 'vm'
          }
        },
        params: {},
        resolve: {
          translatePartialLoader: ['$translate', '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('graph');
              $translatePartialLoader.addPart('global');
              return $translate.refresh();
            }
          ]
        }
      });
  }

})();
