(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('ProductSalesSummaryMySuffixDetailController', ProductSalesSummaryMySuffixDetailController);

    ProductSalesSummaryMySuffixDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProductSalesSummary', 'SalesTransaction', 'Product'];

    function ProductSalesSummaryMySuffixDetailController($scope, $rootScope, $stateParams, previousState, entity, ProductSalesSummary, SalesTransaction, Product) {
        var vm = this;

        vm.productSalesSummary = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('reportApp:productSalesSummaryUpdate', function(event, result) {
            vm.productSalesSummary = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
