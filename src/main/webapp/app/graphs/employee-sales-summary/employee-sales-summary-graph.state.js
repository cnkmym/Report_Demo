(function() {
  'use strict';

  angular
    .module('reportApp')
    .config(stateConfig);

  stateConfig.$inject = ['$stateProvider'];

  function stateConfig($stateProvider) {
    $stateProvider
      .state('employee-sales-summary-graph', {
        parent: 'graph',
        url: '/graph/employee-sales-summary',
        data: {
          authorities: [], //['ROLE_USER'],
          pageTitle: 'This is Title'
        },
        views: {
          'content@': {
            templateUrl: 'app/graphs/employee-sales-summary/employee-sales-summary-graph.html',
            controller: 'EmployeeSalesSummaryGraphController',
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
