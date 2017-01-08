'use strict';

describe('Controller Tests', function() {

    describe('GeneralSalesSummary Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGeneralSalesSummary, MockSalesTransaction;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGeneralSalesSummary = jasmine.createSpy('MockGeneralSalesSummary');
            MockSalesTransaction = jasmine.createSpy('MockSalesTransaction');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'GeneralSalesSummary': MockGeneralSalesSummary,
                'SalesTransaction': MockSalesTransaction
            };
            createController = function() {
                $injector.get('$controller')("GeneralSalesSummaryMySuffixDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'reportApp:generalSalesSummaryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
