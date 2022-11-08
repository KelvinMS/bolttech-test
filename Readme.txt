

    --> **There's some ways to trigger the test**
        1. As the project has configured to the test lifecycle, you can run the tests simply using the bellow command on terminal
            and it'll run the tests provided at src/test/java/resources/testSuite.xml
            -- $ mvn test
        2. Another way is using the surefire and suiteXmlFile plugins by commandline to trigger an especific .xml file life following
            -- $ mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testSuite.xml
        3. Trigger using the TestNG running:
            --> Go to the test class under path - src/test/java/tets/functional/ConvertionTest.java
            --> right-click --> run 'className'

