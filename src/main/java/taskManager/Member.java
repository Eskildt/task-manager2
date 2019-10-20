package taskManager;

import java.util.Objects;

public class Member {

    public String task;
    public String email;
    private String name;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){
        return "{Member: " + name + " Project: " + task + " Email: " + email + "}";
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(name, member.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name);
    }

    public void setTask(String task){
        this.task = task;
    }

    public String getTask(){
        return task;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
