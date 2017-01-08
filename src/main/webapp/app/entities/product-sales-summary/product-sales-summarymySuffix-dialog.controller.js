(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('ProductSalesSummaryMySuffixDialogController', ProductSalesSummaryMySuffixDialogController);

    ProductSalesSummaryMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProductSalesSummary', 'SalesTransaction', 'Product'];

    function ProductSalesSummaryMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProductSalesSummary, SalesTransaction, Product) {
        var vm = this;

        vm.productSalesSummary = entity;
        vm.clear = clear;
        vm.save = save;
        vm.salestransactions = SalesTransaction.query();
        vm.products = Product.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.productSalesSummary.id !== null) {
                ProductSalesSummary.update(vm.productSalesSummary, onSaveSuccess, onSaveError);
            } else {
                ProductSalesSummary.save(vm.productSalesSummary, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportApp:productSalesSummaryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
