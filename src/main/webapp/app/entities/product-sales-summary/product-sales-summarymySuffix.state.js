(function() {
    'use strict';

    angular
        .module('reportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-sales-summarymySuffix', {
            parent: 'entity',
            url: '/product-sales-summarymySuffix?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.productSalesSummary.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-sales-summary/product-sales-summariesmySuffix.html',
                    controller: 'ProductSalesSummaryMySuffixController',
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
                    $translatePartialLoader.addPart('productSalesSummary');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('product-sales-summarymySuffix-detail', {
            parent: 'entity',
            url: '/product-sales-summarymySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.productSalesSummary.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-sales-summary/product-sales-summarymySuffix-detail.html',
                    controller: 'ProductSalesSummaryMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productSalesSummary');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProductSalesSummary', function($stateParams, ProductSalesSummary) {
                    return ProductSalesSummary.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'product-sales-summarymySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        });
    }

})();
