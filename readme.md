Prerequisites:
Maven installed

Run test:
1. Run "mvn test" on level of pom.xml (it will run all tests)
OR
2. Open project in IDE. Running class "RunCucumberTests" will run all tests
OR
3. Open project in IDE. Go to ApiTests.feature and mark selected scenarios with @local tag (by putting @local directly above "Scenario: ...."). Run class "RunMarkedLocalTests". Only marked tests will run.
