import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    static List<TaskDTO> getAll(int userId) {
        try (Connection c = JDBCConnection.connect()) {
            List<TaskDTO> results = new ArrayList<>();
            PreparedStatement ps = c.prepareStatement("""
                    select * from "public".task where user_id = ?
                    """);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String status = rs.getString("status");

                results.add(new TaskDTO(id, name, description, status, userId));
            }
            return results;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void save(String name, String description, String status, int userId) {
        try (Connection c = JDBCConnection.connect()) {
            PreparedStatement ps = c.prepareStatement("""
                    insert into "public".task(name,description,status,user_id)
                    values(?,?,?,?)
                    """);

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setString(3, status);
            ps.setInt(4, userId);

            ps.execute();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void update(int id, String name, String description) {
        try (Connection c = JDBCConnection.connect()) {

            PreparedStatement ps = c.prepareStatement("""
                    UPDATE "public".task
                    SET name = ?, description = ?
                    WHERE id = ?
                    """);

            ps.setString(1, name);
            ps.setString(2, description);
            ps.setInt(3, id);

            ps.execute();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void complete(int id) {
        try (Connection c = JDBCConnection.connect()) {
            PreparedStatement ps = c.prepareStatement("""
                    UPDATE "public".task
                    SET status = 1
                    WHERE id = ?
                    """);

            ps.setInt(1, id);

            ps.execute();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static void delete(int id) {
        try (Connection c = JDBCConnection.connect()) {
            PreparedStatement ps = c.prepareStatement("""
                    delete from "public".task where id = ?
                    """);

            ps.setInt(1, id);

            ps.execute();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
