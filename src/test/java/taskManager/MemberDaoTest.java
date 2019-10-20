package taskManager;

import org.assertj.core.api.Assertions;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberDaoTest {

    JdbcDataSource dataSource = TestDatabase.testDataSource();

    @Test
    public void shouldListInsertedMembers() throws SQLException {
        MemberDao memberDao = new MemberDao(dataSource);
        Member member = sampleMember();
        memberDao.insert(member);
        Assertions.assertThat(memberDao.listAll()).contains(member);
    }

    @Test
    void shouldSaveAllMemberFields()throws SQLException{
        MemberDao memberDao = new MemberDao(dataSource);
        Member member = sampleMember();
        assertThat(member).hasNoNullFieldsOrProperties();
        long id = memberDao.insert(member);
        assertThat(memberDao.retrieve(id)).isEqualToComparingFieldByField(member);
    }

    private Member sampleMember(){
        Member member = new Member();
        member.setName(sampleMemberName());
        member.setTask(sampleRandomTask());
        member.setEmail(sampleEmail());
        return member;
    }

    private String sampleMemberName(){
        String[] alternatives = {
                "Bjørg", "Bjarne", "Bjarte", "Brage"
        };
        return alternatives[new Random().nextInt(alternatives.length)];
    }

    private String sampleRandomTask(){
        String[] alternatives = {
                "Trene", "Programmere", "Lage mat", "Se på TV"
        };

        return alternatives[new Random().nextInt(alternatives.length)];
    }

    private String sampleEmail(){
        String[] alternatives ={
                "bjørg@mail.com", "bjarne@mail.com", "bjarte@yahoo.com", "brage@hotmail.com"
        };
        return alternatives[new Random().nextInt(alternatives.length)];
    }

}
