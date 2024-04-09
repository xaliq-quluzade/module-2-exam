import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@WebServlet(urlPatterns = {"/task"})
public class TaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username =
                SecurityUtil.getCookieByName(req.getCookies(), "username").getValue();
        Integer userId = null;
        try (Connection c = JDBCConnection.connect()) {
            PreparedStatement ps = c.prepareStatement("""
                    select * from "public".person where username = ?
                    """);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");

                List<TaskDTO> tasks = TaskRepository.getAll(userId);
                PrintWriter writer = resp.getWriter();

                writer.write("""
                        {
                                "result":""" + tasks + """
                                    
                              }
                        """);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");

        PrintWriter writer = resp.getWriter();
        String username =
                SecurityUtil.getCookieByName(req.getCookies(), "username").getValue();
        Integer userId = null;
        try (Connection c = JDBCConnection.connect()) {
            PreparedStatement ps = c.prepareStatement("""
                    select * from "public".person where username = ?
                    """);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
                TaskRepository.save(name, description, "0", userId);
                writer.write("successfully added");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        int id = Integer.parseInt(req.getParameter("id"));
        PrintWriter writer = resp.getWriter();

        String username =
                SecurityUtil.getCookieByName(req.getCookies(), "username").getValue();
        try (Connection c = JDBCConnection.connect()) {
            PreparedStatement ps = c.prepareStatement("""
                    select * from "public".person where username = ?
                    """);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");

                List<TaskDTO> tasks = TaskRepository.getAll(userId);

                boolean isFind = false;
                for (TaskDTO task : tasks) {
                    if (task.id() == id && task.userId() == userId) {
                        isFind = true;
                        if (action.equals("update")) {
                            TaskRepository.update(id, name, description);
                            writer.write("successfully updated");
                        } else if (action.equals("complete")) {
                            TaskRepository.complete(id);
                            writer.write("successfully completed");
                        }
                        break;
                    }
                }
                if (!isFind) {
                    resp.setStatus(400);
                    writer.write("error");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        PrintWriter writer = resp.getWriter();

        String username =
                SecurityUtil.getCookieByName(req.getCookies(), "username").getValue();
        try (Connection c = JDBCConnection.connect()) {
            PreparedStatement ps = c.prepareStatement("""
                    select * from "public".person where username = ?
                    """);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");

                List<TaskDTO> tasks = TaskRepository.getAll(userId);

                boolean isFind = false;
                for (TaskDTO task : tasks) {
                    if (task.id() == id && task.userId() == userId) {
                        isFind = true;
                        TaskRepository.delete(id);
                        writer.write("successfully deleted");
                        break;
                    }
                }
                if (!isFind) {
                    resp.setStatus(400);
                    writer.write("error");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
