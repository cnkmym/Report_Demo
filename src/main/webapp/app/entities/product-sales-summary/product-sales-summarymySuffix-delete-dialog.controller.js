(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('ProductSalesSummaryMySuffixDeleteController',ProductSalesSummaryMySuffixDeleteController);

    ProductSalesSummaryMySuffixDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProductSalesSummary'];

    function ProductSalesSummaryMySuffixDeleteController($uibModalInstance, entity, ProductSalesSummary) {
        var vm = this;

        vm.productSalesSummary = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProductSalesSummary.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
