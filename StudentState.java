package com.template.states;
import com.template.contracts.StudentContract;
import net.corda.core.contracts.*;
import net.corda.core.identity.*;
import java.util.*;
@BelongsToContract(StudentContract.class)
public class StudentState implements ContractState {
    private final String studentId;
    private final String name;
    private final int marks;
    private final Party owner;
    public StudentState(String studentId, String name, int marks, Party owner) {  this.studentId = studentId;
        this.name = name;
        this.marks = marks;
        this.owner = owner;
    }
    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public int getMarks() { return marks; }
    public Party getOwner() { return owner; }
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(owner);
    }
}
