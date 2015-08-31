import java.util.List;
import org.sql2o.*;

public class Task {
  private int id;
  private String name;


  public int getId() {
    return id;
  }



  public String getName() {
    return name;
  }

  public Task(String name) {
    this.name = name;

  }

  @Override
  public boolean equals(Object otherTask) {
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getName().equals(newTask.getName()) &&
             this.getId() == newTask.getId();

    }
  }

  public static List<Task> all() {
  String sql = "SELECT id, name FROM tasks";
  try(Connection con = DB.sql2o.open()) {
    return con.createQuery(sql).executeAndFetch(Task.class);
  }
 }

  public void save() {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO tasks (name) VALUES (:name)";
    this.id = (int) con.createQuery(sql, true)
      .addParameter("name", this.name)
      .executeUpdate()
      .getKey();
   }
}
   public static Task find(int id) {
   try(Connection con = DB.sql2o.open()) {
     String sql = "SELECT * FROM tasks where id=:id";
     Task task = con.createQuery(sql)
       .addParameter("id", id)
       .executeAndFetchFirst(Task.class);
     return task;
   }
 }
}
