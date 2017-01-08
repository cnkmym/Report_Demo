(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('SalesTransactionMySuffixDetailController', SalesTransactionMySuffixDetailController);

    SalesTransactionMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SalesTransaction', 'ProductSalesSummary', 'EmployeeSalesSummary', 'GeneralSalesSummary', 'Employee', 'Product'];

    function SalesTransactionMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, SalesTransaction, ProductSalesSummary, EmployeeSalesSummary, GeneralSalesSummary, Employee, Product) {
        var vm = this;

        vm.salesTransaction = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportApp:salesTransactionUpdate', function(event, result) {
            vm.salesTransaction = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
