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
                authorities: [],//['ROLE_USER'],
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
                authorities: [],//['ROLE_USER'],
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
        });
    }

})();
