(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('GeneralSalesSummaryMySuffixDialogController', GeneralSalesSummaryMySuffixDialogController);

    GeneralSalesSummaryMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GeneralSalesSummary', 'SalesTransaction'];

    function GeneralSalesSummaryMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GeneralSalesSummary, SalesTransaction) {
        var vm = this;

        vm.generalSalesSummary = entity;
        vm.clear = clear;
        vm.save = save;
        vm.salestransactions = SalesTransaction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.generalSalesSummary.id !== null) {
                GeneralSalesSummary.update(vm.generalSalesSummary, onSaveSuccess, onSaveError);
            } else {
                GeneralSalesSummary.save(vm.generalSalesSummary, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportApp:generalSalesSummaryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
