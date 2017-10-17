package state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by ttruong on 17.10.17.
 */
@Component
public class MailService {

  private static Logger LOGGER = LoggerFactory.getLogger(MailService.class);


  public void send() {
    LOGGER.info(">>>> send");
  }

}
