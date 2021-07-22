package com.github.hotire.springcore.register;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class SimpleBeanConsumer {
  private com.github.hotire.springcore.register.SimpleBean simpleBean;
  public SimpleBeanConsumer(SimpleBean simpleBean) {
    this.simpleBean = simpleBean;
    log.info("SimpleBeanConsumer SimpleBean hashcode : {}", this.simpleBean.hashCode());
  }
}