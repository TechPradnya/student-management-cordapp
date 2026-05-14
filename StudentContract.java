package com.template.contracts;
import com.template.states.StudentState;
import net.corda.core.contracts.*;
import net.corda.core.transactions.LedgerTransaction;
import java.security.PublicKey;
import java.util.List;
import static net.corda.core.contracts.ContractsDSL.requireThat; public class StudentContract implements Contract {
    public static final String ID = "com.template.contracts.StudentContract";
    public interface Commands extends CommandData {
        class Create implements Commands {}
        class Update implements Commands {}
    }
    @Override
    public void verify(LedgerTransaction tx) {
        requireThat(require -> {
            require.using("One command required",
                    tx.getCommands().size() == 1);
            Command<CommandData> command = tx.getCommand(0);  CommandData cmd = command.getValue();
            List<PublicKey> signers = command.getSigners();
            // ===== CREATE =====
            if (cmd instanceof Commands.Create) {
                require.using("No inputs", tx.getInputs().isEmpty());  require.using("One output", tx.getOutputs().size() == 1);
                StudentState output =
                        tx.outputsOfType(StudentState.class).get(0);
                require.using("Student ID required",
                        output.getStudentId() != null && !output.getStudentId().isEmpty());
                require.using("Marks must be >= 0",
                        output.getMarks() >= 0);
                require.using("Owner must sign",
                        signers.contains(output.getOwner().getOwningKey()));  }
            // ===== UPDATE =====
            if (cmd instanceof Commands.Update) {
                require.using("One input", tx.getInputs().size() == 1);  require.using("One output", tx.getOutputs().size() == 1);
                StudentState output =
                        tx.outputsOfType(StudentState.class).get(0);
                require.using("Owner must sign",
                        signers.contains(output.getOwner().getOwningKey()));  }
            return null;
        });
    }
}
