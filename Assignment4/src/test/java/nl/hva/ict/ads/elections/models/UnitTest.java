package nl.hva.ict.ads.elections.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {
    private Candidate testCandidate;
    private Party testParty;
    static Constituency constituency;

    private final int VOTES_S1 = 11;
    private final int VOTES_S3 = 33;
    private final int VOTES_T1 = 1;

    private final int VOTES_ST = 3;


    private Party studentsParty, teachersParty;
    private Candidate student1, student2, student3a, student3b, teacher1, teacher2;
    private Candidate studentTeacher;
    private List<Candidate> studentCandidates;
    private List<Candidate> teacherCandidates;
    private PollingStation pollingStation1, pollingStation2;

    @BeforeEach
    public void setup() {

        this.constituency = new Constituency(0, "HvA");

        this.studentsParty = new Party(101,"Students Party");
        this.teachersParty = new Party(102,"Teachers Party");

        this.student1 = new Candidate("S.", null, "Leader", this.studentsParty);
        this.student2 = new Candidate("S.", null, "Vice-Leader", this.studentsParty);
        this.student3a = new Candidate("A.", null, "Student", this.studentsParty);
        this.student3b = new Candidate("A.", null, "Student", this.studentsParty);
        this.teacher1 = new Candidate("T.", null, "Leader", this.teachersParty);
        this.teacher2 = new Candidate("T.", null, "Vice-Leader", this.teachersParty);
        this.studentTeacher = new Candidate("A.", null, "Student", this.teachersParty);

        this.studentCandidates = List.of(this.student1, this.student3a);
        this.constituency.register(1, this.student1);
        this.constituency.register(3, this.student3a);
        this.teacherCandidates = List.of(this.teacher1);
        this.constituency.register(1, this.teacher1);

        this.pollingStation1 = new PollingStation("WHB", "1091GH", "Wibauthuis");
        this.pollingStation2 = new PollingStation("LWB", "1097DZ", "Leeuwenburg");
        this.constituency.add(this.pollingStation1);
        this.constituency.add(this.pollingStation2);
        pollingStation1.addVotes(this.student1,VOTES_S1);
        pollingStation1.addVotes(this.student3a,VOTES_S3);
        pollingStation1.addVotes(this.teacher1,VOTES_T1);
        pollingStation1.addVotes(this.studentTeacher,VOTES_ST);
        pollingStation2.addVotes(this.student1,VOTES_S1);
        pollingStation2.addVotes(this.student3b,VOTES_S3);
    }


    @Test
    void candidateMustHaveAPartyAfterBeingAdded() {
        this.testCandidate = new Candidate("A.", null, "Candidate");
        this. testParty = new Party(9999, "testParty");

        //Make sure they don't have been assigned to a party
        assertNull(testCandidate.getParty());
        //Make sure they have a party after being assigned
        assertEquals(testParty, testParty.addOrGetCandidate(testCandidate).getParty());
    }

    @Test()
    void InvalidZipcodeShouldNotReturnAnything(){
//        For this test we used data that was already provided in the other tests

        //Using correct inputs
        String result = "[PollingStation{id='WHB',zipCode='1091GH',name='Wibauthuis'}, PollingStation{id='LWB',zipCode='1097DZ',name='Leeuwenburg'}]";
        assertEquals(result, constituency.getPollingStationsByZipCodeRange( "1091AA","1097ZZ").toString());

        //Swapped the First and Last zipcode with each other
        try{
            constituency.getPollingStationsByZipCodeRange("1097ZZ", "1091AA");
        } catch (IllegalArgumentException exception){
            assertNotNull(exception.getMessage());
        }
    }
}
