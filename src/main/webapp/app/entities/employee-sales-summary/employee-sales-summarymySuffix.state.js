(function() {
    'use strict';

    angular
        .module('reportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('employee-sales-summarymySuffix', {
            parent: 'entity',
            url: '/employee-sales-summarymySuffix?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.employeeSalesSummary.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-sales-summary/employee-sales-summariesmySuffix.html',
                    controller: 'EmployeeSalesSummaryMySuffixController',
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
                    $translatePartialLoader.addPart('employeeSalesSummary');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('employee-sales-summarymySuffix-detail', {
            parent: 'entity',
            url: '/employee-sales-summarymySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.employeeSalesSummary.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/employee-sales-summary/employee-sales-summarymySuffix-detail.html',
                    controller: 'EmployeeSalesSummaryMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('employeeSalesSummary');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EmployeeSalesSummary', function($stateParams, EmployeeSalesSummary) {
                    return EmployeeSalesSummary.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'employee-sales-summarymySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('employee-sales-summarymySuffix-detail.edit', {
            parent: 'employee-sales-summarymySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-sales-summary/employee-sales-summarymySuffix-dialog.html',
                    controller: 'EmployeeSalesSummaryMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmployeeSalesSummary', function(EmployeeSalesSummary) {
                            return EmployeeSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employee-sales-summarymySuffix.new', {
            parent: 'employee-sales-summarymySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-sales-summary/employee-sales-summarymySuffix-dialog.html',
                    controller: 'EmployeeSalesSummaryMySuffixDialogController',
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
                    $state.go('employee-sales-summarymySuffix', null, { reload: 'employee-sales-summarymySuffix' });
                }, function() {
                    $state.go('employee-sales-summarymySuffix');
                });
            }]
        })
        .state('employee-sales-summarymySuffix.edit', {
            parent: 'employee-sales-summarymySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-sales-summary/employee-sales-summarymySuffix-dialog.html',
                    controller: 'EmployeeSalesSummaryMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EmployeeSalesSummary', function(EmployeeSalesSummary) {
                            return EmployeeSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-sales-summarymySuffix', null, { reload: 'employee-sales-summarymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('employee-sales-summarymySuffix.delete', {
            parent: 'employee-sales-summarymySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/employee-sales-summary/employee-sales-summarymySuffix-delete-dialog.html',
                    controller: 'EmployeeSalesSummaryMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EmployeeSalesSummary', function(EmployeeSalesSummary) {
                            return EmployeeSalesSummary.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('employee-sales-summarymySuffix', null, { reload: 'employee-sales-summarymySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
