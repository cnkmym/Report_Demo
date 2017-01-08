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
        })
        .state('general-sales-summarymySuffix-detail.edit', {
            parent: 'general-sales-summarymySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/general-sales-summary/general-sales-summarymySuffix-dialog.html',
                    controller: 'GeneralSalesSummaryMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GeneralSalesSummary', function(GeneralSalesSummary) {
                            return GeneralSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('general-sales-summarymySuffix.new', {
            parent: 'general-sales-summarymySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/general-sales-summary/general-sales-summarymySuffix-dialog.html',
                    controller: 'GeneralSalesSummaryMySuffixDialogController',
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
                    $state.go('general-sales-summarymySuffix', null, { reload: 'general-sales-summarymySuffix' });
                }, function() {
                    $state.go('general-sales-summarymySuffix');
                });
            }]
        })
        .state('general-sales-summarymySuffix.edit', {
            parent: 'general-sales-summarymySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/general-sales-summary/general-sales-summarymySuffix-dialog.html',
                    controller: 'GeneralSalesSummaryMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GeneralSalesSummary', function(GeneralSalesSummary) {
                            return GeneralSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('general-sales-summarymySuffix', null, { reload: 'general-sales-summarymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('general-sales-summarymySuffix.delete', {
            parent: 'general-sales-summarymySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/general-sales-summary/general-sales-summarymySuffix-delete-dialog.html',
                    controller: 'GeneralSalesSummaryMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GeneralSalesSummary', function(GeneralSalesSummary) {
                            return GeneralSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('general-sales-summarymySuffix', null, { reload: 'general-sales-summarymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
