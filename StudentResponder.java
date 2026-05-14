package com.template.flows;
import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
@InitiatedBy(StudentFlow.class)
public class StudentResponder extends FlowLogic<SignedTransaction> {  private final FlowSession counterpartySession;
    public StudentResponder(FlowSession counterpartySession) {  this.counterpartySession = counterpartySession;
    }
    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        return subFlow(
                new ReceiveFinalityFlow(counterpartySession)  );
    }
}
