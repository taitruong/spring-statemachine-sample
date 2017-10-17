package state;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.StateMachineUtils;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ttruong on 16.10.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

  @Autowired
  private StateMachine<ContactRequestStateMachine.States, ContactRequestStateMachine.Events> stateMachine;

  @Test
  public void testStates() {
    StateMachineTestPlan<ContactRequestStateMachine.States, ContactRequestStateMachine.Events> plan =
            StateMachineTestPlanBuilder.<ContactRequestStateMachine.States, ContactRequestStateMachine.Events>builder()
            .stateMachine(stateMachine)
            .step()
              .expectState(ContactRequestStateMachine.States.IDLE)
              .and()
            .step()
                    .sendEvent(ContactRequestStateMachine.Events.CREATE)
              //.sendEvent(MessageBuilder.withPayload(ContactRequestStateMachine.Events.CREATE.setHeader("context", "1").build())
              .expectStateChanged(1)
                    .expectStates(ContactRequestStateMachine.States.CONTACT_REQUEST,
                            ContactRequestStateMachine.States.CREATE_REQUEST,
                            ContactRequestStateMachine.States.SEND_REQUEST)
                    //.expectVariable("context", "1")
              .and()
            .build();
    //System.out.println("================START 2======================");
    //stateMachine.sendEvent(ContactRequestStateMachine.Events.CREATE);
  }

  @Test
  public void testCreate() {
    stateMachine.sendEvent(ContactRequestStateMachine.Events.CREATE);
  }
}
