(function() {
    'use strict';

    angular
        .module('reportApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('productmySuffix', {
            parent: 'entity',
            url: '/productmySuffix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.product.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product/productsmySuffix.html',
                    controller: 'ProductMySuffixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('product');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('productmySuffix-detail', {
            parent: 'entity',
            url: '/productmySuffix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'reportApp.product.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product/productmySuffix-detail.html',
                    controller: 'ProductMySuffixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('product');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Product', function($stateParams, Product) {
                    return Product.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'productmySuffix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('productmySuffix-detail.edit', {
            parent: 'productmySuffix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/productmySuffix-dialog.html',
                    controller: 'ProductMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('productmySuffix.new', {
            parent: 'productmySuffix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/productmySuffix-dialog.html',
                    controller: 'ProductMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                productName: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('productmySuffix', null, { reload: 'productmySuffix' });
                }, function() {
                    $state.go('productmySuffix');
                });
            }]
        })
        .state('productmySuffix.edit', {
            parent: 'productmySuffix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/productmySuffix-dialog.html',
                    controller: 'ProductMySuffixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('productmySuffix', null, { reload: 'productmySuffix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
