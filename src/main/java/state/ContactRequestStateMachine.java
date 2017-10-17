package state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

/**
 * Created by ttruong on 05.10.17.
 */
@Configuration
@EnableStateMachine
@WithStateMachine
public class ContactRequestStateMachine extends EnumStateMachineConfigurerAdapter<ContactRequestStateMachine.States, ContactRequestStateMachine.Events> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ContactRequestStateMachine.class);

  enum States {

    IDLE,
    // Use Case 1 - main state ...
    CONTACT_REQUEST,
    // with its sub states
    CREATE_REQUEST,
    SEND_REQUEST,
    READ,
    DELETE,
    END
  }

  enum Events {

    CREATE, READ, DELETE
  }

  @Autowired
  private RepoService repoService;

  @Autowired
  private MailService mailService;

  @Autowired
  private ValidationService validationService;

  @Override
  public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
    config.withConfiguration().autoStartup(true);
  }

  @Override
  public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
    states
            .withStates()
            .initial(States.IDLE)
            .state(States.IDLE)
            .and()
            .withStates()
              .parent(States.IDLE)
              .initial(States.END)
              .state(States.CONTACT_REQUEST)
              .and()
              .withStates()
                .parent(States.CONTACT_REQUEST)
                .initial(States.CREATE_REQUEST)
                .state(States.CREATE_REQUEST, createAction(), null)
                .and()
                .withStates()
                  .parent(States.CREATE_REQUEST)
                  .initial(States.SEND_REQUEST)
                  .state(States.SEND_REQUEST, sendAction(), null);
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
    transitions
            .withExternal()
            .source(States.IDLE).target(States.CONTACT_REQUEST)
            .event(Events.CREATE)
            .guard(validateGuard())
    ;
  }

  public Action<States, Events> createAction() {
    return ctx -> {
      repoService.create();
    };
  }

  public Action<States, Events> sendAction() {
    return ctx -> {
      mailService.send();
    };
  }

  public Guard<States, Events> validateGuard() {
    return ctx -> {
      validationService.validate();
      return true;
    };
  }

}
