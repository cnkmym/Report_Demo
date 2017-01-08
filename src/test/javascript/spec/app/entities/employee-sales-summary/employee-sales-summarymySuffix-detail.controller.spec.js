'use strict';

describe('Controller Tests', function() {

    describe('EmployeeSalesSummary Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockEmployeeSalesSummary, MockSalesTransaction, MockEmployee;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockEmployeeSalesSummary = jasmine.createSpy('MockEmployeeSalesSummary');
            MockSalesTransaction = jasmine.createSpy('MockSalesTransaction');
            MockEmployee = jasmine.createSpy('MockEmployee');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'EmployeeSalesSummary': MockEmployeeSalesSummary,
                'SalesTransaction': MockSalesTransaction,
                'Employee': MockEmployee
            };
            createController = function() {
                $injector.get('$controller')("EmployeeSalesSummaryMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportApp:employeeSalesSummaryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
