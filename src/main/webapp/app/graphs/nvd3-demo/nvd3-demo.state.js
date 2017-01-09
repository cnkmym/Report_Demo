(function() {
    'use strict';

    angular
        .module('reportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('nvd3-demomySuffix', {
            parent: 'graph',
            url: '/graph/nvd3-demo',
            data: {
                authorities: [],//['ROLE_USER'],
                pageTitle: 'This is Title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/graphs/nvd3-demo/nvd3-demo.html',
                    controller: 'Nvd3DemoController',
                    controllerAs: 'vm'
                }
            },
            params: {
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('nvd3Demo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        });
    }

})();
