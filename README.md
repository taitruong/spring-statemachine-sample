# Example with Spring Statemachine and Spring Boot
PoC with one main state and two nested sub states

- CONTACT_REQUEST
 - CREATE_REQUEST
   - SEND_REQUEST
   
```
public class ContactRequestStateMachine extends EnumStateMachineConfigurerAdapter<ContactRequestStateMachine.States, ContactRequestStateMachine.Events> {
...
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
...
```

# Test class ApplicationTest

# Resources
[UML state machine, Wikipedia](https://en.wikipedia.org/wiki/UML_state_machine)
Very good explanation on Wikipedia

[Spring Reference Documentation, based on Showcase sample](https://docs.spring.io/spring-statemachine/docs/1.2.6.RELEASE/reference/htmlsingle/#statemachine-examples-showcase)
Spring Documentation with lots of samples and [crash course in the appendix](https://docs.spring.io/spring-statemachine/docs/1.2.6.RELEASE/reference/htmlsingle/#crashcourse)

[Spring Statemachine project site](https://projects.spring.io/spring-statemachine/)

