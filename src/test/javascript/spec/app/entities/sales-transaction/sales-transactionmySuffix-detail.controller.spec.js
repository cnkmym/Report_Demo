'use strict';

describe('Controller Tests', function() {

    describe('SalesTransaction Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSalesTransaction, MockProductSalesSummary, MockEmployeeSalesSummary, MockGeneralSalesSummary, MockEmployee, MockProduct;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSalesTransaction = jasmine.createSpy('MockSalesTransaction');
            MockProductSalesSummary = jasmine.createSpy('MockProductSalesSummary');
            MockEmployeeSalesSummary = jasmine.createSpy('MockEmployeeSalesSummary');
            MockGeneralSalesSummary = jasmine.createSpy('MockGeneralSalesSummary');
            MockEmployee = jasmine.createSpy('MockEmployee');
            MockProduct = jasmine.createSpy('MockProduct');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SalesTransaction': MockSalesTransaction,
                'ProductSalesSummary': MockProductSalesSummary,
                'EmployeeSalesSummary': MockEmployeeSalesSummary,
                'GeneralSalesSummary': MockGeneralSalesSummary,
                'Employee': MockEmployee,
                'Product': MockProduct
            };
            createController = function() {
                $injector.get('$controller')("SalesTransactionMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportApp:salesTransactionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
