package com.template.flows;
import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.StudentContract;
import com.template.states.StudentState;
import net.corda.core.contracts.Command;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.*;
import java.util.*;
@InitiatingFlow
@StartableByRPC
public class StudentFlow extends FlowLogic<SignedTransaction> {
    private final String studentId;
    private final String name;
    private final int marks;
    public StudentFlow(String studentId, String name, int marks) {  this.studentId = studentId;
        this.name = name;
        this.marks = marks;
    }
    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {  Party me = getOurIdentity();
        Party notary = getServiceHub()
                .getNetworkMapCache()
                .getNotaryIdentities().get(0);
        StudentState output = new StudentState(
                studentId, name, marks, me
        );
        Command command = new Command(
                new StudentContract.Commands.Create(),
                me.getOwningKey()
        );
        TransactionBuilder txBuilder = new TransactionBuilder(notary)  .addOutputState(output, StudentContract.ID)
                .addCommand(command);
        txBuilder.verify(getServiceHub());
        SignedTransaction stx =
                getServiceHub().signInitialTransaction(txBuilder);
        return subFlow(new FinalityFlow(stx, new ArrayList<>()));  }
}
