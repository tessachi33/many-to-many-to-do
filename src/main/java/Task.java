import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;


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

 public void addCategory(Category category) {
  try(Connection con = DB.sql2o.open()) {
    String sql = "INSERT INTO categories_tasks (category_id, task_id) VALUES (:category_id, :task_id)";
    con.createQuery(sql)
      .addParameter("category_id", category.getId())
      .addParameter("task_id", this.getId())
      .executeUpdate();
  }
}

public ArrayList<Category> getCategories() {
  try(Connection con = DB.sql2o.open()){
    String sql = "SELECT category_id FROM categories_tasks WHERE task_id = :task_id";
    List<Integer> categoryIds = con.createQuery(sql)
      .addParameter("task_id", this.getId())
      .executeAndFetch(Integer.class);

    ArrayList<Category> categories = new ArrayList<Category>();

    for (Integer categoryId : categoryIds) {
        String taskQuery = "Select * From categories WHERE id = :categoryId";
        Category category = con.createQuery(taskQuery)
          .addParameter("categoryId", categoryId)
          .executeAndFetchFirst(Category.class);
        categories.add(category);
    }
    return categories;
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
