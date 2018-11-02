package tma.datraining.integrationTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tma.datraining.integrationTest.controller.LocationControllerIntegrationTest;
import tma.datraining.integrationTest.controller.ProductControllerIntegrationTest;
import tma.datraining.integrationTest.controller.TimeControllerIntegrationTest;

@RunWith(Suite.class)
@SuiteClasses({LocationControllerIntegrationTest.class,ProductControllerIntegrationTest.class,TimeControllerIntegrationTest.class})
public class TestSuiteIntegration {

}
