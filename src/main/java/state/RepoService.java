package state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by ttruong on 17.10.17.
 */
@Component
public class RepoService {

  private static Logger LOGGER = LoggerFactory.getLogger(RepoService.class);

  public void create() {
    LOGGER.info(">>>> create");
  }
}
