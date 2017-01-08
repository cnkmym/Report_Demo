(function() {
    'use strict';

    angular
        .module('reportApp')
        .controller('EmployeeSalesSummaryMySuffixDialogController', EmployeeSalesSummaryMySuffixDialogController);

    EmployeeSalesSummaryMySuffixDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EmployeeSalesSummary', 'SalesTransaction', 'Employee'];

    function EmployeeSalesSummaryMySuffixDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EmployeeSalesSummary, SalesTransaction, Employee) {
        var vm = this;

        vm.employeeSalesSummary = entity;
        vm.clear = clear;
        vm.save = save;
        vm.salestransactions = SalesTransaction.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.employeeSalesSummary.id !== null) {
                EmployeeSalesSummary.update(vm.employeeSalesSummary, onSaveSuccess, onSaveError);
            } else {
                EmployeeSalesSummary.save(vm.employeeSalesSummary, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('reportApp:employeeSalesSummaryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
