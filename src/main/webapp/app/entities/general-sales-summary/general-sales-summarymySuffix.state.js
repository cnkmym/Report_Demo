(function() {
    'use strict';

    angular
        .module('reportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('general-sales-summarymySuffix', {
            parent: 'entity',
            url: '/general-sales-summarymySuffix?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.generalSalesSummary.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/general-sales-summary/general-sales-summariesmySuffix.html',
                    controller: 'GeneralSalesSummaryMySuffixController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('generalSalesSummary');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('general-sales-summarymySuffix-detail', {
            parent: 'entity',
            url: '/general-sales-summarymySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.generalSalesSummary.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/general-sales-summary/general-sales-summarymySuffix-detail.html',
                    controller: 'GeneralSalesSummaryMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('generalSalesSummary');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GeneralSalesSummary', function($stateParams, GeneralSalesSummary) {
                    return GeneralSalesSummary.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'general-sales-summarymySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        });
    }

})();
