package com.kakao.hotire.springcore.di;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TestApplicationContextTest {

  @Test
  public void getBean() {
    // Given
    final TestApplicationContext testApplicationContext = new TestApplicationContext();

    // When
    final TestController testController = testApplicationContext.getBean(TestController.class);

    // Then
    assertThat(testController).isNotNull();
  }

  @Test
  public void getBeanIsSingleTon() {
    // Given
    final TestApplicationContext testApplicationContext = new TestApplicationContext();

    // When
    final TestController testController = testApplicationContext.getBean(TestController.class);
    final TestController testController2 = testApplicationContext.getBean(TestController.class);

    // Then
    assertThat(testController).isEqualTo(testController2);
  }

  @Test
  public void di() {
    // Given
    final TestApplicationContext testApplicationContext = new TestApplicationContext();

    // When
    final TestController testController = testApplicationContext.getBean(TestController.class);
    final TestService testService = testApplicationContext.getBean(TestService.class);

    // Then
    assertThat(testController.getTestService()).isNotNull();
    assertThat(testController.getTestService()).isEqualTo(testService);
  }

  @Test
  public void di_mock() {
    // Given
    final TestService testService = mock(TestService.class);
    final Map<Class<?>, Object> mockMap = new HashMap<>();
    mockMap.put(TestService.class, testService);
    final TestApplicationContext testApplicationContext = new TestApplicationContext(mockMap);

    // When
    TestController testController = testApplicationContext.getBean(TestController.class);

    // Then
    assertThat(testController).isNotNull();
    assertThat(testController.getTestService()).isEqualTo(testService);
  }
}