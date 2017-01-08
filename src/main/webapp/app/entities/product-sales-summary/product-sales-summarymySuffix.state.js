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
        })
        .state('product-sales-summarymySuffix-detail.edit', {
            parent: 'product-sales-summarymySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-sales-summary/product-sales-summarymySuffix-dialog.html',
                    controller: 'ProductSalesSummaryMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductSalesSummary', function(ProductSalesSummary) {
                            return ProductSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-sales-summarymySuffix.new', {
            parent: 'product-sales-summarymySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-sales-summary/product-sales-summarymySuffix-dialog.html',
                    controller: 'ProductSalesSummaryMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                year: null,
                                month: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('product-sales-summarymySuffix', null, { reload: 'product-sales-summarymySuffix' });
                }, function() {
                    $state.go('product-sales-summarymySuffix');
                });
            }]
        })
        .state('product-sales-summarymySuffix.edit', {
            parent: 'product-sales-summarymySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-sales-summary/product-sales-summarymySuffix-dialog.html',
                    controller: 'ProductSalesSummaryMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductSalesSummary', function(ProductSalesSummary) {
                            return ProductSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-sales-summarymySuffix', null, { reload: 'product-sales-summarymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-sales-summarymySuffix.delete', {
            parent: 'product-sales-summarymySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-sales-summary/product-sales-summarymySuffix-delete-dialog.html',
                    controller: 'ProductSalesSummaryMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductSalesSummary', function(ProductSalesSummary) {
                            return ProductSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-sales-summarymySuffix', null, { reload: 'product-sales-summarymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
