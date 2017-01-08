(function() {
    'use strict';

    angular
        .module('reportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sales-transactionmySuffix', {
            parent: 'entity',
            url: '/sales-transactionmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.salesTransaction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sales-transaction/sales-transactionsmySuffix.html',
                    controller: 'SalesTransactionMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('salesTransaction');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sales-transactionmySuffix-detail', {
            parent: 'entity',
            url: '/sales-transactionmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.salesTransaction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sales-transaction/sales-transactionmySuffix-detail.html',
                    controller: 'SalesTransactionMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('salesTransaction');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SalesTransaction', function($stateParams, SalesTransaction) {
                    return SalesTransaction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sales-transactionmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sales-transactionmySuffix-detail.edit', {
            parent: 'sales-transactionmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sales-transaction/sales-transactionmySuffix-dialog.html',
                    controller: 'SalesTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SalesTransaction', function(SalesTransaction) {
                            return SalesTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sales-transactionmySuffix.new', {
            parent: 'sales-transactionmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sales-transaction/sales-transactionmySuffix-dialog.html',
                    controller: 'SalesTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                transactionDate: null,
                                transactionAmount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sales-transactionmySuffix', null, { reload: 'sales-transactionmySuffix' });
                }, function() {
                    $state.go('sales-transactionmySuffix');
                });
            }]
        })
        .state('sales-transactionmySuffix.edit', {
            parent: 'sales-transactionmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sales-transaction/sales-transactionmySuffix-dialog.html',
                    controller: 'SalesTransactionMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SalesTransaction', function(SalesTransaction) {
                            return SalesTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sales-transactionmySuffix', null, { reload: 'sales-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sales-transactionmySuffix.delete', {
            parent: 'sales-transactionmySuffix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sales-transaction/sales-transactionmySuffix-delete-dialog.html',
                    controller: 'SalesTransactionMySuffixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SalesTransaction', function(SalesTransaction) {
                            return SalesTransaction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sales-transactionmySuffix', null, { reload: 'sales-transactionmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
