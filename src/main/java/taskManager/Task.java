package taskManager;

import java.util.Objects;

public class Task {

    private String name;
    private String task;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setTask(String task){
        this.task = task;
    }

    public String getTask() {
        return task;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(name);
    }

    @Override
    public String toString(){
        return "{Project: " + name + "}";
    }
}
