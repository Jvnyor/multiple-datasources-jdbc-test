package com.jvnyor.multdatasourcesjdbc.infra.database.dao.todo;

import com.jvnyor.multdatasourcesjdbc.model.todo.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TodoDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoDAO.class);

    private static final String INSERT_TODO = "INSERT INTO todos (name) VALUES (?)";

    private static final String SELECT_ALL_TODOS = "SELECT * FROM todos";

    private static final String SELECT_TODO_BY_ID = "SELECT * FROM todos WHERE id = ?";

    private static final String UPDATE_TODO = "UPDATE todos SET name = ? WHERE id = ?";

    @Qualifier("todosDataSource")
    private final DataSource todosDataSource;

    public TodoDAO(DataSource todosDataSource) {
        this.todosDataSource = todosDataSource;
    }

    public void save(Todo todo) {
        LOGGER.info("Saving todo");

        try (Connection conn = todosDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_TODO)) {
            ps.setString(1, todo.name());
            int executedUpdate = ps.executeUpdate();

            LOGGER.info("Todo saved, rows affected: {}", executedUpdate);
        } catch (Exception e) {
            LOGGER.error("Error saving todo", e);
        }

        LOGGER.info("Todo saved");
    }

    public void saveAll(List<Todo> todos) {
        LOGGER.info("Saving all todos");

        try (Connection conn = todosDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_TODO)) {
            for (Todo todo : todos) {
                ps.setString(1, todo.name());
                ps.addBatch();
            }

            int[] executedUpdate = ps.executeBatch();

            LOGGER.info("All todos saved, rows affected: {}", executedUpdate.length);
        } catch (Exception e) {
            LOGGER.error("Error saving all todos", e);
        }

        LOGGER.info("All todos saved");
    }

    public void update(Todo todo) {
        LOGGER.info("Updating todo");

        try (Connection conn = todosDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_TODO)) {
            ps.setString(1, todo.name());
            ps.setLong(2, todo.id());

            int executedUpdate = ps.executeUpdate();

            LOGGER.info("Todo updated, rows affected: {}", executedUpdate);
        } catch (Exception e) {
            LOGGER.error("Error updating todo", e);
        }

        LOGGER.info("Todo updated");
    }

    public void updateAll(List<Todo> todos) {
        LOGGER.info("Updating all todos");

        try (Connection conn = todosDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_TODO)) {
            for (Todo todo : todos) {
                ps.setString(1, todo.name());
                ps.setLong(2, todo.id());
                ps.addBatch();
            }

            int[] executedUpdate = ps.executeBatch();

            LOGGER.info("All todos updated, rows affected: {}", executedUpdate.length);
        } catch (Exception e) {
            LOGGER.error("Error updating all todos", e);
        }

        LOGGER.info("All todos updated");
    }

    public List<Todo> findAll() {
        LOGGER.info("Finding all todos");

        var todos = new ArrayList<Todo>();

        try (Connection conn = todosDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_TODOS);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                todos.add(new Todo(rs.getLong("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            LOGGER.error("Error finding all todos", e);
        }

        LOGGER.info("All todos found");
        return todos;
    }

    public Optional<Todo> findById(Long id) {
        LOGGER.info("Finding todo by id");

        try (Connection conn = todosDataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_TODO_BY_ID)) {
            ps.setLong(1, id);

            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Todo(rs.getLong("id"), rs.getString("name")));
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error finding todo by id", e);
        }

        LOGGER.info("Todo found by id");
        return Optional.empty();
    }
}
